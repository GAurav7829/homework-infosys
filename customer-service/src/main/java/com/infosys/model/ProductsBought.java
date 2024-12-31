package com.infosys.model;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
public class ProductsBought {
	@Id
	private long id;
	private Long productId;
	private double price;
	private int quantity;
	private LocalDateTime date;
	private double rewardPoints;
	
	public ProductsBought(Long productId, double price, int quantity, LocalDateTime date) {
		super();
		this.id = Math.abs(new Random().nextLong());
		this.productId = productId;
		this.price = price;
		this.quantity = quantity;
		this.date = date;
	}
	
}
