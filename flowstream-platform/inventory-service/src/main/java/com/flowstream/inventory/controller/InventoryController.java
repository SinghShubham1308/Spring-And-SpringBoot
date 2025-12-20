package com.flowstream.inventory.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowstream.inventory.dto.StockReservationRequest;
import com.flowstream.inventory.service.InventoryService;

/**
 * @author SinghShubham1308
 */
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

	private final InventoryService service;

	public InventoryController(InventoryService service) {
		super();
		this.service = service;
	}

	@GetMapping("/{productId}/price")
	public ResponseEntity<BigDecimal> getPrice(@PathVariable String productId) {
		return ResponseEntity.ok(service.getPrice(productId));
	}

	@PostMapping("/seed")
	public ResponseEntity<String> seedData() {
		service.seedData();
		return ResponseEntity.ok("Inventory Data Seeded");
	}

	@PostMapping("/reservation")
	public ResponseEntity<Boolean> reserveStock(@RequestBody StockReservationRequest request) {
		boolean success = service.reserveStock(request.getProductId(), request.getQuantity());

		if (success) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.badRequest().body(false);
		}
	}
}
