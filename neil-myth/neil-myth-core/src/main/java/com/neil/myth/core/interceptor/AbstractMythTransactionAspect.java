package com.neil.myth.core.interceptor;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Aspect
@RequiredArgsConstructor
public abstract class AbstractMythTransactionAspect {

    private final MythTransactionInterceptor mythTransactionInterceptor;

    /**
     * 既是生产者也是消费者
     */
    @Pointcut("@annotation(com.neil.myth.annotation.Myth)")
    public void mythInterceptor() {

    }

    @Around("mythInterceptor()")
    public Object interceptMythAnnotationMethod(ProceedingJoinPoint proceedingJoinPoint) throws  Throwable {
        return mythTransactionInterceptor.interceptor(proceedingJoinPoint);
    }

}
