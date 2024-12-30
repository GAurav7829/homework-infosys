package com.infosys.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
public class ProductsBought {
	private Long productId;
	private double price;
	private int quantity;
	private LocalDateTime date;
	
	public ProductsBought(Long productId, double price, int quantity, LocalDateTime date) {
		super();
		this.productId = productId;
		this.price = price;
		this.quantity = quantity;
		this.date = date;
	}
	
}
