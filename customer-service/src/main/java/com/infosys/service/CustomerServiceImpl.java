package com.infosys.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infosys.exception.NotFoundException;
import com.infosys.model.Customer;
import com.infosys.model.Product;
import com.infosys.model.ProductsBought;
import com.infosys.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository repository;

	@Override
	public Customer registerCustomer(Customer customer) {
		if (customer.getId() == 0)
			customer.setId(new Random().nextLong());
		if (null == customer.getProductsBought())
			customer.setProductsBought(new ArrayList<>());
		Customer savedCustomer = repository.save(customer);
		return savedCustomer;
	}

	@Override
	public Customer buyProduct(Product product, long customerId) {
		Customer customer = repository.findById(customerId).get();
		ProductsBought productBought = new ProductsBought();
		productBought.setProductId(product.getId());
		productBought.setPrice(product.getPrice());
		productBought.setQuantity(product.getQuantity());
		customer.getProductsBought().add(productBought);
		double productPrice = product.getPrice();
		double cashBackPoints = 0;
		if (productPrice >= 50 && productPrice < 100) {
			if (productPrice == 50)
				cashBackPoints = 1;
			else
				cashBackPoints = productPrice - 50;
		} else if (productPrice >= 100) {
			if (productPrice == 100)
				cashBackPoints = 2;
			else
				cashBackPoints = (productPrice - 100) * 2 + 50;
		}
		customer.setCashBackPoints(customer.getCashBackPoints() + cashBackPoints);
		repository.save(customer);
		return customer;
	}

	@Override
	public List<Customer> findAllCustomer() {
		return repository.findAll();
	}

	@Override
	public List<ProductsBought> getLastThreeTransaction(Long id) {
		Optional<Customer> customerById = repository.findById(id);
		if (customerById.isPresent()) {
			LocalDateTime today = LocalDateTime.now();
			LocalDateTime threeMonthsAgo = today.minusMonths(3);

			List<ProductsBought> list = customerById.get().getProductsBought().stream()
					.filter(i -> i.getDate().isAfter(threeMonthsAgo) && i.getDate().isBefore(today))
					.collect(Collectors.toList());
			return list;
		} else {
			throw new NotFoundException("Customer Not Found.");
		}
	}

}
