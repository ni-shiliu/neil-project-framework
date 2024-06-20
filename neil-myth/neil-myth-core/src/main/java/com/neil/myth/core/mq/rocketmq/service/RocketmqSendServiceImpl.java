package com.neil.myth.core.mq.rocketmq.service;

import cn.hutool.json.JSONUtil;
import com.neil.myth.common.bean.mq.MessageEntity;
import com.neil.myth.common.exception.MythException;
import com.neil.myth.core.mq.service.MythMqSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author nihao
 * @date 2024/6/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "neil.myth.mq.rocketmq", name = "producer-enabled", havingValue = "true")
public class RocketmqSendServiceImpl implements MythMqSendService {

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendMessage(String destination, MessageEntity message) {
        try {
            log.info("myth rocketMq send destination: {}, message: {}", destination, JSONUtil.toJsonStr(message));
            SendResult sendResult = rocketMQTemplate.syncSend(destination, JSONUtil.toJsonStr(message));
            log.info("myth rocketMq send result: {}", JSONUtil.toJsonStr(sendResult));
        } catch (Exception e) {
            log.error("myth rocketMq send result error: {}", e.getMessage());
            throw new MythException("myth rocketMq send result error");
        }
    }

}
