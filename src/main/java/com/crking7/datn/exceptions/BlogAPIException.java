package com.crking7.datn.exceptions;

import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException {

	private HttpStatus status;
	
	private String message;

	public BlogAPIException(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public BlogAPIException(String message, HttpStatus status, String message1) {
		super(message);
		this.status = status;
		message = message1;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
	
}
