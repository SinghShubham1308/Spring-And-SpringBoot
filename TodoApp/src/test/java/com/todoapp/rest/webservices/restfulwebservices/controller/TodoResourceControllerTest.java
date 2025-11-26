package com.todoapp.rest.webservices.restfulwebservices.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.rest.webservices.restfulwebservices.entity.Todo;
import com.todoapp.rest.webservices.restfulwebservices.exceptions.ResourceNotFoundException;
import com.todoapp.rest.webservices.restfulwebservices.services.TodoService;

@WebMvcTest(TodoResourceController.class)
class TodoResourceControllerTest {

	// MockMvc allows us to send HTTP requests to our controller
	@Autowired
	private MockMvc mockMvc;

	// We provide a mock of the service layer, so we're not using the real business
	// logic
	@MockitoBean
	private TodoService todoService;

	// ObjectMapper helps with converting Java objects to JSON strings
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "testuser")
	void testRetrieveTodos_ShouldReturnListOfTodos() throws Exception {
		// 1. Arrange
		String username = "testuser";
		List<Todo> todos = List.of(new Todo("1", username, "Learn Integration Testing", LocalDate.now(), false));

		// Given: When the todoService.findByUsername is called, return our list of
		// todos.
		// We use BDDMockito (given) which is a nice style for test arrangement.
		given(todoService.findByUsername(username)).willReturn(todos);

		// 2. Act
		// Perform a GET request to the endpoint
		ResultActions response = mockMvc.perform(get("/user/todos", username).contentType(MediaType.APPLICATION_JSON));

		// 3. Assert
		// Check if the response is as expected
		response.andExpect(status().isOk()) // Expect HTTP 200 OK
				.andExpect(jsonPath("$.size()", is(todos.size()))) // Expect the JSON array to have 2 items
				.andExpect(jsonPath("$[0].description", is("Learn Integration Testing"))); // Expect the first item's
																							// description to match
	}

	@Test
	@WithMockUser(username = "testuser")
	void testRetrieveTodosWithId_ShouldReturnTodo() throws Exception {
		// 1. Arrange
		String username = "testuser";
		String userId = "1";
		Todo expectedtodo = new Todo(userId, username, "Learn Integration Testing", LocalDate.now(), false);

		// Given: When the todoService.findByUsername is called, return our list of
		// todos.
		// We use BDDMockito (given) which is a nice style for test arrangement.
		given(todoService.findById(userId)).willReturn(expectedtodo);

		// 2. Act
		// Perform a GET request to the endpoint
		ResultActions response = mockMvc
				.perform(get("/user/todos/{id}", userId).contentType(MediaType.APPLICATION_JSON));

		// 3. Assert
		// Check if the response is as expected
		response.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(expectedtodo.getId())));

	}

	@Test
	@WithMockUser(username = "testuser", authorities = { "ROLE_USER" })
	void testAddTodo_ShouldCreateAndReturnNewTodo() throws Exception {
		// 1. Arrange
		String username = "testuser";

		// This is the object we will send in the request body
		Todo todoToAdd = new Todo(null, username, "New POST Task", LocalDate.now(), false);

		// This is the object we expect the service to return after saving (with an ID)
		Todo savedTodo = new Todo("1", username, "New POST Task", LocalDate.now(), false);

		// Given: When todoService.addTodo is called with these details, return the
		// savedTodo
		given(todoService.addTodo(any(Todo.class))).willReturn(savedTodo);

		// 2. Act
		// Perform a POST request
		ResultActions response = mockMvc.perform(post("/user/todos", username).contentType(MediaType.APPLICATION_JSON)
				// Use ObjectMapper to convert the Java object to a JSON string
				.content(objectMapper.writeValueAsString(todoToAdd)).with(csrf()));

		// 3. Assert
		// Expect HTTP 201 Created status
		response.andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(savedTodo.getId())))
				.andExpect(jsonPath("$.description", is(savedTodo.getDescription())));
	}

	@Test
	@WithMockUser(username = "testuser", authorities = { "ROLE_USER" })
	void testUpdateTodo_WhenTodoExists_ShouldUpdateAndReturnTodo() throws Exception {
		// 1. Arrange
		String username = "testuser";
		String todoId = "1";

		// This is the updated data we will send in the request body
		Todo updatedTodo = new Todo(todoId, username, "Updated PUT Task", LocalDate.now(), true);

		// Given: When the service is called to update, return the updated object
		given(todoService.updateTodo(eq(todoId), any(Todo.class))).willReturn(updatedTodo);

		// 2. Act
		// Perform a PUT request with the updated todos in the request body
		ResultActions response = mockMvc.perform(put("/user/todos/{id}", todoId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedTodo)).with(csrf()));

		// 3. Assert
		// Expect HTTP 200 OK for a successful update
		response.andExpect(status().isOk()).andExpect(jsonPath("$.description", is(updatedTodo.getDescription())))
				.andExpect(jsonPath("$.done", is(true)));
	}

	@Test
	@WithMockUser(username = "testuser", authorities = { "ROLE_USER" })
	void testUpdateTodo_WhenTodoDoesNotExist_ShouldReturnNotFound() throws Exception {
		// 1. Arrange
		String username = "testuser";
		String nonExistentId = "999";
		Todo updatedTodo = new Todo(nonExistentId, username, "Updated PUT Task", LocalDate.now(), true);

		// Given: When the service is called with a non-existent ID, it returns null
		given(todoService.updateTodo(eq(nonExistentId), any(Todo.class)))
				.willThrow(new ResourceNotFoundException("Todo not found"));

		// 2. Act
		ResultActions response = mockMvc
				.perform(put("/user/todos/{id}", nonExistentId).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedTodo)).with(csrf()));

		// 3. Assert
		// Expect HTTP 404 Not Found
		response.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "testuser")
	void testDeleteTodoById_ShouldReturnOk() throws Exception {
		// 1. Arrange
		String todoId = "1";

		// Given: The service's deleteById method will run without throwing an
		// exception.
		willDoNothing().given(todoService).deleteById(todoId);

		// 2. Act
		ResultActions response = mockMvc.perform(delete("/user/todos/{id}", todoId)
				// ✅ Include a valid CSRF token in the request
				.with(csrf()));

		// 3. Assert
		// Expect HTTP 200 OK
		response.andExpect(status().isNoContent());

		// Also, verify that the service method was called exactly once with the correct
		// ID
		verify(todoService, times(1)).deleteById(todoId);
	}
}
