package com.shubham.portfolio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubham.portfolio.model.ContactRequest;
import com.shubham.portfolio.model.Project;
import com.shubham.portfolio.service.ProjectService;

/**
 * @author SinghShubham
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ProjectController {

	private ProjectService service;

	public ProjectController(ProjectService service) {
		super();
		this.service = service;
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
		System.out.println("===================================");
		System.out.println("CONTACT REQUEST RECEIVED:");
		System.out.println("Name: " + contactRequest.getName());
		System.out.println("Email: " + contactRequest.getEmail());
		System.out.println("Message: " + contactRequest.getMessage());
		System.out.println("===================================");

		// Return 200 OK
		return ResponseEntity.ok().build();
	}

}
