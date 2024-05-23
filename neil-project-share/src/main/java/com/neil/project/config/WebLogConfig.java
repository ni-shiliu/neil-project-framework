package com.neil.project.config;

import com.neil.project.compont.RequestAndResponseLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author nihao
 * @date 2024/5/23
 */
@Configuration
public class WebLogConfig {

    @Bean
    public Filter requestAndResponseLoggingFilter() {
        return new RequestAndResponseLoggingFilter();
    }
}
