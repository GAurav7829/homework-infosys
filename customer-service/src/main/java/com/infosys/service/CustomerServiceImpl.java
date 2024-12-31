package com.infosys.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infosys.exception.InvalidMonthException;
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
			customer.setId(Math.abs(new Random().nextLong()));
		if (null == customer.getProductsBought())
			customer.setProductsBought(new ArrayList<>());
		Customer savedCustomer = repository.save(customer);
		return savedCustomer;
	}

	@Override
	public Customer buyProduct(Product product, long customerId, int quantity) {
		Customer customer = repository.findById(customerId).get();
		ProductsBought productBought = new ProductsBought();
		productBought.setId(Math.abs(new Random().nextLong()));
		productBought.setProductId(product.getId());
		productBought.setPrice(product.getPrice());
		productBought.setQuantity(quantity);
		productBought.setDate(LocalDateTime.now());

		double productPrice = product.getPrice();
		double cashBackPoints = 0;
		if (productPrice > 50 && productPrice <= 100) {
			cashBackPoints = ((productPrice - 50) * 1);
		} else if (productPrice > 100) {
			cashBackPoints = (productPrice - 100) * 2 + 50;
		}
		productBought.setRewardPoints(cashBackPoints);
		customer.getProductsBought().add(productBought);
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

	@Override
	public List<ProductsBought> getRecordForMonth(Long id, int month) {
		if (month <= 0)
			throw new InvalidMonthException("Invalid month.");
		if (month > 0 && month > 3)
			throw new InvalidMonthException("Only fetch records for last 3 months");
		Optional<Customer> customerById = repository.findById(id);
		if (customerById.isPresent()) {
			LocalDateTime today = LocalDateTime.now();
			LocalDateTime monthMinus = today.minusMonths(month);

			List<ProductsBought> list = customerById.get().getProductsBought().stream()
					.filter(i -> i.getDate().isAfter(monthMinus) && i.getDate().isBefore(today))
					.collect(Collectors.toList());
			return list;
		} else {
			throw new NotFoundException("Customer Not Found.");
		}
	}

}
