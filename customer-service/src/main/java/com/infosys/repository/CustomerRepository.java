package com.infosys.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.infosys.model.Customer;

public interface CustomerRepository extends MongoRepository<Customer, Long> {
	Customer findByUserName(String userName);
}
