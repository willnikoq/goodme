package com.goodme.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 应用程序启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.goodme"})
@EnableFeignClients(basePackages = {"com.goodme.product.api", "com.goodme.shop.api"})
public class GoodmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodmeApplication.class, args);
    }
} 