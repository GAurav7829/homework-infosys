package com.infosys.retailer_service.service;

import static com.infosys.retailer_service.utils.AppConstants.PRODUCT_QUANTITY_IS_GREATER_THAN_QUANTITY_ASKED;
import static com.infosys.retailer_service.utils.AppConstants.QUANTITY_REDUCED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.infosys.retailer_service.exception.NotFoundException;
import com.infosys.retailer_service.model.Product;
import com.infosys.retailer_service.repository.ProductRepository;

public class ProductServiceTest {
	@InjectMocks
	private ProductServiceImpl productService;
	@Mock
	private ProductRepository repository;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
    void testSave() {
        Product product = new Product();
        product.setProduct("Test Product");

        Product savedProduct = new Product();
        savedProduct.setId(new Random().nextLong());
        savedProduct.setProduct("Test Product");

        when(repository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.save(product);

        assertNotNull(result);
        assertEquals("Test Product", result.getProduct());
        verify(repository, times(1)).save(product);
    }
	
	@Test
    void testFindAll() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(repository.findAll()).thenReturn(products);

        List<Product> result = productService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }
	
	@Test
    void testFindById_ProductFound() {
        Product product = new Product();
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(1L);
    }
	
	@Test
    void testFindById_ProductNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.findById(1L));
        verify(repository, times(1)).findById(1L);
    }
	
	@Test
    void testReduceQuantity_InsufficientQuantity() {
        Product product = new Product();
        product.setId(1L);
        product.setQuantity(5);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        String result = productService.reduceQuantity(1L, 10);

        assertEquals(PRODUCT_QUANTITY_IS_GREATER_THAN_QUANTITY_ASKED, result);
        verify(repository, never()).save(any(Product.class));
    }

	@Test
    void testReduceQuantity_SufficientQuantity() {
        Product product = new Product();
        product.setId(1L);
        product.setQuantity(10);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        String result = productService.reduceQuantity(1L, 5);

        assertEquals(QUANTITY_REDUCED, result);
        assertEquals(5, product.getQuantity());
        verify(repository, times(1)).save(product);
    }
}
