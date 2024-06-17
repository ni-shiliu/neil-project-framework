package com.neil.myth.core.handle;

import com.neil.myth.common.bean.context.MythTransactionContext;
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
public class ParticipantMythTransactionHandler implements MythTransactionHandler {


    private final MythTransactionEngine mythTransactionEngine;

    @Override
    public Object handler(ProceedingJoinPoint point, MythTransactionContext mythTransactionContext) throws Throwable {
        try {
            return point.proceed();
        } catch (Throwable throwable) {
            mythTransactionEngine.addParticipant(point);
        }
        return null;
    }

}
