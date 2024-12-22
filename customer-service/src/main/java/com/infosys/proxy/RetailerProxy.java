package com.infosys.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.infosys.model.Product;

//@FeignClient(name = "retailer-service", url = "http://localhost:8001")
@FeignClient(name = "retailer-service")
public interface RetailerProxy {
	@GetMapping("/products/")
	public ResponseEntity<List<Product>> getAllProducts();

	@PostMapping("products/{productId}/reduce-quantity/{quantity}")
	// products/{productId}/reduce-quantity/{quantity}
	public ResponseEntity<String> reduceProductQuantity(@PathVariable(name = "productId") long id,
			@PathVariable int quantity);

	@GetMapping("products/{id}")
	public ResponseEntity<Product> findProductById(@PathVariable Long id);
}
