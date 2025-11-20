package com.shubham.portfolio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shubham.portfolio.model.Role;
import com.shubham.portfolio.model.User;
import com.shubham.portfolio.repository.ProjectRepository;
import com.shubham.portfolio.repository.UserRepository;

@SpringBootApplication
public class PortfolioApiApplication {
	@Value("${jwt.secret.key}")
//	@Value("${admin.email}")
	private String jwtkeyString;

	public static void main(String[] args) {

		SpringApplication.run(PortfolioApiApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ProjectRepository repository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
//			repository.deleteAll();
			/*
			 * repository.deleteAll();
			 * 
			 * // --- Project 1: Professional Case Study (SAML) --- Project samlProject =
			 * new Project(); samlProject.setTitle("Enterprise SAML 2.0 SSO Integration");
			 * samlProject.setDescription(
			 * "Integrated a new Spring Boot application with on-premise Active Directory (ADFS) for SSO. Responsible for configuring Spring Security's SAML extension, managing metadata exchange, and handling assertion processing."
			 * ); samlProject.setProjectType("PROFESSIONAL");
			 * samlProject.setTechnologiesUsed(List.of("Java", "Spring Boot",
			 * "Spring Security", "SAML 2.0", "ADFS")); samlProject.setGithubLink(null); //
			 * No public link
			 * 
			 * // --- Project 2: Professional Case Study (Genesys) --- Project
			 * genesysProject = new Project();
			 * genesysProject.setTitle("Real-Time Analytics Listener for Genesys PureCloud"
			 * ); genesysProject.setDescription(
			 * "Built a Java service to capture real-time agent data. Implemented a Quartz Scheduler to poll REST APIs and subscribed to a WebSocket for immediate event notifications."
			 * ); genesysProject.setProjectType("PROFESSIONAL"); genesysProject
			 * .setTechnologiesUsed(List.of("Java", "Spring Boot", "Quartz", "WebSocket",
			 * "Genesys Cloud API"));
			 * 
			 * // --- Project 3: Public Demo (The Portfolio Itself) --- Project
			 * portfolioProject = new Project();
			 * portfolioProject.setTitle("Full-Stack Portfolio Website");
			 * portfolioProject.setDescription(
			 * "This website. A decoupled full-stack application featuring a React frontend and a Spring Boot REST API backend with a PostgreSQL database."
			 * ); portfolioProject.setProjectType("PUBLIC_DEMO");
			 * portfolioProject.setTechnologiesUsed(List.of("Java", "Spring Boot", "JPA",
			 * "React", "PostgreSQL", "AWS")); portfolioProject .setGithubLink(
			 * "https://github.com/SinghShubham1308/Spring-And-SpringBoot/tree/main/portfolio-api"
			 * );
			 * 
			 * // Save them all to the database repository.saveAll(List.of(samlProject,
			 * genesysProject, portfolioProject));
			 */

//			userRepository.deleteAll(); // Pehle saare purane users delete karein (sirf dev ke liye)

			if (userRepository.findByUsername("upstreamdevotion").isEmpty()) {
				User adminUser = User.builder().username("upstreamdevotion")
						// Password ko hash karke save karein
						.password(passwordEncoder.encode("P@ssw0rd@1234")).role(Role.ADMIN).build();
				userRepository.save(adminUser);
				System.out.println("Admin user created: upstreamdevotion / P@ssw0rd@1234");
				System.out.println("printing jwtkey " + jwtkeyString);
			}
		};
	}
}
