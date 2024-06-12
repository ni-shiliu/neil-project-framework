package com.neil.myth.rocketmq.service;

import cn.hutool.json.JSONUtil;
import com.neil.myth.common.bean.mq.MessageEntity;
import com.neil.myth.common.exception.MythException;
import com.neil.myth.core.service.MythMqSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author nihao
 * @date 2024/6/12
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "rocketmq", name = "enabled", havingValue = "true")
public class RocketmqSendServiceImpl implements MythMqSendService {

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendMessage(String destination, MessageEntity message) {
        try {
            SendResult sendResult = rocketMQTemplate.syncSend(destination, message);
            log.info("myth rocketMq send result: {}", JSONUtil.toJsonStr(sendResult));
        } catch (Exception e) {
            log.error("myth rocketMq send result error: {}", e.getMessage());
            throw new MythException("myth rocketMq send result error");
        }
    }

}
