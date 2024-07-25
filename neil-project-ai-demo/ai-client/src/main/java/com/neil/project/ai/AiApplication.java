package com.neil.project.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author nihao
 * @date 2024/7/25
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.neil.project.user", "com.neil.project.goods"})
public class AiApplication {

    public static void main( String[] args ) {
        SpringApplication.run(AiApplication.class, args);
    }

}
