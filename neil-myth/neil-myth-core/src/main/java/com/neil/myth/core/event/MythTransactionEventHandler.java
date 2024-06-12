package com.neil.myth.core.event;

import com.lmax.disruptor.WorkHandler;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.enums.EventTypeEnum;
import com.neil.myth.core.service.MythCoordinatorService;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executor;

/**
 * @author nihao
 * @date 2024/6/11
 */
@RequiredArgsConstructor
public class MythTransactionEventHandler implements WorkHandler<MythTransactionEvent> {

    private final MythCoordinatorService mythCoordinatorService;

    private final Executor executor;

    @Override
    public void onEvent(MythTransactionEvent mythTransactionEvent) throws Exception {
        executor.execute(() -> {
            EventTypeEnum type = mythTransactionEvent.getType();
            switch (type) {
                case SAVE:
                    mythCoordinatorService.save(mythTransactionEvent.getMythTransaction());
                    break;
                case UPDATE_FAIR:
                    mythCoordinatorService.updateFailTransaction(mythTransactionEvent.getMythTransaction());
                    break;
                case UPDATE_STATUS:
                    MythTransaction mythTransaction = mythTransactionEvent.getMythTransaction();
                    mythCoordinatorService.updateStatus(mythTransaction.getTransId(), mythTransaction.getStatus());
                    break;
                case UPDATE_PARTICIPANT:
                    mythCoordinatorService.updateParticipant(mythTransactionEvent.getMythTransaction());
                    break;
                default:
                    break;
            }
            mythTransactionEvent.clear();
        });
    }
}
