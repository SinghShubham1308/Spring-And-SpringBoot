package com.todoapp.rest.webservices.restfulwebservices.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todoapp.rest.webservices.restfulwebservices.entity.Todo;
import com.todoapp.rest.webservices.restfulwebservices.repository.TodosRepository;

/**
 * @author SinghShubham
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

	@Mock
	private TodosRepository repository;
	@InjectMocks
	private TodoService todoService;

	@Test
	void testFindByUsername_ShouldReturnListOfTodos() {
		// 1. Arrange: Set up the test data and mock behavior
		String username = "testuser";
		List<Todo> expectedTodos = List.of(new Todo("1", username, "Learn Testing", LocalDate.now(), false),
				new Todo("2", username, "Write Unit Tests", LocalDate.now().plusDays(1), false));

		// Tell Mockito: "When todosRepository.findByUsername is called with 'testuser',
		// then return our list of expectedTodos."
		Mockito.when(repository.findByUsername(username)).thenReturn(expectedTodos);

		// 2. Act: Call the service method we are testing
		List<Todo> actualTodos = todoService.findByUsername(username);

		// 3. Assert: Verify the result
		Assertions.assertNotNull(actualTodos); // Check that the list is not null
		Assertions.assertEquals(2, actualTodos.size()); // Check that the list has 2 items
		Assertions.assertEquals("Learn Testing", actualTodos.get(0).getDescription()); // Check a specific item
	}

	@Test
	void testAddTodo_ShouldSaveAndReturnTodo() {
		// 1. Arrange: Define the input arguments and the expected return object
		
		Todo todoToSave = new Todo(null, "testuser", "Learn Mockito", LocalDate.now(), false);
		Todo expectedSavedTodo = new Todo("1", "testuser", "Learn Mockito", LocalDate.now(), false);

		// This is the key change: Use an argument matcher.
		// "When repository.save is called with ANY object of type Todos,
		// then return our predefined 'expectedSavedTodo' object."
		Mockito.when(repository.save(ArgumentMatchers.any(Todo.class))).thenReturn(expectedSavedTodo);

		// 2. Act: Call the service method
		Todo actualTodo = todoService.addTodo(todoToSave);

		// 3. Assert: Verify the result is what we expected
		Assertions.assertNotNull(actualTodo);
		Assertions.assertEquals(expectedSavedTodo.getId(), actualTodo.getId());
		Assertions.assertEquals(expectedSavedTodo.getDescription(), actualTodo.getDescription());
	}

	@Test
	void testDeleteById_ShouldCallRepositoryDelete() {
		// 1. Arrange: Define the ID we want to delete.
		// No need for Mockito.when() because the method returns void.
		String todoId = "1";

		// 2. Act: Call the service method
		todoService.deleteById(todoId);

		// 3. Assert (Verify): Check if the deleteById method on the mock repository
		// was called exactly 1 time with the correct todoId.
		Mockito.verify(repository, Mockito.times(1)).deleteById(todoId);
	}

	@Test
	void testFindById_ShouldReturTodo() {
		// 1. Arrange: Define the ID we want to delete.
		// No need for Mockito.when() because the method returns void.
		String username = "testuser";
		String description = "Learn Mockito Matchers";
		LocalDate targetDate = LocalDate.now();
		String todoId = "1";
		Todo expectedTodo = new Todo("1", username, description, targetDate, false);
		Mockito.when(repository.findById(todoId)).thenReturn(Optional.of(expectedTodo));
		// 2. Act: Call the service method
		Todo actualTodo = todoService.findById(todoId);
		Assertions.assertNotNull(actualTodo);
		Assertions.assertEquals(expectedTodo.getId(), actualTodo.getId());
	}

	@Test
	void testUpdateTodo_WhenTodoDoesNotExist_ShouldReturnNull() {
	    // 1. Arrange
	    String todoId = "nonexistent";
	    Todo updatedTodoInfo = new Todo(todoId, "testuser", "Updated Task", LocalDate.now(), true);
	    
	    // Mock the repository to return an empty Optional
	    Mockito.when(repository.findById(todoId)).thenReturn(Optional.empty());

	    // 2. Act
	    Todo result = todoService.updateTodo(todoId, updatedTodoInfo);

	    // 3. Assert
	    Assertions.assertNull(result);
	    
	    // Optionally, verify that the save method was NEVER called
	    Mockito.verify(repository, Mockito.never()).save(Mockito.any(Todo.class));
	}

}
