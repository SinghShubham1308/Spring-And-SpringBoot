package com.flowstream.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SinghShubham1308
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockReservationRequest {
    private String productId;
    private Integer quantity;
}
