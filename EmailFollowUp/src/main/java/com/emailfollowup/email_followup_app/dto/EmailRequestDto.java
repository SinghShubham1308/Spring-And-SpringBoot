package com.emailfollowup.email_followup_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SinghShubham1308
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {

	private String recipient;
    private String subject;
    private String body;
}
