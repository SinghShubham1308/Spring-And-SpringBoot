package com.flowstream.orders.model;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SinghShubham1308
 */
@Entity
@Table(name = "order_items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "product_id", nullable = false)
	private String productId;

	@Column(nullable = false)
	private Integer quantity;

	@Column(name = "price_at_purchase", nullable = false, precision = 19, scale = 2)
	private BigDecimal priceAtPurchase;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
}
