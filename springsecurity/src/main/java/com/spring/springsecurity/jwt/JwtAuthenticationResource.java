package com.spring.springsecurity.jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;

//@RestController
public class JwtAuthenticationResource {

	private final JwtEncoder jwtEncoder;

	public JwtAuthenticationResource(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	@PostMapping("/authenticate")
	public jwtResponse authenticate(Authentication authentication) {
		Map<String, String> tokenDetails = createToken(authentication);
		return new jwtResponse( tokenDetails.get("token"), tokenDetails.get("expiresAt"),
				tokenDetails.get("type"));
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

	private String createScope(Authentication authentication) {
		return authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(" "));
	}
}

record jwtResponse(String token, String expiresAt, String type) {
}
