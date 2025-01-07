package com.spring.springsecurity.basic;

import static org.springframework.security.config.Customizer.withDefaults;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JWTAuthSecurityConfiguration {

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
		http.sessionManagement((sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));
//		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
//		http.headers().frameOptions().sameOrigin();
		http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
//		http.csrf().disable();
		http.csrf(csrf -> csrf.disable());
		http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
//		http.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(withDefaults()));
		return http.build();
	}

	@Bean
	DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScripts(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION).build();
	}

//	@Bean
//	public UserDetailsService inMemomryUser() {
//		UserDetails userDetails = User.withUsername("SinghShubham").password("{noop}springSecurity").roles().build();
//		UserDetails adminDetails = User.withUsername("Admin").password("{noop}springSecurity").roles("ADMIN").build();
//		return new InMemoryUserDetailsManager(userDetails, adminDetails);
//
//	}

	@Bean
	public UserDetailsService embeddedUser(DataSource datasource) {
		UserDetails userDetails = User.withUsername("SinghShubham").password("springSecurity")
				.passwordEncoder(str -> passwordEncoder().encode(str))
//				.password("{noop}springSecurity")

				.roles("USER").build();
		UserDetails adminDetails = User.withUsername("Admin").password("springSecurity")
				.passwordEncoder(str -> passwordEncoder().encode(str))
//				.password("{noop}springSecurity")
				.roles("ADMIN").build();

		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(datasource);
		jdbcUserDetailsManager.createUser(userDetails);
		jdbcUserDetailsManager.createUser(adminDetails);

		return jdbcUserDetailsManager;

	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public KeyPair KeyPair() {
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
	public RSAKey getRsaKey(KeyPair keyPair) {
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
	public JwtDecoder jwtDecoder(RSAKey getRsaKey) throws JOSEException {
		return NimbusJwtDecoder.withPublicKey(getRsaKey.toRSAPublicKey()).build();
	}

}
