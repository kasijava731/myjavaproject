package com.e2s.cam.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
	
	private static final Logger logger = LogManager.getLogger(ErrorResponse.class);
	
	HttpStatus httpStatus;
	ErrorResponseMessage message;
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	public ErrorResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ErrorResponseMessage message) {
		this.message = message;
	}
	
	public ErrorResponse(HttpStatus httpStatus, ErrorResponseMessage message) {
		this.httpStatus = httpStatus;
		this.message = message;
		logger.error(message);
	}
	
}
