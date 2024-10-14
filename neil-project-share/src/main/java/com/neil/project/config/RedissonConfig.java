package com.neil.project.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nihao
 * @date 2024/9/18
 */

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "redisson", value = "enabled", havingValue = "true")
public class RedissonConfig {

    @Value("${redisson.address}")
    private String address;


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 集群
//        config.useClusterServers();
        // 哨兵
//        config.useSentinelServers();
        // 单节点
        config.useSingleServer()
                .setAddress(address);

        return Redisson.create(config);
    }

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

}
