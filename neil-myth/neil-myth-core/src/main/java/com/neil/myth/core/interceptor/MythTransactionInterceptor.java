package com.neil.myth.core.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author nihao
 * @date 2024/6/11
 */
public interface MythTransactionInterceptor {

    Object interceptor(ProceedingJoinPoint pjp) throws Throwable;


}
