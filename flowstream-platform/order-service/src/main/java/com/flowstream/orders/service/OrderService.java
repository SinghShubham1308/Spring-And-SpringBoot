package com.flowstream.orders.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.flowstream.orders.client.InventoryClient;
import com.flowstream.orders.dto.InventoryReservationRequest;
import com.flowstream.orders.dto.OrderRequest;
import com.flowstream.orders.exception.InsufficientStockException;
import com.flowstream.orders.model.Order;
import com.flowstream.orders.model.OrderItem;
import com.flowstream.orders.model.OrderStatus;
import com.flowstream.orders.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SinghShubham1308
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final OrderRepository repository;
	private final InventoryClient client;

	public UUID createOrder(OrderRequest request) {
		log.info("[OrderService][createOrder] Creating order for customer with id: {}", request.getCustomerId());
		Order order = Order.builder().customerId(request.getCustomerId()).status(OrderStatus.CREATED)
				.totalAmount(BigDecimal.ZERO).build();
		BigDecimal calculatedTotal = BigDecimal.ZERO;
		for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
			String productId = itemRequest.getProductId();
			Integer quantity = itemRequest.getQuantity();
			BigDecimal currentPrice = fetchPriceFromInventory(itemRequest.getProductId());
			log.info("Reserving stock for Product: {} Qty: {}", productId, quantity);
			InventoryReservationRequest reservationDto = new InventoryReservationRequest(productId, quantity);

			boolean reserved = client.reserveStock(reservationDto);

			if (!reserved) {
				log.error("[OrderService][createOrder] Stock reservation failed for product: {}" + productId);
				throw new InsufficientStockException("Insufficient stock for product: " + productId);
			}

			OrderItem orderItem = OrderItem.builder().productId(itemRequest.getProductId())
					.quantity(itemRequest.getQuantity()).priceAtPurchase(currentPrice).order(order).build();

			order.addOrderItem(orderItem);

			BigDecimal itemTotal = currentPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
			calculatedTotal = calculatedTotal.add(itemTotal);
		}
		order.setTotalAmount(calculatedTotal);
		Order savedOrder = repository.save(order);
		log.info("[OrderService][createOrder] Order created successfully with ID: {}", savedOrder.getId());
		return savedOrder.getId();

	}

	private BigDecimal fetchPriceFromInventory(String productId) {
		return client.getPrice(productId);
	}

}
