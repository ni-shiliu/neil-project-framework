package com.neil.myth.core.interceptor;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Aspect
@Component
@RequiredArgsConstructor
public class MythTransactionAspect {

    private final MythTransactionInterceptor mythTransactionInterceptor;

    /**
     * 事务开启者
     */
    @Pointcut("@annotation(com.neil.myth.annotation.Myth)")
    public void mythInterceptor() {

    }

    /**
     * 事务producer
     */
    @Pointcut("@annotation(com.neil.myth.annotation.MythProducer)")
    public void mythProducerInterceptor() {

    }

    /**
     * 事务consumer
     */
    @Pointcut("@annotation(com.neil.myth.annotation.MythConsumer)")
    public void mythConsumerInterceptor() {

    }

    @Around("mythInterceptor() || mythConsumerInterceptor() || mythProducerInterceptor()")
    public Object interceptMythAnnotationMethod(ProceedingJoinPoint proceedingJoinPoint) throws  Throwable {
        return mythTransactionInterceptor.interceptor(proceedingJoinPoint);
    }

}
