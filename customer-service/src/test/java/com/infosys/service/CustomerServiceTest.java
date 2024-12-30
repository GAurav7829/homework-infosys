package com.infosys.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.infosys.exception.NotFoundException;
import com.infosys.model.Customer;
import com.infosys.model.Product;
import com.infosys.model.ProductsBought;
import com.infosys.repository.CustomerRepository;

public class CustomerServiceTest {

	@InjectMocks
	private CustomerServiceImpl customerService;

	@Mock
	private CustomerRepository repository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRegisterCustomer_NewCustomer() {
		Customer customer = new Customer();
		customer.setId(0);
		customer.setProductsBought(null);

		Customer savedCustomer = new Customer();
		savedCustomer.setId(new Random().nextLong());
		savedCustomer.setProductsBought(new ArrayList<>());

		when(repository.save(any(Customer.class))).thenReturn(savedCustomer);

		Customer result = customerService.registerCustomer(customer);

		assertNotNull(result);
		assertNotEquals(0, result.getId());
		assertNotNull(result.getProductsBought());
		verify(repository, times(1)).save(customer);
	}

	@Test
	void testBuyProduct() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setProductsBought(new ArrayList<>());
		customer.setCashBackPoints(0);

		Product product = new Product();
		product.setId(1L);
		product.setPrice(120);
		product.setQuantity(1);

		when(repository.findById(1L)).thenReturn(Optional.of(customer));

		Customer result = customerService.buyProduct(product, 1L);

		assertNotNull(result);
		assertEquals(1, result.getProductsBought().size());
		assertEquals(90, result.getCashBackPoints());
		verify(repository, times(1)).save(customer);
	}

	@Test
	void testBuyProductWithCashBackPoints() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setProductsBought(new ArrayList<>());
		customer.setCashBackPoints(0);

		Product product = new Product();
		product.setId(2L);
		product.setPrice(60); // Product price between 50 and 100
		product.setQuantity(1);

		when(repository.findById(1L)).thenReturn(Optional.of(customer));

		Customer result = customerService.buyProduct(product, 1L);

		assertNotNull(result);
		assertEquals(1, result.getProductsBought().size());
		assertEquals(10, result.getCashBackPoints());
		assertEquals(result.getCashBackPoints(), 10);
		verify(repository, times(1)).save(customer);
	}

	@Test
	void testFindAllCustomer() {
		List<Customer> customers = Arrays.asList(new Customer(), new Customer());
		when(repository.findAll()).thenReturn(customers);

		List<Customer> result = customerService.findAllCustomer();

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(repository, times(1)).findAll();
	}

	@Test
	void testGetLastThreeTransaction_Success() {
		Customer customer = new Customer();
		customer.setId(1L);
		List<ProductsBought> products = Arrays.asList(
				new ProductsBought(1L, 100, 2, LocalDateTime.now().minusMonths(2)),
				new ProductsBought(2L, 50, 1, LocalDateTime.now().minusMonths(4)));
		customer.setProductsBought(products);

		when(repository.findById(1L)).thenReturn(Optional.of(customer));

		List<ProductsBought> result = customerService.getLastThreeTransaction(1L);

		assertNotNull(result);
		assertEquals(1, result.size());
		verify(repository, times(1)).findById(1L);
	}

	@Test
    void testGetLastThreeTransaction_CustomerNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.getLastThreeTransaction(1L));
        verify(repository, times(1)).findById(1L);
    }

}
