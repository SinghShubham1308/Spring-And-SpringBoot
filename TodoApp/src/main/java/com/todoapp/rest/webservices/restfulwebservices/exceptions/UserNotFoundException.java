package com.todoapp.rest.webservices.restfulwebservices.exceptions;

/**
 * @author SinghShubham
 */
public class UserNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2655611081692073688L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
