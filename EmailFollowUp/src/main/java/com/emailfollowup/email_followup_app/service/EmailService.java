package com.emailfollowup.email_followup_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @author SinghShubham1308
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;

	@Value("${SENDER_EMAIL}")
	private  String senderEmail;


	public void sendEmail(String to, String subject, String body) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();

			// YE LINE SABSE IMPORTANT HAI:
			// Yahan wo email aayega jo Brevo mein "Sender" list mein verified hai.
			// Receiver ko yahi email dikhega "From" mein.
			message.setFrom(senderEmail);

			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);

			javaMailSender.send(message);

			log.info("Email sent successfully to: " + to);
		} catch (Exception e) {
			log.error("Error sending email: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
