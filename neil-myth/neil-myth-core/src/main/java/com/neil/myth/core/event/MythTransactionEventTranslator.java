package com.neil.myth.core.event;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.enums.EventTypeEnum;

/**
 * @author nihao
 * @date 2024/6/11
 */
public class MythTransactionEventTranslator implements EventTranslatorOneArg<MythTransactionEvent, MythTransaction> {

    private EventTypeEnum type;

    public MythTransactionEventTranslator(EventTypeEnum type) {
        this.type = type;
    }

    @Override
    public void translateTo(MythTransactionEvent mythTransactionEvent, long l, MythTransaction mythTransaction) {
        mythTransactionEvent.setMythTransaction(mythTransaction);
        mythTransactionEvent.setType(type);
    }
}
