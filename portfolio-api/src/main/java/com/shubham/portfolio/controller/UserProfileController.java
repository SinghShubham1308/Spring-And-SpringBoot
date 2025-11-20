package com.shubham.portfolio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubham.portfolio.dto.PortfolioDataDto;
import com.shubham.portfolio.service.UserProfileService;

/**
 * @author SinghShubham
 */
@RestController
@RequestMapping("/api/v1/profile-data")
public class UserProfileController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);
	private final UserProfileService service;

	public UserProfileController(UserProfileService service) {
		super();
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<PortfolioDataDto> getPortfolioData() {
		PortfolioDataDto data = service.getPortfolioData();
		LOGGER.info("Fetching portfolio data for user: {}");
		return ResponseEntity.ok(data);
	}

	@PutMapping
	public ResponseEntity<PortfolioDataDto> updatePortfolioData(@RequestBody PortfolioDataDto portfolioData) {
		PortfolioDataDto updatedData = service.insertUpdatePortfolioData(portfolioData);
		return ResponseEntity.ok(updatedData);
	}

}
