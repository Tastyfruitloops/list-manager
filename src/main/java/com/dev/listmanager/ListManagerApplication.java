package com.dev.listmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class, scanBasePackages = {"com.dev.listmanager.service"})
@ComponentScan(basePackages = {"com.dev.listmanager.*"})
public class ListManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListManagerApplication.class, args);
    }

}
