package com.in28minutes.rest.webservices.restfulwebservices.todo;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoResource {

	TodoService todoService = new TodoService();

	TodoResource(TodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping("/user/{username}/todos")
	public List<Todo> retreiveTodos(@PathVariable String username) {
		return todoService.findByUsername(username);
	}
	
	@GetMapping("/user/{username}/todos/{id}")
	public Todo retreiveTodosById(@PathVariable String username,@PathVariable int id) {
		return todoService.findById(id);
	}
	
	@DeleteMapping("/user/{username}/todos/{id}")
	public ResponseEntity<String> deleteTodosById(@PathVariable String username,@PathVariable int id) {
		todoService.deleteById(id);
		return ResponseEntity.ok("Todo deleted successfully with id {}"+id);
	}
		
	@PutMapping("/user/{username}/todos/{id}")
	public ResponseEntity<Todo> updateTodo(@PathVariable String username, 
	                                       @PathVariable int id, 
	                                       @RequestBody Todo todo) { 
	    Todo updatedTodo = todoService.updateTodo(id, todo);
	    return updatedTodo != null ? ResponseEntity.ok(updatedTodo) : ResponseEntity.notFound().build();
	}

}
