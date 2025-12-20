package com.flowstream.orders.model;

/**
 * @author SinghShubham1308
 */
public enum OrderStatus {
	CREATED,    // Order initialized, items in cart
    PENDING,    // Payment processing
    CONFIRMED,  // Payment success, stock reserved
    SHIPPED,    // With logistics
    CANCELLED   // Payment failed or user cancelled
}
