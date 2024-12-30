package com.infosys.controller;

import static com.infosys.utils.AppConstants.PRODUCT_QUANTITY_IS_GREATER_THAN_QUANTITY_ASKED;
import static com.infosys.utils.AppConstants.QUANTITY_REDUCED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.exception.InvalidMonthException;
import com.infosys.exception.NotFoundException;
import com.infosys.model.Customer;
import com.infosys.model.Product;
import com.infosys.model.ProductsBought;
import com.infosys.proxy.RetailerProxy;
import com.infosys.service.CustomerService;

public class CustomerControllerTest {

	@InjectMocks
	private CustomerController customerController;

	@Mock
	private CustomerService customerService;

	@Mock
	private RetailerProxy proxy;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
	}

	@Test
	void testGetAllProduct() throws Exception {
		List<Product> products = Arrays.asList(new Product(1L, "Product1", "Retailer1", 50, 10),
				new Product(2L, "Product2", "Retailer2", 100, 5));
		when(proxy.getAllProducts()).thenReturn(ResponseEntity.ok(products));

		mockMvc.perform(get("/customer/show-all-product")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2));

		verify(proxy, times(1)).getAllProducts();
	}

	@Test
	void testRegisterCustomer() throws Exception {
		Customer customer = new Customer(0L, "gaurav", "grv", new ArrayList<ProductsBought>(), 0);
		Customer registeredCustomer = new Customer(1L, "saurav", "srv", new ArrayList<ProductsBought>(), 0);

		when(customerService.registerCustomer(any(Customer.class))).thenReturn(registeredCustomer);

		mockMvc.perform(post("/customer/register-customer").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(customer))).andExpect(status().isCreated());

		verify(customerService, times(1)).registerCustomer(any(Customer.class));
	}

	@Test
	void testFindAllCustomer() throws Exception {
		List<Customer> customers = Arrays.asList(new Customer(1L, "Gaurav", "grv", new ArrayList<>(), 0));
		when(customerService.findAllCustomer()).thenReturn(customers);

		mockMvc.perform(get("/customer")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
				.andExpect(jsonPath("$[0].customerName").value("Gaurav"));

		verify(customerService, times(1)).findAllCustomer();
	}

	@Test
    void testBuyProduct_Success() throws Exception {
        when(proxy.reduceProductQuantity(1L, 2)).thenReturn(ResponseEntity.ok(QUANTITY_REDUCED));
        Product product = new Product(1L, "Product1","Retailer1", 5000, 2);
        when(proxy.findProductById(1L)).thenReturn(ResponseEntity.ok(product));

        mockMvc.perform(post("/customer/1/buy-product/1/2"))
                .andExpect(status().isOk());

        verify(proxy, times(1)).reduceProductQuantity(1L, 2);
        verify(proxy, times(1)).findProductById(1L);
        verify(customerService, times(1)).buyProduct(product, 1L, 2);
    }

	@Test
    void testBuyProduct_Failure() throws Exception {
        when(proxy.reduceProductQuantity(1L, 2)).thenReturn(ResponseEntity.ok(PRODUCT_QUANTITY_IS_GREATER_THAN_QUANTITY_ASKED));

        mockMvc.perform(post("/customer/1/buy-product/1/2"))
                .andExpect(status().isOk());

        verify(proxy, times(1)).reduceProductQuantity(1L, 2);
        verify(proxy, never()).findProductById(anyLong());
    }

	@Test
	void testGetLastThreeMonthsTransaction_Success() throws Exception {
		List<ProductsBought> transactions = Arrays
				.asList(new ProductsBought(1L, 100, 2, LocalDateTime.now().minusMonths(2)));
		when(customerService.getLastThreeTransaction(1L)).thenReturn(transactions);

		mockMvc.perform(get("/customer/1/lastThreeMonthsTransaction")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$[0].price").value(100));

		verify(customerService, times(1)).getLastThreeTransaction(1L);
	}

	@Test
    void testGetLastThreeMonthsTransaction_NotFound() throws Exception {
        when(customerService.getLastThreeTransaction(1L)).thenThrow(new NotFoundException("Customer Not Found"));

        mockMvc.perform(get("/customer/1/lastThreeMonthsTransaction"))
                .andExpect(status().isNotFound());

    }

	@Test
	void testGetMonthWiseProductBought_InvalidMonth() throws Exception {
		doThrow(new InvalidMonthException("Only fetch records for last 3 months")).when(customerService)
				.getRecordForMonth(1L, 4);
		mockMvc.perform(get("/customers/1/month/4")).andExpect(status().is(404));
	}

	@Test
	void testGetMonthWiseProductBought_CustomerNotFound() throws Exception {
		doThrow(new NotFoundException("Customer Not Found.")).when(customerService).getRecordForMonth(1L, 2);
		mockMvc.perform(get("/customers/1/month/2")).andExpect(status().isNotFound());
	}
}
