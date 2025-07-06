package com.app.api.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2 {
	public static Logger getLogger(Class<?> clazz) {
		return LogManager.getLogger(clazz.getName());
	}

	public String info;

	public void info(String string) {
		// TODO Auto-generated method stub
		
	}
}