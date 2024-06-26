package com.neil.myth.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author nihao
 * @date 2024/6/7
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "neil.myth")
public class MythConfig {

    private Boolean enabled;

    private String repositorySuffix;


    /**
     * 序列化框架
     */
    private String serializer = "kryo";

    /**
     * 数据存储类型，db / mongodb
     */
    private String repositorySupport = "db";

    private MythDbConfig mythDbConfig;

    private MythMongoDbConfig mythMongoDbConfig;

    private int bufferSize = 4096;

    private int retryMax = 3;

    private int consumeMessageBatchMaxSize = 1;

    private int consumerThreads = Runtime.getRuntime().availableProcessors() << 1;

    private Mq mq;

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "neil.myth.mq")
    public static class Mq {

        private Rocketmq rocketmq;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "neil.myth.mq.rocketmq")
    public static class Rocketmq {

        private Boolean producerEnabled;

        private String producerGroup;

        private Boolean consumerEnabled;

        private String consumerTopic;

        private String consumerGroup;

        private String consumerTag;
    }



}
