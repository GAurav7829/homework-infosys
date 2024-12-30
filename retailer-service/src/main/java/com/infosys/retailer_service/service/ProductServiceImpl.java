package com.infosys.retailer_service.service;

import static com.infosys.retailer_service.utils.AppConstants.PRODUCT_QUANTITY_IS_GREATER_THAN_QUANTITY_ASKED;
import static com.infosys.retailer_service.utils.AppConstants.QUANTITY_REDUCED;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infosys.retailer_service.exception.NotFoundException;
import com.infosys.retailer_service.model.Product;
import com.infosys.retailer_service.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository repository;

	@Override
	public Product save(Product product) {
		product.setId(Math.abs(new Random().nextLong()));
		Product savedProduct = repository.save(product);
		return savedProduct;
	}

	@Override
	public List<Product> findAll() {
		List<Product> list = repository.findAll();
		return list;
	}

	@Override
	public Product findById(Long id) {
		Optional<Product> findById = repository.findById(id);
		return findById.orElseThrow(() -> new NotFoundException("Product not found"));
	}

	@Override
	public String reduceQuantity(long id, int quantity) {
		Product product = findById(id);
		if (product.getQuantity() < quantity) {
			return PRODUCT_QUANTITY_IS_GREATER_THAN_QUANTITY_ASKED;
		} else {
			product.setQuantity(product.getQuantity() - quantity);
			repository.save(product);
			return QUANTITY_REDUCED;
		}
	}

}
