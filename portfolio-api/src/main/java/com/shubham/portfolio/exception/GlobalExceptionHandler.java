package com.shubham.portfolio.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.shubham.portfolio.dto.ErrorDetails;


/**
 * @author SinghShubham
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handleResourceNotFoundException(Exception exception, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
}
