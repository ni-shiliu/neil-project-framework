package com.neil.myth.core.service.impl;

import com.neil.myth.common.bean.context.MythTransactionContext;
import com.neil.myth.core.handle.ActorMythTransactionHandler;
import com.neil.myth.core.handle.ParticipantMythTransactionHandler;
import com.neil.myth.core.handle.StartMythTransactionHandler;
import com.neil.myth.core.service.MythTransactionFactoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Service
@RequiredArgsConstructor
public class MythTransactionFactoryServiceImpl implements MythTransactionFactoryService {

    private final MythTransactionEngine mythTransactionEngine;

    @Override
    public Class factoryOf(MythTransactionContext context) throws Throwable {
        if (Objects.isNull(context)) {
            return mythTransactionEngine.isBegin()
                    ? ParticipantMythTransactionHandler.class
                    : StartMythTransactionHandler.class;
        } else {
            return ActorMythTransactionHandler.class;
        }
    }
}
