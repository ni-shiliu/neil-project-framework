package com.neil.myth.core.mq.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.neil.myth.common.bean.context.MythTransactionContext;
import com.neil.myth.common.bean.entity.MythInvocation;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.bean.mq.MessageEntity;
import com.neil.myth.common.bean.threadlocal.TransactionContextLocal;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.common.enums.EventTypeEnum;
import com.neil.myth.common.enums.MythRoleEnum;
import com.neil.myth.common.enums.MythStatusEnum;
import com.neil.myth.common.serializer.Serializer;
import com.neil.myth.core.event.MythTransactionEventPublisher;
import com.neil.myth.core.service.MythCoordinatorService;
import com.neil.myth.core.service.MythMqReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author nihao
 * @date 2024/6/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MythMqReceiveServiceImpl implements MythMqReceiveService {

    private final MythCoordinatorService mythCoordinatorService;

    private final MythConfig mythConfig;

    private final MythTransactionEventPublisher publisher;

    private Serializer serializer;

    private static final Lock LOCK = new ReentrantLock();

    @Override
    public Boolean processMessage(byte[] body) throws Exception {
        MessageEntity messageEntity = getObjectSerializer().deSerialize(body, MessageEntity.class);
        MythTransaction mythTransaction = mythCoordinatorService.findByTransId(messageEntity.getTransId());
        LOCK.lock();
        try {
            if (Objects.isNull(mythTransaction)) {
                doProcessMessage(messageEntity);
            } else {
                reProcessMessage(messageEntity, mythTransaction);
            }
        } catch (Exception e) {
            if (Objects.isNull(mythTransaction)) {
                MythTransaction log = generateMythTransaction(messageEntity.getTransId(), getExceptionMessage(e),
                        MythStatusEnum.FAILURE,
                        messageEntity.getMythInvocation().getTargetClass().getName(),
                        messageEntity.getMythInvocation().getMethodName(),
                        messageEntity.getMythInvocation().getArgs());
                publisher.publishEvent(log, EventTypeEnum.SAVE);
            } else {
                mythTransaction.setErrorMsg(getExceptionMessage(e));
                mythTransaction.setRetryCount((mythTransaction.getRetryCount() == null
                        ? 0
                        : mythTransaction.getRetryCount()) + 1);
                publisher.publishEvent(mythTransaction, EventTypeEnum.UPDATE_FAIR);
            }
            throw e;
        } finally {
            TransactionContextLocal.getInstance().remove();
            LOCK.unlock();
        }
        return true;
    }

    private void doProcessMessage(MessageEntity messageEntity) throws Exception {
        execute(messageEntity);
        final MythTransaction log = generateMythTransaction(messageEntity.getTransId(), null,
                MythStatusEnum.COMMIT,
                messageEntity.getMythInvocation().getTargetClass().getName(),
                messageEntity.getMythInvocation().getMethodName(),
                messageEntity.getMythInvocation().getArgs());
        publisher.publishEvent(log, EventTypeEnum.SAVE);
    }

    private MythTransaction generateMythTransaction(String transId, String errorMsg, MythStatusEnum status, String targetClass, String methodName, Object[] args) {
        MythTransaction logTransaction = new MythTransaction(transId);
        logTransaction.setStatus(status);
        logTransaction.setErrorMsg(errorMsg);
        logTransaction.setRole(MythRoleEnum.PROVIDER);
        logTransaction.setTargetClass(targetClass);
        logTransaction.setTargetMethod(methodName);
        logTransaction.setRetryCount(1);
        logTransaction.setArgs(JSONUtil.toJsonStr(args));
        return logTransaction;
    }

    private void reProcessMessage(MessageEntity messageEntity, MythTransaction mythTransaction) throws Exception {
        if (!MythStatusEnum.FAILURE.equals(mythTransaction.getStatus())) {
            return;
        }
        if (mythTransaction.getRetryCount() >= mythConfig.getRetryMax()) {
            log.debug("trans_id: {}, 超过最大重试次数", mythTransaction.getTransId());
            return;
        }
        execute(messageEntity);
        //执行成功 更新日志为成功
        mythTransaction.setStatus(MythStatusEnum.COMMIT);
        mythTransaction.setRetryCount((mythTransaction.getRetryCount() == null
                ? 0
                : mythTransaction.getRetryCount()) + 1);
        publisher.publishEvent(mythTransaction, EventTypeEnum.UPDATE_STATUS);
    }

    private void execute(MessageEntity messageEntity) throws Exception {
        MythTransactionContext context = new MythTransactionContext();
        context.setTransId(messageEntity.getTransId());
        context.setMythRole(MythRoleEnum.LOCAL);
        TransactionContextLocal.getInstance().set(context);
        executeLocalTransaction(messageEntity.getMythInvocation());
    }

    private void executeLocalTransaction(MythInvocation mythInvocation) throws Exception {
        if (Objects.isNull(mythInvocation)) {
            return;
        }

        Class clazz = mythInvocation.getTargetClass();
        String method = mythInvocation.getMethodName();
        Object[] args = mythInvocation.getArgs();
        Class[] parameterTypes = mythInvocation.getParameterTypes();
        Object bean = SpringUtil.getBean(clazz);
        MethodUtils.invokeMethod(bean, method, args, parameterTypes);
    }

    private String getExceptionMessage(Throwable e) {
        String exceptionMessage = e.getMessage();
        if (exceptionMessage == null && e instanceof InvocationTargetException && e.getCause() != null) {
            exceptionMessage = e.getCause().getMessage();
        }
        return exceptionMessage;
    }

    private synchronized Serializer getObjectSerializer() {
        if (serializer == null) {
            synchronized (MythSendMessageServiceImpl.class) {
                if (serializer == null) {
                    serializer = SpringUtil.getBean(Serializer.class);
                }
            }
        }
        return serializer;
    }
}
