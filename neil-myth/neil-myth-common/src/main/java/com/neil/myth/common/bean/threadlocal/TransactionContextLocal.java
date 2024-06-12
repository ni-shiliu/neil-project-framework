package com.neil.myth.common.bean.threadlocal;

import com.neil.myth.common.bean.context.MythTransactionContext;

/**
 * @author nihao
 * @date 2024/6/12
 */
public class TransactionContextLocal {

    private static final ThreadLocal<MythTransactionContext> CURRENT_LOCAL = new ThreadLocal<>();

    private static final TransactionContextLocal TRANSACTION_CONTEXT_LOCAL = new TransactionContextLocal();

    private TransactionContextLocal() {
    }

    public static TransactionContextLocal getInstance() {
        return TRANSACTION_CONTEXT_LOCAL;
    }

    public void set(final MythTransactionContext context) {
        CURRENT_LOCAL.set(context);
    }

    public MythTransactionContext get() {
        return CURRENT_LOCAL.get();
    }

    public void remove() {
        CURRENT_LOCAL.remove();
    }

}
