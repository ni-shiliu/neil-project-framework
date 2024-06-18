package com.neil.myth.common.config;

import lombok.Data;

/**
 * @author nihao
 * @date 2024/6/7
 */
@Data
public class MythConfig {

    private String repositorySuffix;


    /**
     * 序列化框架
     */
    private String serializer = "kryo";

    /**
     * 数据存储类型，db
     */
    private String repositorySupport = "db";

    private MythDbConfig mythDbConfig;

    private int bufferSize = 4096;

    private int retryMax = 3;

    private int consumerThreads = Runtime.getRuntime().availableProcessors() << 1;

    private Mq mq;

    @Data
    static class Mq {
        private Rocketmq rocketmq;
    }

    @Data
    static class Rocketmq {
        private Boolean enabled;
    }

}
