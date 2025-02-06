package com.todoapp.rest.webservices.restfulwebservices.todo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.todoapp.rest.webservices.restfulwebservices.entity.Todo;
import com.todoapp.rest.webservices.restfulwebservices.repository.TodosRepository;

@Service
public class TodoService {

//	private static List<Todo> todos = new ArrayList<>();

//	private static int todosCount = 0;
	TodosRepository todosRepository;

	public TodoService(TodosRepository todosRepository) {
		super();
		this.todosRepository = todosRepository;
	}

	/*
	 * static { todos.add(new Todo(++todosCount, "SinghShubham",
	 * "Get AWS Certified", LocalDate.now().plusYears(10), false)); todos.add(new
	 * Todo(++todosCount, "SinghShubham", "Learn DevOps",
	 * LocalDate.now().plusYears(11), false)); todos.add(new Todo(++todosCount,
	 * "SinghShubham", "Learn Full Stack Development",
	 * LocalDate.now().plusYears(12), false)); }
	 */

	/*
	 * public List<Todo> findByUsername(String username){ Predicate<? super Todo>
	 * predicate = todo -> todo.getUsername().equalsIgnoreCase(username); return
	 * todos.stream().filter(predicate).toList(); }
	 * 
	 * public Todo addTodo(String username, String description, LocalDate
	 * targetDate, boolean done) { Todo todo = new Todo(++todosCount, username,
	 * description, targetDate, done); todos.add(todo); return todo; }
	 * 
	 * public void deleteById(int id) { Predicate<? super Todo> predicate = todo ->
	 * todo.getId() == id; todos.removeIf(predicate); }
	 * 
	 * public Todo findById(int id) { Predicate<? super Todo> predicate = todo ->
	 * todo.getId() == id; Todo todo =
	 * todos.stream().filter(predicate).findFirst().get(); return todo; }
	 * 
	 * public Todo updateTodo(int id, Todo updatedTodo) { for (Todo todo : todos) {
	 * if (todo.getId() == id) { todo.setDescription(updatedTodo.getDescription());
	 * todo.setDone(updatedTodo.isDone());
	 * todo.setTargetDate(updatedTodo.getTargetDate()); return todo; // Successfully
	 * updated } } return null; // If no todo with given ID is found }
	 */
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