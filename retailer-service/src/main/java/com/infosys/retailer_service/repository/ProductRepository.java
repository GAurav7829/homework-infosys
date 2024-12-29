package com.infosys.retailer_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.infosys.retailer_service.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, Long> {

}
