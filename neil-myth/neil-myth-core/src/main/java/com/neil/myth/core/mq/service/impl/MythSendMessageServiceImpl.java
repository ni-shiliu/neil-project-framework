package com.neil.myth.core.mq.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.neil.myth.common.bean.entity.MythParticipant;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.bean.mq.MessageEntity;
import com.neil.myth.common.enums.MythStatusEnum;
import com.neil.myth.common.utils.SpringBeanUtil;
import com.neil.myth.core.mq.service.MythMqSendService;
import com.neil.myth.core.mq.service.MythSendMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author nihao
 * @date 2024/6/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MythSendMessageServiceImpl implements MythSendMessageService {

    private volatile MythMqSendService mythMqSendService;

    @Override
    public Boolean sendMessage(MythTransaction mythTransaction) {
        if (Objects.isNull(mythTransaction)) {
            return false;
        }

        List<MythParticipant> mythParticipants = mythTransaction.getParticipants();
        if (CollUtil.isEmpty(mythParticipants)) {
            return true;
        }

        for (MythParticipant mythParticipant : mythParticipants) {
            if (MythStatusEnum.COMMIT.name().equals(mythParticipant.getStatus())) {
                continue;
            }
            MessageEntity messageEntity = new MessageEntity(mythParticipant.getTransId(), mythParticipant.getMythInvocation());
            try {
                getMythMqSendService().sendMessage(mythParticipant.getDestination(), messageEntity);
            } catch (Exception e) {
                log.error("please check myth.mq config");
                return Boolean.FALSE;
            }
        }
        return true;
    }

    private synchronized MythMqSendService getMythMqSendService() {
        if (mythMqSendService == null) {
            synchronized (MythSendMessageServiceImpl.class) {
                if (mythMqSendService == null) {
                    mythMqSendService = SpringBeanUtil.getInstance().getBean(MythMqSendService.class);
                }
            }
        }
        return mythMqSendService;
    }
}
