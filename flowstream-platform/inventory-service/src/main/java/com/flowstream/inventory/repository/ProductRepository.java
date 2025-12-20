package com.flowstream.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowstream.inventory.model.Product;

/**
 * @author SinghShubham1308
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, String>{

}
