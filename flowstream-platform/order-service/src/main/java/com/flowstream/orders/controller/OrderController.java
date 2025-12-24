package com.flowstream.orders.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowstream.orders.dto.OrderRequest;
import com.flowstream.orders.service.OrderService;

/**
 * @author SinghShubham1308
 */
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

	private OrderService service;

	public OrderController(OrderService service) {
		super();
		this.service = service;
	}
	@PostMapping
	public ResponseEntity<UUID> createOrder(@RequestBody OrderRequest request){
		UUID order = service.createOrder(request);
		return new ResponseEntity<>(order,HttpStatus.CREATED);
	}
		
}
