package com.neil.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author nihao
 * @date 2024/6/18
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.neil.project.order"})
@ComponentScan(basePackages = {"com.neil.myth", "com.neil"})
public class UserApplication {

    public static void main( String[] args ) {
        SpringApplication.run(UserApplication.class, args);
    }
}
