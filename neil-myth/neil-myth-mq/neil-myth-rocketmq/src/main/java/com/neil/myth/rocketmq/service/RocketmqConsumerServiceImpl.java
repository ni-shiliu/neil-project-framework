package com.neil.myth.rocketmq.service;

import com.neil.myth.common.bean.mq.MessageEntity;
import com.neil.myth.core.service.MythMqReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author nihao
 * @date 2024/6/12
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "neil.myth.mq.rocketmq.consumer", name = "enabled", havingValue = "true")
@RocketMQMessageListener(topic = "${rocketmq.consumer.myth.topic}",
        consumerGroup = "${rocketmq.consumer.myth.group}",
        selectorType = SelectorType.TAG,
        selectorExpression = "${rocketmq.consumer.myth.tag}",
        maxReconsumeTimes = 3
)
public class RocketmqConsumerServiceImpl implements RocketMQListener<MessageEntity> {

    @Resource
    private MythMqReceiveService mythMqReceiveService;

    @Override
    public void onMessage(MessageEntity message) {
        mythMqReceiveService.processMessage(message);
    }

}
