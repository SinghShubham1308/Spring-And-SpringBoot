package com.emailfollowup.email_followup_app.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author SinghShubham1308
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String TIMESTAMP = "timestamp";
	private static final String STATUS = "status";

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put(TIMESTAMP, LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put(STATUS, HttpStatus.NOT_FOUND.value());
		body.put("error", "Not Found");

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		Map<String, Object> body = new HashMap<>();
		body.put(TIMESTAMP, LocalDateTime.now());
		body.put(STATUS, HttpStatus.BAD_REQUEST.value());
		body.put("errors", errors);

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	// 2. Koi bhi aur Anjaan Error (NullPointer, Database down, etc.)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGeneralException(Exception ex) {
		Map<String, Object> body = new HashMap<>();
		body.put(TIMESTAMP, LocalDateTime.now());
		body.put("message", "An unexpected error occurred: " + ex.getMessage());
		body.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
