package com.app.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        logger.info("Application started successfully.");
    }
}