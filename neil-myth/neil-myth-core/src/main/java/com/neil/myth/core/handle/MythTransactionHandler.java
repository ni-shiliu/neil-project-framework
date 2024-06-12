package com.neil.myth.core.handle;

import com.neil.myth.common.bean.context.MythTransactionContext;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author nihao
 * @date 2024/6/11
 */
public interface MythTransactionHandler {
    Object handler(ProceedingJoinPoint point, MythTransactionContext mythTransactionContext) throws Throwable;
}
