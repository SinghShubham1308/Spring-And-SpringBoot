package com.shubham.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shubham.portfolio.exception.ResourceNotFoundException;
import com.shubham.portfolio.model.Project;
import com.shubham.portfolio.repository.ProjectRepository;

/**
 * @author SinghShubham
 */
@Service
public class ProjectService {
	private final ProjectRepository projectRepository;

	public ProjectService(ProjectRepository projectRepository) {
		super();
		this.projectRepository = projectRepository;
	}

	public List<Project> getAllProjects() {
		// Use the custom method we defined
		return projectRepository.findAllByOrderByProjectTypeAsc();
	}

	public Project getProjectById(Long id) {
		return projectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
	}

	public Project createProject(Project project) {
		return projectRepository.save(project);
	}

	public void deleteProject(Long id) {
		if (!projectRepository.existsById(id)) {
			throw new RuntimeException("Project not found with id: " + id);
		}
		projectRepository.deleteById(id);
	}
}
