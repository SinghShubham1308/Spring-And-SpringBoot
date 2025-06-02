package com.todoapp.rest.webservices.restfulwebservices.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todoapp.rest.webservices.restfulwebservices.entity.User;
import com.todoapp.rest.webservices.restfulwebservices.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {


    private final UserService userService;

	public UserController(UserService userService) {
		this.userService= userService;
	}

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.userExists(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        // Save user (hash password in production)
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
