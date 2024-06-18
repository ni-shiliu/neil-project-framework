package com.neil.myth;

import com.neil.myth.core.bootstrap.MythBootstrap;
import com.neil.myth.core.service.MythInitService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Myth spring-boot-starter
 *
 * @author nihao
 * @date 2024/6/7
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties
@ComponentScan(basePackages = {"com.neil.myth"})
@RequiredArgsConstructor
public class MythAutoConfiguration {

    private final MythConfigProperties mythConfigProperties;

    @Bean
    @ConditionalOnProperty(prefix = "neil.myth", name = "enabled", havingValue = "true")
    public MythBootstrap mythBootstrap(MythInitService mythInitService) {
        return new MythBootstrap(mythInitService, this.mythConfigProperties);
    }

}