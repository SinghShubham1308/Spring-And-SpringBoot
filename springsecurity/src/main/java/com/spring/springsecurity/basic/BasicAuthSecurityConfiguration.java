package com.spring.springsecurity.basic;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true,jsr250Enabled = true,securedEnabled = true)
public class BasicAuthSecurityConfiguration {

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> 
		requests.requestMatchers("/users").hasRole("USER").
		requestMatchers("/admin/**").hasRole("ADMIN").
		anyRequest().authenticated());
		http.sessionManagement((sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));
//		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
//		http.headers().frameOptions().sameOrigin();
		http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
//		http.csrf().disable();
		http.csrf(csrf -> csrf.disable());
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
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
