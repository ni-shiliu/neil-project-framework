package com.neil.myth.core.service.impl;

import com.neil.myth.common.bean.context.MythTransactionContext;
import com.neil.myth.common.utils.SpringBeanUtil;
import com.neil.myth.core.handle.MythTransactionHandler;
import com.neil.myth.core.service.MythTransactionAspectService;
import com.neil.myth.core.service.MythTransactionFactoryService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Service
@RequiredArgsConstructor
public class MythTransactionAspectServiceImpl implements MythTransactionAspectService {


    private final MythTransactionFactoryService mythTransactionFactoryService;


    @Override
    public Object invoke(MythTransactionContext mythTransactionContext, ProceedingJoinPoint point) throws Throwable {
        Class clazz = mythTransactionFactoryService.factoryOf(mythTransactionContext);
        MythTransactionHandler mythTransactionHandler = (MythTransactionHandler) SpringBeanUtil.getInstance().getBean(clazz);
        return mythTransactionHandler.handler(point, mythTransactionContext);
    }

}
