package com.spring_springboot.task_management.security;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthentication {

	private JwtEncoder jwtEncoder;

	public JwtAuthentication(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	@PostMapping("/authentication/getToken")
	public JwtToken authenticate(Authentication authentication) {
		Map<String, String> tokenDetails = createToken(authentication);
		return new JwtToken(tokenDetails.get("type"), tokenDetails.get("token"), tokenDetails.get("expiresAt"));
	}

	private Map<String, String> createToken(Authentication authentication) {
		JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder().issuer("Self").issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(60 * 30)).subject(authentication.getName())
				.claim("scope", createScope(authentication)).claim("type", "bearer").build();

		String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
		Instant expiresAt = jwtClaimsSet.getExpiresAt();
		String type = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getClaimAsString("type");
		Map<String, String> response = new HashMap<>();
		response.put("token", tokenValue);
		response.put("expiresAt", expiresAt.toString()); // Convert Instant to String
		response.put("type", type);

		return response;
	}

	private Object createScope(Authentication authentication) {
		return authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(" "));
	}
}

record JwtToken(String type, String tokenValue, String expiresIn) {
}