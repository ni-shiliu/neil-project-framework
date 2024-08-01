package com.neil.project;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.neil.project.user", "com.neil.project.goods"})
@ComponentScan(basePackages = {"com.neil.myth", "com.neil"})
public class OrderApplication {

    public static void main( String[] args ) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
