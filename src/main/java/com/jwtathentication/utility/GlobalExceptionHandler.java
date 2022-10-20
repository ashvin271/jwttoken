package com.jwtathentication.utility;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jwtathentication.payload.ErrorMessage;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{


	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<Object> handleAnyException(NullPointerException ex, WebRequest  request) {
		//ErrorMessage errorMessage =new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage())
		ex.printStackTrace();
		return new ResponseEntity<>(new ErrorMessage(false, ex.getMessage()),new HttpHeaders(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> handleAnyException(BadCredentialsException ex, WebRequest  request) {
		//ErrorMessage errorMessage =new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage())
		ex.printStackTrace();
		return new ResponseEntity<>(new ErrorMessage(false, ex.getMessage()),new HttpHeaders(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest  request) {
		//ErrorMessage errorMessage =new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage())
		ex.printStackTrace();
		return new ResponseEntity<>(new ErrorMessage(false, ex.getLocalizedMessage()),new HttpHeaders(),HttpStatus.BAD_REQUEST);
	}
}
