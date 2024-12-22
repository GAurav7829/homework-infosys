package com.infosys.retailer_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.infosys.retailer_service.model.Product;

public interface ProductRepository extends MongoRepository<Product, Long> {

}
