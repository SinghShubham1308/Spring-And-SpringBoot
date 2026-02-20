package com.emailfollowup.email_followup_app.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SinghShubham1308
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponseDto {

	private Long id;
	private String recipient;
	private String subject;
	private String body;
	private String status;
	private LocalDateTime createdAt;
}
