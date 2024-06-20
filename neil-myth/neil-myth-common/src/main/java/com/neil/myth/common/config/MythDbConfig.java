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
@ConfigurationProperties(prefix = "neil.myth.myth-db-config")
public class MythDbConfig {

    private String driverClassName = "com.mysql.jdbc.Driver";

    private String url;

    private String username;

    private String password;

    private int initialSize = 10;

    private int maxActive = 100;

    private int minIdle = 20;

    private int maxWait = 60000;

    private int timeBetweenEvictionRunsMillis = 60000;

    /**
     * To configure a connection in the pool minimum survival time, unit is milliseconds.
     */
    private int minEvictableIdleTimeMillis = 300000;

    private String validationQuery = " SELECT 1 ";

    /**
     * Apply for connection to perform validation Query test connection is valid, do this configuration will degrade performance.
     */
    private Boolean testOnBorrow = false;

    /**
     * Return connection to perform validation Query test connection is valid, do this configuration will degrade performance.
     */
    private Boolean testOnReturn = false;

    /**
     * Recommendations to true, do not affect performance,
     * and ensure safety. Application connection testing,
     * if free time is greater than the time Between Eviction Runs Millis,
     * perform the validation Query test connection is valid..
     */
    private Boolean testWhileIdle = true;

    /**
     * Whether the cache prepared Statement,
     * namely PSCache. PSCache cursor database to support huge performance improvements,
     * such as oracle. Under the mysql suggested that shut down.
     */
    private Boolean poolPreparedStatements = false;

    /**
     * To enable the PSCache, you must configure greater than zero,
     * when greater than 0,
     * the pool Prepared Statements trigger automatically modified to true.
     * In the Druid, not Oracle PSCache under too much memory problems,
     * can put this value configuration, such as 100.
     */
    private int maxPoolPreparedStatementPerConnectionSize = 100;
}
