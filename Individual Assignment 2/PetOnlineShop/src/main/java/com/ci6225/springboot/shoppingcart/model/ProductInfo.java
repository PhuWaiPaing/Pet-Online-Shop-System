package com.ci6225.springboot.shoppingcart.model;

import com.ci6225.springboot.shoppingcart.entity.Puppy;

public class ProductInfo {
	
	private String puppyCode;
	private String puppyName;
	private double puppyPrice;
	private String puppyGender;


	public ProductInfo() { 
		}
	 

	public ProductInfo(String puppyCode, String puppyName, String puppyGender, double puppyPrice) {
		super();
		this.puppyCode = puppyCode;
		this.puppyName = puppyName;
		this.puppyGender = puppyGender;
		this.puppyPrice = puppyPrice;
	}
	public ProductInfo(Puppy puppy) {
		this.puppyCode=puppy.getCode();
		this.puppyName=puppy.getName();
		this.puppyGender=puppy.getGender();
		this.puppyPrice=puppy.getPrice();
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
	
	public String getPuppyGender() {
		return puppyGender;
	}


	public void setPuppyGender(String puppyGender) {
		this.puppyGender = puppyGender;
	}


	public double getPuppyPrice() {
		return puppyPrice;
	}

	public void setPuppyPrice(double puppyPrice) {
		this.puppyPrice = puppyPrice;
	}
}
