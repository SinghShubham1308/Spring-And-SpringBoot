package com.shubham.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubham.portfolio.dto.AuthenticationRequest;
import com.shubham.portfolio.dto.AuthenticationResponse;
import com.shubham.portfolio.repository.UserRepository;
import com.shubham.portfolio.service.JwtService;

/**
 * @author SinghShubham
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final JwtService jwtService;

	public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository,
			JwtService jwtService) {
		super();
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}

	/**
	 * Handles the login request.
	 * 
	 * @param request The AuthenticationRequest DTO containing username and
	 *                password.
	 * @return A ResponseEntity containing the JWT token in an
	 *         AuthenticationResponse.
	 */
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
		// 1. Authenticate the user with Spring Security's AuthenticationManager.
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		// 2. If authentication is successful, fetch the user from the repository.
		var user = userRepository.findByUsername(request.getUsername()).orElseThrow(); // Should not happen if
																						// authentication passed

		// 3. Generate a JWT token for this user.
		var jwtToken = jwtService.generateToken(user);

		// 4. Return the token in the response body.
		return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).build());
	}
}