package com.example.demo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHelper extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> userNotFoundException(UserNotFoundException unfe) {
		logger.error("USER NOT FOUND: " +unfe.getMessage());
		return new ResponseEntity<Object>(unfe.getMessage(), HttpStatus.NOT_FOUND);
	}

}
