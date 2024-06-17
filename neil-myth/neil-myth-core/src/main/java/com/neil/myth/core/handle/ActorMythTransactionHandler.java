package com.neil.myth.core.handle;

import com.neil.myth.common.bean.context.MythTransactionContext;
import com.neil.myth.common.bean.threadlocal.TransactionContextLocal;
import com.neil.myth.common.enums.MythStatusEnum;
import com.neil.myth.core.service.impl.MythTransactionEngine;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Service
@RequiredArgsConstructor
public class ActorMythTransactionHandler implements MythTransactionHandler {

    private final MythTransactionEngine mythTransactionEngine;

    @Override
    public Object handler(ProceedingJoinPoint point, MythTransactionContext mythTransactionContext) throws Throwable {
        try {
            mythTransactionEngine.actorTransaction(point, mythTransactionContext);
            final Object proceed = point.proceed();
            mythTransactionEngine.updateStatus(MythStatusEnum.COMMIT);
            return proceed;
        } catch (Throwable throwable) {
            mythTransactionEngine.failTransaction(throwable.getMessage());
            throw throwable;
        } finally {
            TransactionContextLocal.getInstance().remove();
        }
    }
}
