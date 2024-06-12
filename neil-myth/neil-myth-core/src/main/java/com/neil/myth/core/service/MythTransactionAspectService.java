package com.neil.myth.core.service;

import com.neil.myth.common.bean.context.MythTransactionContext;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author nihao
 * @date 2024/6/11
 */
public interface MythTransactionAspectService {

    Object invoke(MythTransactionContext mythTransactionContext, ProceedingJoinPoint point) throws Throwable;

}
