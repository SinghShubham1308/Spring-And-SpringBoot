package com.todoapp.rest.webservices.restfulwebservices.jwtsecurty;

import static org.springframework.security.config.Customizer.withDefaults;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class Jwtauthorization {
	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults())
				.authorizeHttpRequests(requests -> requests.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.antMatchers(HttpMethod.POST, "/api/signup").permitAll().antMatchers("/authenticate")
						.permitAll().anyRequest().authenticated());
		http.sessionManagement((sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));
		http.headers().frameOptions().sameOrigin();

		http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
		http.oauth2ResourceServer(resourceServer -> resourceServer.jwt(withDefaults()));

		return http.build();
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl(
				"jdbc:mysql://localhost:3309/todoapp?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true");
		dataSource.setUsername("root");
		dataSource.setPassword("1234567890");
		return dataSource;
	}

	@Bean
	public UserDetailsService embeddedUser(DataSource datasource) {
		UserDetails userDetails = User.withUsername("SinghShubham").password("springSecurity")
				.passwordEncoder(str -> passwordEncoder().encode(str))
//				.password("{noop}springSecurity")

				.roles("USER").build();
		UserDetails adminDetails = User.withUsername("Singhprashant").password("springSecurity")
				.passwordEncoder(str -> passwordEncoder().encode(str))
//				.password("{noop}springSecurity")
				.roles("ADMIN").build();

		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(datasource);
		jdbcUserDetailsManager.createUser(userDetails);
		jdbcUserDetailsManager.createUser(adminDetails);

		return jdbcUserDetailsManager;

	}
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Allow frontend origin
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true); // ✅ Allow credentials (Fixes your error)
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
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
			throw new IllegalStateException("RSA algorithm not found", e);
		}
	}

	@Bean
	public RSAKey getRsaKey(KeyPair keyPair) {
		return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).privateKey(keyPair.getPrivate())
				.keyID(UUID.randomUUID().toString()).build();
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {

		JWKSet jwkSet = new JWKSet(rsaKey); // calling generateHSKey()
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public JwtDecoder jwtDecoder(RSAKey getRsaKey) throws JOSEException {
		return NimbusJwtDecoder.withPublicKey(getRsaKey.toRSAPublicKey()).build();
	}

	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {

		return new NimbusJwtEncoder(jwkSource);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
