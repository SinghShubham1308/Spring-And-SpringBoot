package com.flowstream.orders.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.flowstream.orders.util.ConstantsUtil;

/**
 * @author SinghShubham1308
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<Map<String, Object>> handleStockException(InsufficientStockException exception) {
		Map<String, Object> response = new HashMap<>();
		response.put(ConstantsUtil.TIMESTAMP, LocalDateTime.now());
		response.put(ConstantsUtil.STATUS, HttpStatus.BAD_REQUEST.value());
		response.put(ConstantsUtil.ERROR, "Stock Insufficient");
		response.put(ConstantsUtil.MESSAGE, exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException exception) {
		Map<String, Object> response = new HashMap<>();
		response.put(ConstantsUtil.TIMESTAMP, LocalDateTime.now());
		response.put(ConstantsUtil.STATUS, HttpStatus.NOT_FOUND.value());
		response.put(ConstantsUtil.ERROR, "NOT FOUND");
		response.put(ConstantsUtil.MESSAGE, exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
		Map<String, Object> response = new HashMap<>();
		response.put(ConstantsUtil.TIMESTAMP, LocalDateTime.now());
		response.put(ConstantsUtil.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.put(ConstantsUtil.ERROR, "Internal Server Error");
		response.put(ConstantsUtil.MESSAGE, ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
