package com.infosys.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsBought {
	private Long id;
	private double price;
	private int quantity;
	private LocalDateTime date;
	
}
