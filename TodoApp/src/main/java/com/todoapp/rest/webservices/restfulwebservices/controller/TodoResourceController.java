package com.todoapp.rest.webservices.restfulwebservices.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.todoapp.rest.webservices.restfulwebservices.entity.Todo;
import com.todoapp.rest.webservices.restfulwebservices.todo.TodoService;

@RestController
public class TodoResourceController {

//	@Autowired
	TodoService todoService;

	TodoResourceController(TodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping("/user/{username}/todos")
	public List<Todo> retreiveTodos(@PathVariable String username) {
		return todoService.findByUsername(username);
	}

	@GetMapping("/user/{username}/todos/{id}")
	public Todo retreiveTodosById(@PathVariable String username, @PathVariable String id) {
		return todoService.findById(id);
	}

	@DeleteMapping("/user/{username}/todos/{id}")
	public ResponseEntity<String> deleteTodosById(@PathVariable String username, @PathVariable String id) {
		todoService.deleteById(id);
		return ResponseEntity.ok("Todo deleted successfully with id " + id);
	}

	@PutMapping("/user/{username}/todos/{id}")
	public ResponseEntity<Todo> updateTodo(@PathVariable String username, @PathVariable String id,
			@RequestBody Todo todo) {
		Todo updatedTodo = todoService.updateTodo(id, todo);
		return updatedTodo != null ? ResponseEntity.ok(updatedTodo) : ResponseEntity.notFound().build();
	}

	@PostMapping("/user/{username}/todos")
	public ResponseEntity<Todo> addTodo(@PathVariable String username, @RequestBody Todo todo) {
		// Add todo using service
		Todo newTodo = todoService.addTodo(username, todo.getDescription(), todo.getTargetDate(), todo.isDone());

		// Return the created todo with 201 Created status
		return ResponseEntity.status(HttpStatus.CREATED).body(newTodo);
	}

}
