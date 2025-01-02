package com.spring_springboot.task_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring_springboot.task_management.entity.Task;
import com.spring_springboot.task_management.repository.TaskRepository;

@Service
public class TaskService {

	private TaskRepository repository;

	public TaskService(TaskRepository repository) {
		this.repository = repository;
	}
	public List<Task> getAllTasks(){
		return repository.findAll();
	}
	
	public Task createTask(Task task){
		return repository.save(task);
	}
	
	public Task updateTask(Long id, Task updatedTask) {
        return repository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setPriority(updatedTask.getPriority());
            task.setStatus(updatedTask.getStatus());
            task.setDueDate(updatedTask.getDueDate());
            task.setAssignee(updatedTask.getAssignee());
            return repository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }
	public void deletetask(Long id) {
		repository.deleteById(id);
	}
}
