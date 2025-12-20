package com.flowstream.orders.client;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.flowstream.orders.dto.InventoryReservationRequest;

/**
 * @author SinghShubham1308
 */
@FeignClient(name = "inventory-service", url = "http://localhost:9091") 
public interface InventoryClient {
	@GetMapping("/api/v1/inventory/{productId}/price")
    BigDecimal getPrice(@PathVariable String productId);
	
	@PostMapping("/api/v1/inventory/reservation")
    boolean reserveStock(@RequestBody InventoryReservationRequest request);
}
