package com.revature.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class NoValidSessionException extends RuntimeException {
	
	private static final Logger log = LoggerFactory.getLogger(InvalidException.class);
	
	public NoValidSessionException() {
		MDC.put("Exception", "NoValidSession");
		MDC.clear();
	}

	public NoValidSessionException(String message) {
		super(message);
		MDC.put("Exception", "NoValidSession");
		log.error(message);
		MDC.clear();
	}
}
