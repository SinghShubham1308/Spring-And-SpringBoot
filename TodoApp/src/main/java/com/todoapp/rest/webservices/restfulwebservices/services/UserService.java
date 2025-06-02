package com.todoapp.rest.webservices.restfulwebservices.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todoapp.rest.webservices.restfulwebservices.entity.User;
import com.todoapp.rest.webservices.restfulwebservices.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean userExists(String username) {
		return userRepository.existsByUsername(username);
	}

	public User saveUser(User user) {
		// Hash password before saving!
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
