package com.infosys.retailer_service.service;

import java.util.List;

import com.infosys.retailer_service.model.Product;

public interface ProductService {
	
	Product save(Product product);
	List<Product> findAll();
	Product findById(Long id);
	String reduceQuantity(long id, int quantity);

}
