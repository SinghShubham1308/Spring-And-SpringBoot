package com.flowstream.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author SinghShubham1308
 */
@Data
@AllArgsConstructor
public class InventoryReservationRequest {
	private String productId;
    private Integer quantity;
}
