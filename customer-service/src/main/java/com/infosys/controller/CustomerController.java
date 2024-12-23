package com.infosys.controller;

import static com.infosys.utils.AppConstants.ITEM_BOUGHT;

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

import com.infosys.model.Customer;
import com.infosys.model.Product;
import com.infosys.proxy.RetailerProxy;
import com.infosys.service.CustomerService;
import com.infosys.utils.AppConstants;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private RetailerProxy proxy;

	@GetMapping("/show-all-product")
	public List<Product> getAllProduct() {
		List<Product> productList = proxy.getAllProducts().getBody();
		return productList;
	}

	@PostMapping("/register-customer")
	public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
		Customer registerCustomer = customerService.registerCustomer(customer);
		return new ResponseEntity<String>("Custmer is Registered with id: " + registerCustomer.getId(),
				HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Customer>> findAllCustomer(){
		List<Customer> customers = customerService.findAllCustomer();
		return ResponseEntity.ok(customers);
	}
	
	@PostMapping("{customerId}/buy-product/{productId}/{quantity}")
	public ResponseEntity<String> buyProduct(@PathVariable long customerId, @PathVariable long productId,
			@PathVariable int quantity) {
		///{productId}/reduce-quantity/{quantity}
		ResponseEntity<String> responseEntity = proxy.reduceProductQuantity(productId, quantity);
		String message = responseEntity.getBody();
		if (message.equals(AppConstants.QUANTITY_REDUCED)) {
			ResponseEntity<Product> productResponseEntity = proxy.findProductById(productId);
			Product product = productResponseEntity.getBody();
			customerService.buyProduct(product, customerId);
			return ResponseEntity.ok(ITEM_BOUGHT);
		} else
			return new ResponseEntity<String>(message, HttpStatus.OK);

	}
}
