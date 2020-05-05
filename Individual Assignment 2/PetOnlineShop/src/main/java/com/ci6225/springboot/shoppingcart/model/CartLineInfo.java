package com.ci6225.springboot.shoppingcart.model;

import com.ci6225.springboot.shoppingcart.model.ProductInfo;

public class CartLineInfo {
	
	private ProductInfo productInfo;
	private int quantity;
	
	public CartLineInfo() {
		this.quantity = 0;
	}

	public ProductInfo getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(ProductInfo productInfo) {
		this.productInfo = productInfo;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return productInfo.getPuppyPrice() * this.quantity;
	}


}
