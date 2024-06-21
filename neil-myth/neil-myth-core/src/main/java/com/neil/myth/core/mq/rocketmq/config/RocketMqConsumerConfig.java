package com.neil.myth.core.mq.rocketmq.config;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.core.service.MythMqReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nihao
 * @date 2024/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "neil.myth.mq.rocketmq", name = "consumer-enabled", havingValue = "true")
public class RocketMqConsumerConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final MythMqReceiveService mythMqReceiveService;

    private final MythConfig mythConfig;

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        startMythConsumer();
    }

    private void startMythConsumer() {
        MythConfig.Rocketmq rocketmq = mythConfig.getMq().getRocketmq();

        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer(rocketmq.getConsumerGroup());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumeMessageBatchMaxSize(mythConfig.getConsumeMessageBatchMaxSize());
        consumer.setMaxReconsumeTimes(mythConfig.getRetryMax());
        /**
         * 订阅指定topic下tags
         */
        try {
            consumer.subscribe(rocketmq.getConsumerTopic(), rocketmq.getConsumerTag());
            consumer.registerMessageListener((List<MessageExt> msgList, ConsumeConcurrentlyContext context) -> {
                MessageExt msg = msgList.get(0);
                try {
                    log.info("rocketmq receive message : {}", JSONUtil.toJsonStr(msg));
                    final byte[] message = msg.getBody();
                    mythMqReceiveService.processMessage(message);
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        SpringUtil.registerBean(consumer.getClass().getName(), consumer);
    }

}
