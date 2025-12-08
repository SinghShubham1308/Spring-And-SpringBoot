package com.shubham.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * @author SinghShubham
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final CorsConfigurationSource corsConfigurationSource;

	public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthFilter,
			CorsConfigurationSource corsConfigurationSource) {
		super();
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthFilter = jwtAuthFilter;
		this.corsConfigurationSource = corsConfigurationSource;
	}

	private static final String[] SWAGGER_WHITELIST = { "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs",
			"/v3/api-docs/**", "/swagger-resources/**", "/webjars/**" };

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource))

				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/v1/projects").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/v1/projects/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/v1/contact").permitAll()
						.requestMatchers(SWAGGER_WHITELIST).permitAll().requestMatchers("/api/v1/auth/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/v1/profile-data").permitAll().requestMatchers("/error")
						.permitAll().requestMatchers(HttpMethod.PUT, "/api/v1/profile-data").authenticated()
						.requestMatchers(HttpMethod.POST, "/api/v1/projects").authenticated()
						.requestMatchers(HttpMethod.DELETE, "/api/v1/projects/**").authenticated()
						.requestMatchers(HttpMethod.GET, "/actuator/health", "/api/v1/ping", "/", "/public/**",
								"/favicon.ico")
						.permitAll().anyRequest().authenticated())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}