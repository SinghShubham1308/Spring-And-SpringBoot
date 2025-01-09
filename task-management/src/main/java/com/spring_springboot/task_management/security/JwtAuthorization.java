package com.spring_springboot.task_management.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.UUID;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwtAuthorization {
	private static Logger logger= LoggerFactory.getLogger(JwtAuthorization.class);
	
	
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
		dataSource.setUrl(
				"jdbc:mysql://localhost:3309/task_Management?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("1234567890");
		return dataSource;
	}

	@Bean
	public UserDetailsService users(DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

		PasswordEncoder passwordEncoder = passwordEncoder();

		if (!jdbcUserDetailsManager.userExists("SinghShubham")) {
//			UserDetails userDetails = User.withDefaultPasswordEncoder().username("SinghShubham")
//					.password("springSecurity").roles("user").build();
//			System.out.println("_________________________________" + userDetails.getPassword());
			// {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
	        UserDetails userDetails = User.withUsername("SinghShubham")
	                .password(passwordEncoder.encode("springSecurity")) // Encode the password
	                .roles("USER")
	                .build();
	        logger.info("username {} and password is ",userDetails.getUsername(),userDetails.getPassword());
			jdbcUserDetailsManager.createUser(userDetails);
		}

		if (!jdbcUserDetailsManager.userExists("Admin")) {
			UserDetails adminDetails = User.withUsername("Admin").password(passwordEncoder.encode("springSecurity")) // Encode
																														// the
																														// password
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
	public KeyPair keyPair() {
		KeyPairGenerator generator;
		try {
			generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Bean
	public RSAKey rsaKey(KeyPair keyPair) {
		return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).privateKey(keyPair.getPrivate())
				.keyID(UUID.randomUUID().toString()).build();
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) throws ParseException, NoSuchAlgorithmException {

		JWKSet jwkSet = new JWKSet(rsaKey); // calling generateHSKey()
//		System.out.println(jwkSet.toJSONObject()); // {keys:[]} 

//		return new JWKSource<SecurityContext>() {
//
//			@Override
//			public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException {
//				// TODO Auto-generated method stub
//				return jwkSelector.select(jwkSet);
//			}
//		};
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
	}

	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}
}
