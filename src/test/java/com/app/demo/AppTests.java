package com.app.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppTests {

    private static final Logger logger = LogManager.getLogger(AppTests.class);

    public static void main(String[] args) {
        SpringApplication.run(AppTests.class, args);
        logger.info("Application started successfully.");
    }
}