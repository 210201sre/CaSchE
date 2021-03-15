package com.revature.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;

public abstract class InvalidException {
	private static final Logger log = LoggerFactory.getLogger(InvalidException.class);
	
	public static ResponseEntity<String> thrown(String message, RuntimeException err) {
		logException(message, err);
		return ResponseEntity.status(400).body(message);
	}
	
	public static ResponseEntity<String> thrown(String message, RuntimeException err, int statusCode) {
		logException(message, err);
		return ResponseEntity.status(statusCode).body(message);
	}
	// Double Check this method
	private static void logException(String message, RuntimeException err) {
		MDC.put("Exception", "Invalid");
		if (MDC.get("Start") != null) {
			String start = MDC.get("Start");
			long now = System.currentTimeMillis();
			MDC.put("Duration", String.format("%dms",	now - Long.parseLong(start)));
		}
		
		log.error(message, err);
		MDC.clear();
	}
}
