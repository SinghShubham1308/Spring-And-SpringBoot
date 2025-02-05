package com.in28minutes.rest.webservices.restfulwebservices.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class JwtAuthorization {
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl(
				"jdbc:mysql://localhost:3309/todoapp?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("1234567890");
		return dataSource;
	}
}
