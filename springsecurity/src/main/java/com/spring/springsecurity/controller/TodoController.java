package com.spring.springsecurity.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

record Todo(String username, String description) {
}

@RestController
public class TodoController {
	private static final List<Todo> todoList = new ArrayList<>(List.of(
		    new Todo("SinghShubham", "learn Aws"),
		    new Todo("Singhprashant", "spring security")
		));

	@GetMapping("/todos")
	public List<Todo> getAllTodos() {
		return todoList;
	}

	@GetMapping("/users/{username}/todos")
	public List<Todo> getFirstTodo(@PathVariable("username") String username) {
	    return todoList.stream()
	                   .filter(todo -> todo.username().equals(username)) // Compare the username field
	                   .toList(); // Collect filtered items into a list
	}
	
	@PostMapping("/todos")
	public boolean createTodo(@RequestBody Todo todo) {
		return todoList.add(todo);
	}

}
