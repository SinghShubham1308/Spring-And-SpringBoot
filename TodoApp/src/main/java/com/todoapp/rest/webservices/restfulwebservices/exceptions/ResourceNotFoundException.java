package com.todoapp.rest.webservices.restfulwebservices.exceptions;

/**
 * @author SinghShubham
 */
//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1576491217010681049L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
