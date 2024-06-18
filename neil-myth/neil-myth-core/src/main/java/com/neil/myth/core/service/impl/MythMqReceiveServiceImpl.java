package com.neil.myth.core.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.neil.myth.common.bean.context.MythTransactionContext;
import com.neil.myth.common.bean.entity.MythInvocation;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.bean.mq.MessageEntity;
import com.neil.myth.common.bean.threadlocal.TransactionContextLocal;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.common.enums.EventTypeEnum;
import com.neil.myth.common.enums.MythRoleEnum;
import com.neil.myth.common.enums.MythStatusEnum;
import com.neil.myth.common.exception.MythException;
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

    private static final Lock LOCK = new ReentrantLock();

    @Override
    public Boolean processMessage(MessageEntity messageEntity) {
        LOCK.lock();
        MythTransaction mythTransaction = mythCoordinatorService.findByTransId(messageEntity.getTransId());
        try {
            if (Objects.isNull(mythTransaction)) {
                return false;
            }

            if (!MythStatusEnum.FAILURE.equals(mythTransaction.getStatus())) {
                return true;
            }

            execute(messageEntity);
            //执行成功 更新日志为成功
            mythTransaction.setStatus(MythStatusEnum.COMMIT);
            publisher.publishEvent(mythTransaction, EventTypeEnum.UPDATE_STATUS);
        } catch (Exception e) {
            mythTransaction.setErrorMsg(getExceptionMessage(e));
            publisher.publishEvent(mythTransaction, EventTypeEnum.UPDATE_FAIR);
            throw new MythException(e);
        } finally {
            TransactionContextLocal.getInstance().remove();
            LOCK.unlock();
        }
        return true;
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

        final Class clazz = mythInvocation.getTargetClass();
        final String method = mythInvocation.getMethodName();
        final Object[] args = mythInvocation.getArgs();
        final Class[] parameterTypes = mythInvocation.getParameterTypes();
        final Object bean = SpringUtil.getBean(clazz);
        MethodUtils.invokeMethod(bean, method, args, parameterTypes);
    }

    private String getExceptionMessage(Throwable e) {
        String exceptionMessage = e.getMessage();
        if (exceptionMessage == null && e instanceof InvocationTargetException && e.getCause() != null) {
            exceptionMessage = e.getCause().getMessage();
        }
        return exceptionMessage;
    }

}
