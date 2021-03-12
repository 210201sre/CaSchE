package com.revature.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class UserNotFoundException extends RuntimeException {
	
	private static final Logger log = LoggerFactory.getLogger(InvalidException.class);
	
	public UserNotFoundException() {
	}

	public UserNotFoundException(String message) {
		super(message);
		MDC.put("Exception", "UserNotFound");
		String start = MDC.get("start").toString();
		if (start != null) {
			long now = System.currentTimeMillis();
			MDC.put("Duration", String.format("%dms",	now - Long.parseLong(start)));
		}
		
		log.error(message);
		MDC.clear();
	}
}
