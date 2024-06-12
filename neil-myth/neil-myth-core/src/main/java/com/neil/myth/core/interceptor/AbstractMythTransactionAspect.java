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

    @Pointcut("@annotation(com.neil.myth.annotation.MythProducer)")
    public void mythProducerInterceptor() {

    }

    @Pointcut("@annotation(com.neil.myth.annotation.MythConsumer)")
    public void mythConsumerInterceptor() {

    }

    @Pointcut("mythProducerInterceptor() || mythConsumerInterceptor()")
    public void mythTransactionInterceptor() {

    }

    @Around("mythTransactionInterceptor()")
    public Object interceptMythAnnotationMethod(ProceedingJoinPoint proceedingJoinPoint) throws  Throwable {
        return mythTransactionInterceptor.interceptor(proceedingJoinPoint);
    }

}
