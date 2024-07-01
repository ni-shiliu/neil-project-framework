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
import java.util.Objects;

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
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        MythTransactionContext mythTransactionContext;
        if (Objects.isNull(requestAttributes)) {
            // 通过消息反射执行, mock new MythTransactionContext
            mythTransactionContext = new MythTransactionContext();
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            // 获取请求头 MYTH_TRANSACTION_CONTEXT
            mythTransactionContext = RpcMediator.getInstance().acquire(request::getHeader);
        }
        return mythTransactionAspectService.invoke(mythTransactionContext, pjp);
    }

}
