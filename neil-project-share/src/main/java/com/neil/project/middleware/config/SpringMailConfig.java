package com.neil.project.middleware.config;

import com.neil.project.middleware.service.SpringMailService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author nihao
 * @date 2024/5/8
 *
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.mail", value = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "spring.mail")
@Data
@RequiredArgsConstructor
public class SpringMailConfig {

    private final JavaMailSender javaMailSender;

    @Bean
    public SpringMailService springMailService() {
        return new SpringMailService(javaMailSender);
    }
}
