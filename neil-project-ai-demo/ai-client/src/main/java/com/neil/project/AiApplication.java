package com.neil.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author nihao
 * @date 2024/7/25
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.neil.project.ai"})
public class AiApplication {

    public static void main( String[] args ) {
        SpringApplication.run(AiApplication.class, args);
    }

}
