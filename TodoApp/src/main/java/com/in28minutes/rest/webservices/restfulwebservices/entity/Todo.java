package com.in28minutes.rest.webservices.restfulwebservices.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.in28minutes.rest.webservices.restfulwebservices.util.TodoIdGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "todos")
public class Todo {

	@Id
	private String id;

	private String username;

	private String description;
	private LocalDate targetDate;
	private boolean done;
	
	
	 @PrePersist
	    private void generateId() {
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
	        String sequenceNumber = TodoIdGenerator.getNextSequence();  // Get the next sequence number
	        this.id = "TA-" + currentDate + "-" + sequenceNumber;
	    }
}