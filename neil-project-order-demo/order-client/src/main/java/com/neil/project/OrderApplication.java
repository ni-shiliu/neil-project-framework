package com.neil.project;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.neil.project.user", "com.neil.project.goods"})
public class OrderApplication {

    public static void main( String[] args ) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
