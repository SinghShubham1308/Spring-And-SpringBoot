package com.emailfollowup.email_followup_app.entitty;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String recipient;
	private String subject;
	private String body;
	private LocalDateTime createdAt;
	private String status;
	
	public EmailTask(String recipient, String subject, String body) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.createdAt = LocalDateTime.now(); // Current time set kar denge
        this.status = "PENDING"; // Default status
    }
}
