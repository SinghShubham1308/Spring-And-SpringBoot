package com.emailfollowup.email_followup_app.exception;

/**
 * @author SinghShubham1308
 */

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8325252784414451335L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
