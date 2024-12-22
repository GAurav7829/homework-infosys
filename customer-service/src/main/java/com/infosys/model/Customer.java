package com.infosys.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Customer {
	@Id
	private long id;
	private String customerName;
	private String userName;
	private List<Items> itemBought;
	private long cashBackPoints;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public List<Items> getItemBought() {
		return itemBought;
	}

	public void setItemBought(List<Items> itemBought) {
		this.itemBought = itemBought;
	}

	public long getCashBackPoints() {
		return cashBackPoints;
	}

	public void setCashBackPoints(long cashBackPoints) {
		this.cashBackPoints = cashBackPoints;
	}

}
