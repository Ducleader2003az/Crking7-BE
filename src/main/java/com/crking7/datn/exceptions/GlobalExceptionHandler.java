package com.crking7.datn.exceptions;

import com.crking7.datn.models.dtos.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	//Xử lý lỗi cụ thể
	@ExceptionHandler(ResourceNotFoundException.class)
	private ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
																			WebRequest webRequest){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(BlogAPIException.class)
	private ResponseEntity<ErrorDetails> handleBlogAPIException(BlogAPIException exception,
																			WebRequest webRequest){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	//Lỗi chung
	@ExceptionHandler(Exception.class)
	private ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
																			WebRequest webRequest){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
