package com.neil.project.config;

import jakarta.annotation.Resource;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nihao
 * @date 2024/9/26
 */
@Configuration
public class RateLimiterConfig {

    @Resource
    private RedissonClient redissonClient;


    @Bean
    public RRateLimiter rateLimiter() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("my-limiter");
        // 每分钟5个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.MINUTES);
        return rateLimiter;
    }
}
