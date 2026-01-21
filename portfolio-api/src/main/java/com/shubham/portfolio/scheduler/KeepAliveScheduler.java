package com.shubham.portfolio.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;

/**
 * @author SinghShubham
 */
@Component
@Log4j2
public class KeepAliveScheduler {

	// Replace with your actual Render backend URL
	private final String PING_URL = "https://portfolio-backend-0u1k.onrender.com/api/v1/ping";
	private final RestTemplate restTemplate = new RestTemplate();

	// Run every 5 minutes (5 * 60 * 1000 = 840000 ms) to be safe within the
	// 5-minute window
	@Scheduled(fixedRate = 300000)
	public void pingSelf() {
		try {
			log.info("Pinging self to keep alive: {}", PING_URL);
			String response = restTemplate.getForObject(PING_URL, String.class);
			log.info("Ping response: {}", response);
		} catch (Exception e) {
			log.error("Failed to ping self: {}", e.getMessage());
		}
	}
}