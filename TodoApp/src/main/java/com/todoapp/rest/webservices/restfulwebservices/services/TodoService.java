package com.todoapp.rest.webservices.restfulwebservices.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.todoapp.rest.webservices.restfulwebservices.entity.Todo;
import com.todoapp.rest.webservices.restfulwebservices.repository.TodosRepository;

@Service
public class TodoService {

	TodosRepository todosRepository;

	public TodoService(TodosRepository todosRepository) {
		super();
		this.todosRepository = todosRepository;
	}

	public List<Todo> findByUsername(String username) {
		return todosRepository.findByUsername(username);
	}

	public Todo addTodo(String username, String description, LocalDate targetDate, boolean done) {
		Todo todo = Todo.builder().username(username).description(description).targetDate(targetDate).done(done)
				.build();
		return todosRepository.save(todo);
	}

	public void deleteById(String id) {
		todosRepository.deleteById(id);
	}

	public Todo findById(String id) {
		return todosRepository.findById(id).orElse(null);
	}

	public Todo updateTodo(String id, Todo updatedTodo) {
		Optional<Todo> optionalTodo = todosRepository.findById(id);
		if (optionalTodo.isPresent()) {
			Todo todo = optionalTodo.get();
			todo.setDescription(updatedTodo.getDescription());
			todo.setDone(updatedTodo.isDone());
			todo.setTargetDate(updatedTodo.getTargetDate());
			return todosRepository.save(todo);
		}
		return null; // If not found
	}

}