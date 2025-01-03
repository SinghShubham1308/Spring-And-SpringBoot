package com.spring_springboot.task_management.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring_springboot.task_management.entity.Task;
import com.spring_springboot.task_management.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	
	private TaskService taskService;

	
	public TaskController(TaskService taskService) {
		super();
		this.taskService = taskService;
	}

	@GetMapping()
	public List<Task> getAllTask() {
		return taskService.getAllTasks();
	}
	
	@GetMapping("/{taskId}")
	public Optional<Task> getTaskById(@PathVariable Long taskId) {
	    return taskService.getTaskById(taskId);
	}

	
	@PostMapping
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
		Task savedTask = taskService.createTask(task);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
	}
	
	@PutMapping("/{id}")
	public Task updateTask(@PathVariable Long id,@RequestBody Task updatedTask) {
		return taskService.updateTask(id, updatedTask);
	}
	@DeleteMapping("/{id}")
	public void deleteTask(@PathVariable Long id) {
		taskService.deletetask(id);
	}
}
