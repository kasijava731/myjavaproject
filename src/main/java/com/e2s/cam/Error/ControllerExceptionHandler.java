package com.e2s.cam.Error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<ErrorMessage> unAuthorizedException(UnAuthorizedException ex, WebRequest request) {
		List<String> schemas=new ArrayList<>();
		schemas.add("urn:ietf:params:scim:api:messages:2.0:Error");
			ErrorMessage message = new ErrorMessage(schemas,ex.getMessage(),HttpStatus.UNAUTHORIZED.value());

		return new ResponseEntity<ErrorMessage>(message, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorMessage> userNotFoundException(UserNotFoundException ex, WebRequest request) {
		List<String> schemas=new ArrayList<>();
		schemas.add("urn:ietf:params:scim:api:messages:2.0:Error");
			ErrorMessage message = new ErrorMessage(schemas,ex.getMessage(),HttpStatus.NOT_FOUND.value());

		return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> exception(Exception ex, WebRequest request) {
		List<String> schemas=new ArrayList<>();
		schemas.add("urn:ietf:params:scim:api:messages:2.0:Error");
			ErrorMessage message = new ErrorMessage(schemas,"Unexpected condition encountered while processing your request. Please try again later or contact support.",HttpStatus.INTERNAL_SERVER_ERROR.value());

		return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}