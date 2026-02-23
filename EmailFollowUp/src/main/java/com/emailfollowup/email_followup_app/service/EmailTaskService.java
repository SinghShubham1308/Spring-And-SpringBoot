package com.emailfollowup.email_followup_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emailfollowup.email_followup_app.dto.EmailRequestDto;
import com.emailfollowup.email_followup_app.dto.EmailResponseDto;
import com.emailfollowup.email_followup_app.entitty.EmailStatus;
import com.emailfollowup.email_followup_app.entitty.EmailTask;
import com.emailfollowup.email_followup_app.exception.ResourceNotFoundException;
import com.emailfollowup.email_followup_app.repository.EmailTaskRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author SinghShubham1308
 */
@Service
@RequiredArgsConstructor
public class EmailTaskService {
	private final EmailTaskRepository repository;
	private final EmailService service;

	public EmailResponseDto scheduleEmail(EmailRequestDto requestDto) {
		EmailTask emailTask = new EmailTask();
		emailTask.setRecipient(requestDto.recipient());
		emailTask.setBody(requestDto.body());
		emailTask.setSubject(requestDto.subject());
		emailTask.setStatus(EmailStatus.PENDING);
		EmailTask savedTask = repository.save(emailTask);
		try {
			service.sendEmail(savedTask.getRecipient(), savedTask.getSubject(), savedTask.getBody());
			savedTask.setStatus(EmailStatus.SENT);
		} catch (Exception e) {
			savedTask.setStatus(EmailStatus.FAILED);
		}
		savedTask = repository.save(savedTask);
		return mapToResponseDto(savedTask);
	}

	public List<EmailTask> getAllTasks() {
		return repository.findAll();
	}

	public List<EmailTask> getPendingTasks() {
		return repository.findByStatus(EmailStatus.PENDING);
	}

	public EmailResponseDto getTaskById(Long id) {
		EmailTask task = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Email Task not found with id: " + id));
		return mapToResponseDto(task);
	}

	private EmailResponseDto mapToResponseDto(EmailTask task) {
		return new EmailResponseDto(task.getId(), task.getRecipient(), task.getSubject(), task.getBody(),
				task.getStatus(), task.getCreatedAt());
	}
}
