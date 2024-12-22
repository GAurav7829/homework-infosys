package com.infosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infosys.model.Customer;
import com.infosys.model.Items;
import com.infosys.model.Product;
import com.infosys.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository repository;

	@Override
	public Customer registerCustomer(Customer customer) {
		if (customer.getId() == 0)
			customer.setId(new Random().nextLong());
		if (null == customer.getItemBought())
			customer.setItemBought(new ArrayList<>());
		Customer savedCustomer = repository.save(customer);
		return savedCustomer;
	}

	@Override
	public Customer buyProduct(Product product, long customerId) {
		Customer customer = repository.findById(customerId).get();
		if (null == customer.getItemBought()) {
			customer.setItemBought(new ArrayList<>());
		}
		Items item = new Items();
		item.setId(product.getId());
		item.setPrice(product.getPrice());
		item.setQuantity(product.getQuantity());
		customer.getItemBought().add(item);
		repository.save(customer);
		return customer;
	}

	@Override
	public List<Customer> findAllCustomer() {
		return repository.findAll();
	}

}
