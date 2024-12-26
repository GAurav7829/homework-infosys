package com.infosys.retailer_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	private long id;
	private String product;
	private String retailerName;
	private double price;
	private int quantity;
}
