package com.todoapp.rest.webservices.restfulwebservices.jwtsecurty;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.todoapp.rest.webservices.restfulwebservices.dto.AuthRequest;
import com.todoapp.rest.webservices.restfulwebservices.dto.JwtResponse;


@RestController
public class JwtAuthenticationResource {


	private final JwtTokenService jwtTokenService;

	private final AuthenticationManager authenticationManager;
	public JwtAuthenticationResource(JwtTokenService jwtTokenService,AuthenticationManager authenticationManager) {
		this.jwtTokenService = jwtTokenService;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/authenticate")
	public JwtResponse authenticate(@RequestBody AuthRequest authRequest) {
	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
	    );

	    String tokenDetails = jwtTokenService.generateToken(authentication);
//	    return new jwtResponse(tokenDetails.get("token"), tokenDetails.get("expiresAt"), tokenDetails.get("type"));
	    return new JwtResponse(tokenDetails, Instant.now().plusSeconds(60 * 30).toString(), "bearer");
	}

}


