package com.flowstream.orders.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

/**
 * @author SinghShubham1308
 */
@Data
public class OrderRequest {
	private UUID customerId;
	private List<OrderItemRequest> items;
	
	@Data
    public static class OrderItemRequest {
        private String productId;
        private Integer quantity;
    }
}
