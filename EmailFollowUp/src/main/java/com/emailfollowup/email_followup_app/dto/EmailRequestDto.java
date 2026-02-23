package com.emailfollowup.email_followup_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author SinghShubham1308
 */
public record EmailRequestDto(
		@NotBlank(message = "Recipient email is required") @Email(message = "Please provide a valid email address") String recipient,

		@NotBlank(message = "Subject cannot be empty") String subject,

		@NotBlank(message = "Email body cannot be empty") String body) {
}
