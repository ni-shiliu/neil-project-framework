package com.neil.myth.core.event;

import com.lmax.disruptor.EventFactory;

/**
 * @author nihao
 * @date 2024/6/11
 */
public class MythTransactionEventFactory implements EventFactory<MythTransactionEvent> {

    @Override
    public MythTransactionEvent newInstance() {
        return new MythTransactionEvent();
    }

}
