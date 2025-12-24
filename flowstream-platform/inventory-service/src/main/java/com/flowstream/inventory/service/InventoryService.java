package com.flowstream.inventory.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flowstream.inventory.model.Product;
import com.flowstream.inventory.model.Stock;
import com.flowstream.inventory.repository.ProductRepository;
import com.flowstream.inventory.repository.StockRepository;

import jakarta.transaction.Transactional;

/**
 * @author SinghShubham1308
 */
@Service
public class InventoryService {

	private final ProductRepository productRepository;
	private final StockRepository stockRepository;
	private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

	public InventoryService(ProductRepository productRepository, StockRepository stockRepository) {
		super();
		this.productRepository = productRepository;
		this.stockRepository = stockRepository;
	}

	public BigDecimal getPrice(String productId) {
		return productRepository.findById(productId).map(Product::getPrice)
				.orElseThrow(() -> new RuntimeException("Product not found " + productId));
	}

	public Integer getStockAvailability(String productId) {
		return stockRepository.findByProductId(productId).map(Stock::getAvailableQty).orElse(0);
	}

	@Transactional
	public boolean reserveStock(String productId, Integer quantity) {
		log.info("[InventoryService][reserveStock] Attempting to reserve {} items for product {}", quantity, productId);
		Stock stock = stockRepository.findByProductId(productId)
				.orElseThrow(() -> new RuntimeException("Stock record not found for: " + productId));
		if (stock.getAvailableQty() < quantity) {
			log.warn("[InventoryService][reserveStock] Insufficient stock for product {}. Requested: {}, Available: {}",
					productId, quantity, stock.getAvailableQty());
			return false;// throw custom InsufficientStockException
		}

		stock.setAvailableQty(stock.getAvailableQty() - quantity);
		stock.setReservedQty(stock.getReservedQty() + quantity);

		stockRepository.save(stock);
		log.info("[InventoryService][reserveStock] Stock reserved successfully. New Available: {}",
				stock.getAvailableQty());
		return true;
	}
	
	@Transactional
    public void seedData() {
//        if (productRepository.count() == 0) {
            Product p1 = Product.builder().id("PROD-003").name("wireless charger").sku("WC-89").price(new BigDecimal("1200.00")).build();
            Product p2 = Product.builder().id("PROD-004").name("Iphone").sku("IP-001").price(new BigDecimal("100000.00")).build();
            
            productRepository.save(p1);
            productRepository.save(p2);

            Stock s1 = Stock.builder().productId("PROD-003").availableQty(10).reservedQty(0).build();
            Stock s2 = Stock.builder().productId("PROD-004").availableQty(100).reservedQty(0).build();
            
            stockRepository.save(s1);
            stockRepository.save(s2);
            log.info("Seeded dummy data for Inventory");
//        }
    }

}
