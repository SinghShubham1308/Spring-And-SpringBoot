package com.spring_springboot.task_management.security;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class BasicAuthorization {

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
			UserDetails userDetails = User.withUsername("SinghShubham").password("{noop}springSecurity").roles("USER")
					.build();
			jdbcUserDetailsManager.createUser(userDetails);
		}

		if (!jdbcUserDetailsManager.userExists("Admin")) {
			UserDetails adminDetails = User.withUsername("Admin").password("{noop}springSecurity").roles("ADMIN")
					.build();
			jdbcUserDetailsManager.createUser(adminDetails);
		}

		return jdbcUserDetailsManager;
	}
}
