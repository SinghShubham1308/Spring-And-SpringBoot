package com.spring.springsecurity.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;

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
	@PreAuthorize("hasRole('ROLE_USER') and #username == authentication.principal.username")
	@PostAuthorize("returnObject.username=='SinghShubham'")
	@RolesAllowed({"USER","ADMIN"})
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	public Todo getFirstTodo(@PathVariable("username") @P("username") String username) {
	    return todoList.stream()
	                   .filter(todo -> todo.username().equals(username)) // Filter by username
	                   .findFirst() // Get the first matching Todo
	                   .orElseThrow(() -> new NoSuchElementException("Todo not found for user: " + username));
	}

	
//	@GetMapping("/users/{username}/todos")
//	public List<Todo> debugAuthentication(@PathVariable("username") String username, Authentication authentication) {
//	    System.out.println("Authentication details: " + authentication.getPrincipal().toString());
//	    return todoList.stream()
//	                   .filter(todo -> todo.username().equals(username))
//	                   .toList();
//	}

	
	@PostMapping("/todos")
	public boolean createTodo(@RequestBody Todo todo) {
		return todoList.add(todo);
	}

}
