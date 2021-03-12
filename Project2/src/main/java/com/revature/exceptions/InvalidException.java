package com.revature.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class InvalidException extends RuntimeException {
	private static final Logger log = LoggerFactory.getLogger(InvalidException.class);
	
	public InvalidException() {
		MDC.put(getLocalizedMessage(), getCause().toString());
	}

	public InvalidException(String message) {
		super(message);
		MDC.put("Exception", "Invalid");
		
		if (MDC.get("Start") != null) {
			String start = MDC.get("Start").toString();
			long now = System.currentTimeMillis();
			MDC.put("Duration", String.format("%dms",	now - Long.parseLong(start)));
		}
		
		// Remove 'this' in order to leave out stack trace in logs
		log.error(message, this);
		//log.info("End of stackTrace.");
		MDC.clear();
	}
}
