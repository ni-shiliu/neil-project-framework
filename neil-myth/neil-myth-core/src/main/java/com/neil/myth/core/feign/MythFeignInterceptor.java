package com.neil.myth.core.feign;

import com.neil.myth.common.bean.context.MythTransactionContext;
import com.neil.myth.common.bean.threadlocal.TransactionContextLocal;
import com.neil.myth.core.mediator.RpcMediator;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * @author nihao
 * @date 2024/6/19
 */
@Configuration
public class MythFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        final MythTransactionContext mythTransactionContext = TransactionContextLocal.getInstance().get();
        RpcMediator.getInstance().transmit(requestTemplate::header, mythTransactionContext);
    }

}
