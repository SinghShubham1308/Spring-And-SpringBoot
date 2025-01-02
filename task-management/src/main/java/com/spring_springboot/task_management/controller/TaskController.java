package com.spring_springboot.task_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@PostMapping
	public Task createTask(@RequestBody Task task) {
		return taskService.createTask(task);
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
