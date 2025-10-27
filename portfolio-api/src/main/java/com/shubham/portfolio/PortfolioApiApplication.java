package com.shubham.portfolio;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.shubham.portfolio.model.Project;
import com.shubham.portfolio.repository.ProjectRepository;

@SpringBootApplication
public class PortfolioApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioApiApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ProjectRepository repository) {
		return args -> {
			// Clean the database every time we restart
			repository.deleteAll();

			// --- Project 1: Professional Case Study (SAML) ---
			Project samlProject = new Project();
			samlProject.setTitle("Enterprise SAML 2.0 SSO Integration");
			samlProject.setDescription(
					"Integrated a new Spring Boot application with on-premise Active Directory (ADFS) for SSO. Responsible for configuring Spring Security's SAML extension, managing metadata exchange, and handling assertion processing.");
			samlProject.setProjectType("PROFESSIONAL");
			samlProject.setTechnologiesUsed(List.of("Java", "Spring Boot", "Spring Security", "SAML 2.0", "ADFS"));
			samlProject.setGithubLink(null); // No public link

			// --- Project 2: Professional Case Study (Genesys) ---
			Project genesysProject = new Project();
			genesysProject.setTitle("Real-Time Analytics Listener for Genesys PureCloud");
			genesysProject.setDescription(
					"Built a Java service to capture real-time agent data. Implemented a Quartz Scheduler to poll REST APIs and subscribed to a WebSocket for immediate event notifications.");
			genesysProject.setProjectType("PROFESSIONAL");
			genesysProject
					.setTechnologiesUsed(List.of("Java", "Spring Boot", "Quartz", "WebSocket", "Genesys Cloud API"));

			// --- Project 3: Public Demo (The Portfolio Itself) ---
			Project portfolioProject = new Project();
			portfolioProject.setTitle("Full-Stack Portfolio Website");
			portfolioProject.setDescription(
					"This website. A decoupled full-stack application featuring a React frontend and a Spring Boot REST API backend with a PostgreSQL database.");
			portfolioProject.setProjectType("PUBLIC_DEMO");
			portfolioProject.setTechnologiesUsed(List.of("Java", "Spring Boot", "JPA", "React", "PostgreSQL", "AWS"));
			portfolioProject
					.setGithubLink("https://github.com/SinghShubham1308/Spring-And-SpringBoot/tree/main/portfolio-api"); // Add
																															// your
																															// link

			// Save them all to the database
			repository.saveAll(List.of(samlProject, genesysProject, portfolioProject));
		};
	}
}
