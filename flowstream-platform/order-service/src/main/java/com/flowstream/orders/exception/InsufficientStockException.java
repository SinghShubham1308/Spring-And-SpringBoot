package com.flowstream.orders.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author SinghShubham1308
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientStockException(String message) {
		super(message);
	}
}
