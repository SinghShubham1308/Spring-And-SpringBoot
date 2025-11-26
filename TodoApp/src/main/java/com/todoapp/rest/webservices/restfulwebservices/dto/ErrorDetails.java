package com.todoapp.rest.webservices.restfulwebservices.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SinghShubham
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
	private LocalDateTime timestamp;
	private String message;
	private String details;
}
