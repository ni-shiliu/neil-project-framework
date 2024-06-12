package com.neil.myth.core.mediator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.neil.myth.common.bean.context.MythTransactionContext;
import com.neil.myth.common.constant.CommonConstant;

import java.util.Objects;

/**
 * @author nihao
 * @date 2024/6/11
 */
public class RpcMediator {

    private static final RpcMediator RPC_MEDIATOR = new RpcMediator();

    public static RpcMediator getInstance() {
        return RPC_MEDIATOR;
    }

    public void transmit(RpcTransmit rpcTransmit, MythTransactionContext context) {
        if (Objects.nonNull(context)) {
            rpcTransmit.transmit(CommonConstant.MYTH_TRANSACTION_CONTEXT, JSONUtil.toJsonStr(context));
        }
    }

    public MythTransactionContext acquire(RpcAcquire rpcAcquire) {
        MythTransactionContext mythTransactionContext = null;
        String context = rpcAcquire.acquire(CommonConstant.MYTH_TRANSACTION_CONTEXT);
        if (StrUtil.isNotBlank(context)) {
            mythTransactionContext = JSONUtil.toBean(context, MythTransactionContext.class);
        }
        return mythTransactionContext;
    }
}
