package com.shubham.portfolio.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubham.portfolio.model.ContactRequest;
import com.shubham.portfolio.model.Project;
import com.shubham.portfolio.service.EmailService;
import com.shubham.portfolio.service.ProjectService;

/**
 * @author SinghShubham
 */
@RestController
@RequestMapping("/api/v1")
public class ProjectController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
	private ProjectService service;
	private EmailService emailService;

	public ProjectController(ProjectService service, EmailService emailService) {
		super();
		this.service = service;
		this.emailService = emailService;
	}

	@GetMapping("/projects")
	public List<Project> getProjects() {
		List<Project> allProjects = new ArrayList<>();
		List<Project> projects = service.getAllProjects();
		for (Project project : projects) {
			allProjects.add(project);
		}
		return allProjects;
	}

	@GetMapping("/projects/{id}")
	public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
		Project project = service.getProjectById(id);
		return ResponseEntity.ok(project);
	}

	@PostMapping("/projects")
	public ResponseEntity<Project> createProject(@RequestBody Project project) {
		Project newProject = service.createProject(project);
		// Return a 201 Created status
		return new ResponseEntity<>(newProject, HttpStatus.CREATED);
	}

	@PostMapping("/contact")
	public ResponseEntity<Void> handleContactRequest(@RequestBody ContactRequest contactRequest) {
		LOGGER.info("CONTACT REQUEST RECEIVED: {}", contactRequest.getName());

		// Email service ko call karein
		try {
			emailService.sendContactEmail(contactRequest);
			// Email safalta se bhej diya gaya
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			// Agar email bhejne mein koi error aaye
			LOGGER.error("Failed to send email", e);
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        // Hum service ko call karke project ko delete karenge
        try {
            service.deleteProject(id); // Yeh method hum service file mein banayenge
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            LOGGER.error("Error deleting project with id: {}", id, e);
            // Agar project nahi milta hai toh NOT_FOUND bhej sakte hain
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
