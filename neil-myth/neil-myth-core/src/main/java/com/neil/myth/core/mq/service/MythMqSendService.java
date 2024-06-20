package com.neil.myth.core.mq.service;

import com.neil.myth.common.bean.mq.MessageEntity;

/**
 * @author nihao
 * @date 2024/6/12
 */
@FunctionalInterface
public interface MythMqSendService {
    void sendMessage(String destination, MessageEntity message);
}
