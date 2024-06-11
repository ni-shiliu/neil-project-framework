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
    private String repositorySupport;

    private MythDbConfig mythDbConfig;

}
