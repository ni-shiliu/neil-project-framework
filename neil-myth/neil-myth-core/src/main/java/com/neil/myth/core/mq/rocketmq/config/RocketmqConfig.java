package com.neil.myth.core.mq.rocketmq.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nihao
 * @date 2024/6/12
 */
@Configuration
@ConditionalOnProperty(prefix = "neil.myth.mq.rocketmq", name = "producer-enabled", havingValue = "true")
public class RocketmqConfig {

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Value("${neil.myth.mq.rocketmq.producer-group}")
    private String producerGroup;

    @Bean
    public RocketMQTemplate rocketMQTemplate() {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(producerGroup);
        defaultMQProducer.setNamesrvAddr(nameServer);
        rocketMQTemplate.setProducer(defaultMQProducer);
        return rocketMQTemplate;
    }

}
