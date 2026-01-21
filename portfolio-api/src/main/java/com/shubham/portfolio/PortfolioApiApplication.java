package com.shubham.portfolio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shubham.portfolio.model.Role;
import com.shubham.portfolio.model.User;
import com.shubham.portfolio.repository.ProjectRepository;
import com.shubham.portfolio.repository.UserRepository;

@SpringBootApplication
@EnableScheduling
public class PortfolioApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(PortfolioApiApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ProjectRepository repository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {

			if (userRepository.findByUsername("upstreamdevotion").isEmpty()) {
				User adminUser = User.builder().username("upstreamdevotion")

						.password(passwordEncoder.encode("P@ssw0rd@1234")).role(Role.ADMIN).build();
				userRepository.save(adminUser);
//				System.out.println("Admin user created: upstreamdevotion / P@ssw0rd@1234");
			}
		};
	}
}
