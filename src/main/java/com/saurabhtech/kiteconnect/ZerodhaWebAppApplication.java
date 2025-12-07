package com.saurabhtech.kiteconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ZerodhaWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZerodhaWebAppApplication.class, args);
    }
}