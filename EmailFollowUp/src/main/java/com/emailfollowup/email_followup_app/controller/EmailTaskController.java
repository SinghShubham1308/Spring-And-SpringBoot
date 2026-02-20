package com.emailfollowup.email_followup_app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emailfollowup.email_followup_app.dto.EmailRequestDto;
import com.emailfollowup.email_followup_app.dto.EmailResponseDto;
import com.emailfollowup.email_followup_app.entitty.EmailTask;
import com.emailfollowup.email_followup_app.service.EmailFetchService;
import com.emailfollowup.email_followup_app.service.EmailTaskService;

/**
 * @author SinghShubham1308
 */
@RestController
@RequestMapping("/api/emails")
public class EmailTaskController {

	private final EmailTaskService emailTaskService;
	private final EmailFetchService emailFetchService;

	public EmailTaskController(EmailTaskService emailTaskService, EmailFetchService emailFetchService) {
		this.emailTaskService = emailTaskService;
		this.emailFetchService = emailFetchService;
	}

	@PostMapping("/schedule")
	public EmailResponseDto scheduleEmail(@RequestBody EmailRequestDto emailTask) {
		return emailTaskService.scheduleEmail(emailTask);
	}

	@GetMapping
	public List<EmailTask> getAllEmails() {
		return emailTaskService.getAllTasks();
	}

	@GetMapping("/fetch-sent")
	public String fetchSentEmailsFromGmail() {
		emailFetchService.fetchSentEmails();
		return "Check your console logs! Started fetching sent emails...";
	}

	@GetMapping("/check-replies")
	public String checkReplies() {
		emailFetchService.checkInboxForReplies();
		return "Inbox checked for replies! See console logs.";
	}

}
