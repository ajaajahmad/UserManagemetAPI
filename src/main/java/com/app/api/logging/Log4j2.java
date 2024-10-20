package com.app.api.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2 {
    private static final Logger logger = LogManager.getLogger(Log4j2.class);

    public void yourMethod() {
        logger.info("This is an info message");
        logger.error("This is an error message");
    }
}