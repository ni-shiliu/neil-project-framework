package com.neil.myth.core.service;

import com.neil.myth.common.bean.mq.MessageEntity;

/**
 * @author nihao
 * @date 2024/6/12
 */
@FunctionalInterface
public interface MythMqReceiveService {

    Boolean processMessage(MessageEntity messageEntity);

}
