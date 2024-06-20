package com.neil.myth.core.mq.rocketmq.service;

import cn.hutool.json.JSONUtil;
import com.neil.myth.core.service.MythMqReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
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
@ConditionalOnProperty(prefix = "neil.myth.mq.rocketmq", name = "consumer-enabled", havingValue = "true")
@RocketMQMessageListener(topic = "${neil.myth.mq.rocketmq.consumer-topic}",
        consumerGroup = "${neil.myth.mq.rocketmq.consumer-group}",
        selectorType = SelectorType.TAG,
        selectorExpression = "goods"
)
public class RocketmqConsumerServiceImpl implements RocketMQListener<MessageExt> {

    @Resource
    private MythMqReceiveService mythMqReceiveService;

    @Override
    public void onMessage(MessageExt messageExt) {
        log.debug("rocketmq receive message : {}", JSONUtil.toJsonStr(messageExt));
        byte[] body = messageExt.getBody();
        mythMqReceiveService.processMessage(body);
    }

}
