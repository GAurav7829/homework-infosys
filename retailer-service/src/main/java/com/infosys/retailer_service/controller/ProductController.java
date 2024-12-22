package com.infosys.retailer_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.retailer_service.model.Product;
import com.infosys.retailer_service.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	public ProductService service;

	@GetMapping("/")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> findAll = service.findAll();
		return ResponseEntity.ok(findAll);
	}

	@PostMapping("/addNewProduct")
	public ResponseEntity<Product> addNewProduct(@RequestBody Product product) {
		Product savedProduct = service.save(product);
		return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> findProductById(@PathVariable Long id) {
		Product product = service.findById(id);
		return ResponseEntity.ok(product);
	}

	@PostMapping("/{productId}/reduce-quantity/{quantity}")
	public ResponseEntity<String> reduceProductQuantity(@PathVariable(name = "productId") long id,
			@PathVariable int quantity) {
		String message = service.reduceQuantity(id, quantity);
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}
}
