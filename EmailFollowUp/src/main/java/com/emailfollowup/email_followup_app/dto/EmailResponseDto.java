package com.emailfollowup.email_followup_app.dto;

import java.time.LocalDateTime;

import com.emailfollowup.email_followup_app.entitty.EmailStatus;

/**
 * @author SinghShubham1308
 */
public record EmailResponseDto(Long id, String recipient, String subject, String body, EmailStatus status,
		LocalDateTime createdAt) {
}
