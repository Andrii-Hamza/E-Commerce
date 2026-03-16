package com.petproject.ecomnerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);

        System.out.println("==========================================");
        System.out.println("🍽️  Product Service Started!");
        System.out.println("📍 Port: 8050");
        System.out.println("==========================================");
    }

}
