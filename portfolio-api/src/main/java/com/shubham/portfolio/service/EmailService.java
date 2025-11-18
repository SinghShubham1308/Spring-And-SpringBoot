package com.shubham.portfolio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.shubham.portfolio.model.ContactRequest;

/**
 * @author SinghShubham
 */
@Service
public class EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	private JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}

	@Value("${admin.email}") // Ise properties se laayein
	private String myEmailAddress;

	// Yeh woh email hai jo aapne ABHI Brevo mein validate kiya hai
	@Value("${validated.email}") // Hum Brevo ka login email hi use kar sakte hain
	private String validatedBrevoSender;

	public void sendContactEmail(ContactRequest contactRequest) {
		try {
			SimpleMailMessage messageToAdmin = new SimpleMailMessage();

			// --- YEH HAIN UPDATES ---

			// 1. FROM: Hamesha validated email hona chahiye
			messageToAdmin.setFrom(validatedBrevoSender);

			// 2. TO: Aapka personal email
			messageToAdmin.setTo(myEmailAddress);

			// 3. REPLY-TO: User ka email (taaki aap direct reply kar sakein)
			messageToAdmin.setReplyTo(contactRequest.getEmail());

			// 4. SUBJECT:
			messageToAdmin.setSubject("New Portfolio Contact: " + contactRequest.getName());

			// 5. BODY:
			String emailBody = String.format(
					"You received a new message from your portfolio contact form:\n\n" + "From: %s\n" + "Email: %s\n\n"
							+ "Message:\n%s",
					contactRequest.getName(), contactRequest.getEmail(), contactRequest.getMessage());

			messageToAdmin.setText(emailBody);

			mailSender.send(messageToAdmin);
			LOGGER.info("Contact email sent successfully (from: {}, reply-to: {})", validatedBrevoSender,
					contactRequest.getEmail());

		} catch (Exception e) {
			LOGGER.error("Error sending contact email: {}", e.getMessage(), e);
			// Exception ko re-throw karein taaki controller ko pata chale
			throw new RuntimeException("Failed to send email", e);
		}
	}
}
