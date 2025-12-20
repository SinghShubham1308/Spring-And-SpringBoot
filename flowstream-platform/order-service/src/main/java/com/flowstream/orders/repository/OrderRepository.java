package com.flowstream.orders.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.flowstream.orders.model.Order;
import com.flowstream.orders.model.OrderStatus;

/**
 * @author SinghShubham1308
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>{
	List<Order> findByCustomerId(UUID customerId);
	
	List<Order> findByStatus(OrderStatus status);
	
	Optional<Order> findByIdAndCustomerId(UUID id, UUID customerId);
	
	@Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.customerId = :customerId AND o.status = 'CONFIRMED'")
    BigDecimal calculateTotalSpentByCustomer(@Param("customerId") UUID customerId);

}
