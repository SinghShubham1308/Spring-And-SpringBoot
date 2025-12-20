package com.flowstream.inventory.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SinghShubham1308
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

	@Id
	@Column(name = "id",nullable = false)
	private String id;
	
	@Column(nullable = false,unique = true)
	private String sku;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
	
}
