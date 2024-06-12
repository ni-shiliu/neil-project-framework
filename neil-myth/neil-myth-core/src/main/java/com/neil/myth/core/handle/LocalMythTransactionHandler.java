package com.neil.myth.core.handle;

import com.neil.myth.common.bean.context.MythTransactionContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Service
@RequiredArgsConstructor
public class LocalMythTransactionHandler implements MythTransactionHandler {
    @Override
    public Object handler(ProceedingJoinPoint point, MythTransactionContext mythTransactionContext) throws Throwable {
        return null;
    }
}
