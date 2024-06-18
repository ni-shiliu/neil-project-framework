package com.neil.project.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author nihao
 * @date 2024/6/18
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.neil.project.order"})
public class UserApplication {

    public static void main( String[] args ) {
        SpringApplication.run(UserApplication.class, args);
    }
}
