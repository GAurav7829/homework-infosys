package com.infosys.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	@Id
	private long id;
	private String customerName;
	private String userName;
	private List<ProductsBought> productsBought;
	private double cashBackPoints;
}
