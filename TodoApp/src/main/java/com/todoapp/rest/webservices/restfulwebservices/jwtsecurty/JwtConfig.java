package com.todoapp.rest.webservices.restfulwebservices.jwtsecurty;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwtConfig {
	@Bean
	KeyPair keyPair() {
		KeyPairGenerator generator;
		try {
			generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
			throw new IllegalStateException("RSA algorithm not found", e);
		}
	}

	@Bean
	RSAKey getRsaKey(KeyPair keyPair) {
		return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).privateKey(keyPair.getPrivate())
				.keyID(UUID.randomUUID().toString()).build();
	}

	@Bean
	JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {

		JWKSet jwkSet = new JWKSet(rsaKey); // calling generateHSKey()
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	JwtDecoder jwtDecoder(RSAKey getRsaKey) throws JOSEException {
		return NimbusJwtDecoder.withPublicKey(getRsaKey.toRSAPublicKey()).build();
	}

	@Bean
	JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {

		return new NimbusJwtEncoder(jwkSource);
	}

}
