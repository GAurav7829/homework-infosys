package com.infosys.retailer_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.retailer_service.exception.NotFoundException;
import com.infosys.retailer_service.model.Product;
import com.infosys.retailer_service.service.ProductService;

public class ProductControllerTest {
	@InjectMocks
	private ProductController productController;

	@Mock
	private ProductService productService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
	}

	@Test
	void testGetAllProducts() throws Exception {
		List<Product> products = Arrays.asList(new Product(1L, "Product1", "Retailer1", 5000, 10),
				new Product(2L, "Product2", "Retailer2", 1000, 5));
		when(productService.findAll()).thenReturn(products);

		mockMvc.perform(get("/products/")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].product").value("Product1"));

		verify(productService, times(1)).findAll();
	}

	@Test
	void testAddNewProduct() throws Exception {
		Product product = new Product(0L, "Laptop", "Retailer1", 10000, 20);
		Product savedProduct = new Product(1L, "Laptop", "Retailer1", 10000, 20);

		when(productService.save(any(Product.class))).thenReturn(savedProduct);

		mockMvc.perform(post("/products/addNewProduct").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(product))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.product").value("Laptop"));

		verify(productService, times(1)).save(any(Product.class));
	}

	@Test
	void testFindProductById_Success() throws Exception {
		Product product = new Product(1L, "Product1", "Retailer1", 5000, 10);
		when(productService.findById(1L)).thenReturn(product);

		mockMvc.perform(get("/products/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.product").value("Product1"));

		verify(productService, times(1)).findById(1L);
	}

	@Test
    void testFindProductById_NotFound() throws Exception {
        when(productService.findById(1L)).thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));

        verify(productService, times(1)).findById(1L);
    }

	@Test
    void testReduceProductQuantity_Success() throws Exception {
        when(productService.reduceQuantity(1L, 5)).thenReturn("Quantity reduced successfully");

        mockMvc.perform(post("/products/1/reduce-quantity/5"))
                .andExpect(status().isOk());

        verify(productService, times(1)).reduceQuantity(1L, 5);
	}
}
