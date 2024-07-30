package com.neil.project.config;

import com.neil.project.compont.RequestAndResponseLoggingFilter;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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
