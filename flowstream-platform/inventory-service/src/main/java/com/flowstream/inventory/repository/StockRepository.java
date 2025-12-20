package com.flowstream.inventory.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowstream.inventory.model.Stock;

/**
 * @author SinghShubham1308
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

	Optional<Stock> findByProductId(String productId);
}
