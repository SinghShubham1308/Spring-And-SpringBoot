package com.emailfollowup.email_followup_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class EmailFollowUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailFollowUpApplication.class, args);
	}

}

@RestController
class HealthCheckController {
    
    @GetMapping("/health")
    public String check() {
        return "Email FollowUp App is running! Setup phase complete.";
    }
}