package com.emailfollowup.email_followup_app.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emailfollowup.email_followup_app.entitty.EmailTask;
import com.emailfollowup.email_followup_app.repository.EmailTaskRepository;

import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.FetchProfile;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @author SinghShubham1308
 */

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailFetchService {

	private final EmailTaskRepository emailTaskRepository;

	// Gmail IMAP Settings
	@Value("${HOST}")
	private String host;
	@Value("${PROTOCOL}")
	private String protocol;
	@Value("${USERNAME}")
	private String username;
	@Value("${PASSWORD}")
	private String password;

	public void fetchSentEmails() {
		Store store = null;
		Folder emailFolder = null;
		try {
			// 1. Properties set karte hain

			Properties properties = getProperties();

			// 2. Session create karte hain
			Session session = Session.getInstance(properties);

			// 3. Store (Mailbox) se connect karte hain
			store = session.getStore(protocol);
			store.connect(host, username, password);

			// 4. Folder open karte hain. Gmail mein Sent folder ka naam "[Gmail]/Sent Mail"
			// hota hai
			// Agar Inbox padhna ho toh bas "INBOX" likhein
			emailFolder = store.getFolder("[Gmail]/Sent Mail");
			emailFolder.open(Folder.READ_ONLY);

			// 5. Messages fetch karte hain (Latest 10 emails example ke liye)
			int messageCount = emailFolder.getMessageCount();
			if (messageCount == 0)
				return;
			int start = Math.max(1, messageCount - 9); // Last 10 emails

			Message[] messages = emailFolder.getMessages(start, messageCount);

			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			emailFolder.fetch(messages, fp);

			log.info("Saving last " + messages.length + " messages to Database...");

			for (Message message : messages) {
				try {
					String subject = message.getSubject();
					String recipient = convertAddressToString(message.getRecipients(Message.RecipientType.TO));
					String body = getTextFromMessage(message);
					LocalDateTime sentDate = LocalDateTime.ofInstant(message.getSentDate().toInstant(),
							ZoneId.systemDefault());
					boolean exists = emailTaskRepository.findAll().stream()
							.anyMatch(task -> task.getSubject() != null && task.getSubject().equals(subject)
									&& task.getRecipient() != null && task.getRecipient().equals(recipient));

					if (!exists) {
						EmailTask task = new EmailTask();
						task.setSubject(subject);
						task.setRecipient(recipient);
						// Body agar bahut badi hai toh database error de sakta hai, isliye truncate kar
						// rahe hain
						task.setBody(body.length() > 250 ? body.substring(0, 250) + "..." : body);
						task.setCreatedAt(sentDate);
						task.setStatus("SENT"); // Status SENT set kar diya

						emailTaskRepository.save(task);
						log.info("Saved: " + subject);
					} else {
						log.info("Skipped Duplicate: " + subject);
					}

				} catch (Exception e) {
					log.error("Error processing message: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (emailFolder != null && emailFolder.isOpen())
					emailFolder.close(false);
				if (store != null && store.isConnected())
					store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public void checkInboxForReplies() {
		Store store = null;
		Folder inbox = null;
		try {
			Properties properties = getProperties();

			// Connection Timeout Settings (Taaki jaldi fail na ho)
			properties.put("mail.imaps.connectiontimeout", "10000");
			properties.put("mail.imaps.timeout", "10000");
			Session session = Session.getDefaultInstance(properties);
			store = session.getStore(protocol);
			store.connect(host, username, password);

			// Is baar hum INBOX open karenge
			inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			// Last 20 messages check karte hain performance ke liye
			int messageCount = inbox.getMessageCount();
			if (messageCount == 0)
				return;
			int start = Math.max(1, messageCount - 19);
			Message[] messages = inbox.getMessages(start, messageCount);

			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE); // Subjects, Dates, From pre-load ho jayenge
			inbox.fetch(messages, fp);

			log.info("Checking INBOX for replies...");

			List<EmailTask> tasks = emailTaskRepository.findAll();

			for (Message message : messages) {
				try {
					String subject = message.getSubject();

					// Check 1: Kya ye reply hai? (Subject starts with Re:)
					if (subject == null)
						continue;

					// Check 2: Kisne bheja? (Sender should match our Task Recipient)
					String sender = convertAddressToString(message.getFrom());
					String incomingCleanSubject = cleanSubject(subject);
					log.info("Found Reply Candidate: '" + subject + "' from " + sender);

					for (EmailTask task : tasks) {

						if (task.getSubject() != null && task.getRecipient() != null) {
							boolean recipientMatch = sender.contains(task.getRecipient().toLowerCase());

							String taskCleanSubject = cleanSubject(task.getSubject());
							boolean subjectMatch = incomingCleanSubject.contains(taskCleanSubject) || 
                                    taskCleanSubject.contains(incomingCleanSubject);
               
							if (subjectMatch && recipientMatch && !task.getStatus().equals("REPLIED")) {
								task.setStatus("REPLIED");
								emailTaskRepository.save(task);
								log.info("MATCH FOUND! Task ID " + task.getId() + " marked as REPLIED.");
							}
						}
					}

				} catch (Exception e) {
					// Agar ek message fail ho, toh baaki continue rahenge
					log.error("Skipping a message due to error: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Safely close connection
			try {
				if (inbox != null && inbox.isOpen())
					inbox.close(false);
				if (store != null && store.isConnected())
					store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	private String cleanSubject(String subject) {
		if (subject == null)
			return "";
		return subject.toLowerCase().replace("re:", "").replace("fwd:", "").replace("reply:", "").trim();
	}

	// Helper method to convert Address array to String
	private String convertAddressToString(Address[] addresses) {
		if (addresses == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (Address addr : addresses) {
			sb.append(addr.toString()).append(", ");
		}
		return sb.toString();
	}

	private String getTextFromMessage(Message message) throws MessagingException, IOException {
		if (message.isMimeType("text/plain")) {
			return message.getContent().toString();
		} else if (message.isMimeType("multipart/*")) {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			return getTextFromMimeMultipart(mimeMultipart);
		}
		return "";
	}

	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		StringBuilder result = new StringBuilder();
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result.append(bodyPart.getContent());
				break; // Text mil gaya toh loop tod do, HTML nahi chahiye abhi
			} else if (bodyPart.getContent() instanceof MimeMultipart mimemultipart) {
				result.append(getTextFromMimeMultipart(mimemultipart));
			}
		}
		return result.toString();
	}

	private Properties getProperties() {
		Properties properties = new Properties();
		properties.put("mail.store.protocol", protocol);
		properties.put("mail.imaps.host", host);
		properties.put("mail.imaps.port", "993");
		properties.put("mail.imaps.ssl.enable", "true");
		return properties;
	}
}
