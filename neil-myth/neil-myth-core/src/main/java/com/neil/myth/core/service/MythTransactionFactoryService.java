package com.neil.myth.core.service;

import com.neil.myth.common.bean.context.MythTransactionContext;

/**
 * @author nihao
 * @date 2024/6/11
 */
public interface MythTransactionFactoryService<T> {

    Class<T> factoryOf(MythTransactionContext context) throws Throwable;

}
