package com.neil.project.config;

import com.neil.project.exception.BizExceptionDecoder;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author nihao
 * @date 2024/5/22
 */
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    ErrorDecoder bizExceptionDecoder () {
        return new BizExceptionDecoder();
    }
}
