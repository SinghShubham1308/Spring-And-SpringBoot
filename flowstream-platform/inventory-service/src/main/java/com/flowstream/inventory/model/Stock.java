package com.flowstream.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "stock")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "product_id", nullable = false)
	private String productId;

	@Column(name = "available_qty", nullable = false)
	private Integer availableQty;
	@Column(name = "reseved_qty", nullable = false)
	private Integer reservedQty;

	@Version
	private Long version;

}
