package com.spring_springboot.task_management.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwtAuthorization {

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
		http.sessionManagement((sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));
//		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
//		http.headers().frameOptions().sameOrigin();
//		http.csrf().disable();
		http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
		http.csrf(csrf -> csrf.disable());
		http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
//		http.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(withDefaults()));

		return http.build();
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3309/task_Management");
		dataSource.setUsername("root");
		dataSource.setPassword("1234567890");
		return dataSource;
	}

	@Bean
	public UserDetailsService users(DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

		// Check if the user already exists to avoid duplicates
		if (!jdbcUserDetailsManager.userExists("SinghShubham")) {
			UserDetails userDetails = User.withUsername("SinghShubham").password("springSecurity")
					.passwordEncoder(str -> passwordEncoder().encode(str))
//					.password("{noop}springSecurity")
					.roles("USER").build();
			jdbcUserDetailsManager.createUser(userDetails);
		}

		if (!jdbcUserDetailsManager.userExists("Admin")) {
			UserDetails adminDetails = User.withUsername("Admin").password("springSecurity")
					.passwordEncoder(str -> passwordEncoder().encode(str))
//					.password("{noop}springSecurity")
					.roles("ADMIN").build();
			jdbcUserDetailsManager.createUser(adminDetails);
		}

		return jdbcUserDetailsManager;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public KeyPair KeyPairGenerator() {
		KeyPairGenerator generator;
		try {
			generator = KeyPairGenerator.getInstance("RSA ");
			generator.initialize(2048);
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Bean
	public RSAKey getRsaKey(KeyPair keyPair) {
		return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).privateKey(keyPair.getPrivate())
				.keyID(UUID.randomUUID().toString()).build();
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) throws ParseException, NoSuchAlgorithmException {

		JWKSet jwkSet = new JWKSet(rsaKey); // calling generateHSKey()
//		System.out.println(jwkSet.toJSONObject()); // {keys:[]} 

		return new JWKSource<SecurityContext>() {

			@Override
			public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException {
				// TODO Auto-generated method stub
				return jwkSelector.select(jwkSet);
			}
		};
//		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}
}
