package com.in28minutes.rest.webservices.restfulwebservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in28minutes.rest.webservices.restfulwebservices.entity.Todo;

@Repository
public interface TodosRepository  extends JpaRepository<Todo, String>{
	List<Todo> findByUsername(String username);
}
