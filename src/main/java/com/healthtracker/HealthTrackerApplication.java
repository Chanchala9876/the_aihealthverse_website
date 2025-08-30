package com.healthtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.MongoRepository;

@SpringBootApplication
public class HealthTrackerApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(HealthTrackerApplication.class, args);
        
        // Debug: Print all repository beans
        System.out.println("=== Repository Beans ===");
        context.getBeansOfType(MongoRepository.class).forEach((name, bean) -> {
            System.out.println("Found repository: " + name + " -> " + bean.getClass().getSimpleName());
        });
        System.out.println("=======================");
    }
} 