package com.neil.myth.core.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.neil.myth.annotation.Myth;
import com.neil.myth.common.bean.context.MythTransactionContext;
import com.neil.myth.common.bean.entity.MythInvocation;
import com.neil.myth.common.bean.entity.MythParticipant;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.bean.threadlocal.TransactionContextLocal;
import com.neil.myth.common.enums.EventTypeEnum;
import com.neil.myth.common.enums.MythRoleEnum;
import com.neil.myth.common.enums.MythStatusEnum;
import com.neil.myth.core.event.MythTransactionEventPublisher;
import com.neil.myth.core.mq.service.MythSendMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MythTransactionEngine {

    private static final ThreadLocal<MythTransaction> CURRENT = new ThreadLocal<>();

    private final MythTransactionEventPublisher publishEvent;

    private final MythSendMessageService mythSendMessageService;

    public boolean isBegin() {
        return null != CURRENT.get();
    }

    public void begin(ProceedingJoinPoint point) {
        log.debug("start myth 分布式事务");
        MythTransaction mythTransaction = generateMythTransaction(point, MythRoleEnum.START, MythStatusEnum.BEGIN, "");
        publishEvent.publishEvent(mythTransaction, EventTypeEnum.SAVE);
        CURRENT.set(mythTransaction);
        MythTransactionContext context = new MythTransactionContext();
        context.setTransId(mythTransaction.getTransId());
        context.setMythRole(MythRoleEnum.START);
        TransactionContextLocal.getInstance().set(context);
    }

    private MythTransaction generateMythTransaction(ProceedingJoinPoint point,
                                                    MythRoleEnum roleEnum,
                                                    MythStatusEnum mythStatusEnum,
                                                    String transId) {
        MythTransaction mythTransaction = StrUtil.isBlank(transId)
                ? new MythTransaction()
                : new MythTransaction(transId);
        Object[] args = point.getArgs();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();
        mythTransaction.setStatus(mythStatusEnum);
        mythTransaction.setRole(roleEnum);
        mythTransaction.setTargetClass(clazz.getName());
        mythTransaction.setTargetMethod(method.getName());
        mythTransaction.setArgs(JSONUtil.toJsonStr(args));
        return mythTransaction;
    }

    public void updateStatus(MythStatusEnum mythStatus) {
        MythTransaction mythTransaction = CURRENT.get();
        if (Objects.isNull(mythTransaction)) {
            return;
        }
        mythTransaction.setStatus(mythStatus);
        publishEvent.publishEvent(mythTransaction, EventTypeEnum.UPDATE_STATUS);
    }

    public void failTransaction(String message) {
        MythTransaction mythTransaction = CURRENT.get();
        if (Objects.isNull(mythTransaction)) {
            return;
        }
        mythTransaction.setStatus(MythStatusEnum.FAILURE);
        mythTransaction.setErrorMsg(message);
        publishEvent.publishEvent(mythTransaction, EventTypeEnum.UPDATE_FAIR);
    }

    public void cleanThreadLocal() {
        CURRENT.remove();
    }

    public void sendMessage() {
        Optional.ofNullable(getCurrentTransaction())
                .ifPresent(mythSendMessageService::sendMessage);
    }

    private MythTransaction getCurrentTransaction() {
        return CURRENT.get();
    }

    public void registerParticipant(ProceedingJoinPoint point, MythStatusEnum status, String message) {
        MythTransaction currentTransaction = getCurrentTransaction();
        if (Objects.isNull(currentTransaction)) {
            return;
        }
        Object[] args = point.getArgs();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Myth myth = method.getAnnotation(Myth.class);
        if (Objects.isNull(myth)) {
            return;
        }

        MythInvocation mythInvocation = new MythInvocation(signature.getClass(), method.getName(), method.getParameterTypes(), args);
        String destination = myth.destination();
        if (Strings.isNotBlank(myth.tags())) {
            destination = destination + ":" + myth.tags();
        }
        currentTransaction.registerParticipant(generateMythParticipant(currentTransaction.getTransId(),
                destination,
                mythInvocation,
                0,
                status.name(),
                message));
        publishEvent.publishEvent(currentTransaction, EventTypeEnum.UPDATE_PARTICIPANT);
    }

    public void actorTransaction(ProceedingJoinPoint point, MythTransactionContext mythTransactionContext) {
        MythTransaction mythTransaction = generateMythTransaction(point, MythRoleEnum.PROVIDER, MythStatusEnum.BEGIN, mythTransactionContext.getTransId());
        publishEvent.publishEvent(mythTransaction, EventTypeEnum.SAVE);
        CURRENT.set(mythTransaction);
        //设置提供者角色
        mythTransactionContext.setMythRole(MythRoleEnum.PROVIDER);
        TransactionContextLocal.getInstance().set(mythTransactionContext);
    }

    private MythParticipant generateMythParticipant(String transId, String destination,
                                                    MythInvocation mythInvocation, Integer retriedCount,
                                                    String status, String message) {
        MythParticipant mythParticipant = new MythParticipant();
        mythParticipant.setParticipantId(IdUtil.fastUUID());
        mythParticipant.setTransId(transId);
        mythParticipant.setDestination(destination);
        mythParticipant.setRetriedCount(retriedCount);
        mythParticipant.setMythInvocation(mythInvocation);
        mythParticipant.setStatus(status);
        mythParticipant.setErrorMsg(message);
        return mythParticipant;
    }
}
