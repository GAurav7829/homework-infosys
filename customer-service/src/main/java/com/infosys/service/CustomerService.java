package com.infosys.service;

import java.util.List;

import com.infosys.model.Customer;
import com.infosys.model.ProductsBought;
import com.infosys.model.Product;

public interface CustomerService {
	List<Customer> findAllCustomer();
	Customer registerCustomer(Customer customer);
	Customer buyProduct(Product product, long customerId);
	List<ProductsBought> getLastThreeTransaction(Long id);
	
}
