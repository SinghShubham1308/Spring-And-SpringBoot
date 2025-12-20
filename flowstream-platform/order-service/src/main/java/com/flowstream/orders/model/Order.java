package com.flowstream.orders.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SinghShubham1308
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "customer_id", nullable = false)
	private UUID customerId;

	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	private OrderStatus status;

	@Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
	private BigDecimal totalAmount;

	@Version
	private Long version;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<OrderItem> items = new ArrayList<>();

	public void addOrderItem(OrderItem item) {
		items.add(item);
		item.setOrder(this);
	}

	public void removeOrderItem(OrderItem item) {
		items.remove(item);
		item.setOrder(null);
	}

}
