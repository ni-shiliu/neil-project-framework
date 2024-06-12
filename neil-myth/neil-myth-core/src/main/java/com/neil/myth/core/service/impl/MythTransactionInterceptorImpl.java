package com.neil.myth.core.service.impl;

import com.neil.myth.common.bean.context.MythTransactionContext;
import com.neil.myth.core.interceptor.MythTransactionInterceptor;
import com.neil.myth.core.mediator.RpcMediator;
import com.neil.myth.core.service.MythTransactionAspectService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Service
@RequiredArgsConstructor
public class MythTransactionInterceptorImpl implements MythTransactionInterceptor {


    private final MythTransactionAspectService mythTransactionAspectService;

    @Override
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        MythTransactionContext mythTransactionContext = RpcMediator.getInstance().acquire(request::getHeader);
        return mythTransactionAspectService.invoke(mythTransactionContext, pjp);
    }

}
