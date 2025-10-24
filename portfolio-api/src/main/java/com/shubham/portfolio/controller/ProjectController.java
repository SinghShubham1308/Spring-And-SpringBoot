package com.shubham.portfolio.controller;

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

import com.shubham.portfolio.model.Project;
import com.shubham.portfolio.service.ProjectService;

/**
 * @author SinghShubham
 */
@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

	private ProjectService service;

	public ProjectController(ProjectService service) {
		super();
		this.service = service;
	}
	
	@GetMapping
	public List<Project> getProjects() {
		return service.getAllProjects();
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = service.getProjectById(id);
        return ResponseEntity.ok(project);
    }
	
	@PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project newProject = service.createProject(project);
        // Return a 201 Created status
        return new ResponseEntity<>(newProject, HttpStatus.CREATED);
    }
	
}
