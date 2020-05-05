package com.ci6225.springboot.shoppingcart.model;

public class OrderDetailsInfo {
	private String id;
	private String puppyCode;
	private String puppyName;
	private String puppyGender;
	
	private double price;
	private int quantity;
	private double amount;
	
	public OrderDetailsInfo() {
		
	}
	
	

	public OrderDetailsInfo(String id, String puppyCode, String puppyName, String puppyGender, 
			int quantity, double price, double amount) {
		this.id = id;
		this.puppyCode = puppyCode;
		this.puppyName = puppyName;
		this.puppyGender = puppyGender;
		this.quantity = quantity;
		this.price = price;
		this.amount = amount;
	}



	public String getPuppyGender() {
		return puppyGender;
	}

	public void setPuppyGender(String puppyGender) {
		this.puppyGender = puppyGender;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPuppyCode() {
		return puppyCode;
	}

	public void setPuppyCode(String puppyCode) {
		this.puppyCode = puppyCode;
	}

	public String getPuppyName() {
		return puppyName;
	}

	public void setPuppyName(String puppyName) {
		this.puppyName = puppyName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	

}
