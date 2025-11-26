package com.todoapp.rest.webservices.restfulwebservices.exceptions;

/**
 * @author SinghShubham
 */
public class UserAlreadyExistsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1847148100535057950L;

	public UserAlreadyExistsException(String message) {
		super(message);
	}
}
