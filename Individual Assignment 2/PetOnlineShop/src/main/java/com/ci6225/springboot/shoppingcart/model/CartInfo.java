package com.ci6225.springboot.shoppingcart.model;

import java.util.ArrayList;
import java.util.List;

import com.ci6225.springboot.shoppingcart.model.CartLineInfo;
import com.ci6225.springboot.shoppingcart.model.CustomerInfo;
import com.ci6225.springboot.shoppingcart.model.ProductInfo;

public class CartInfo {
	private int orderNum;
	private CustomerInfo customerInfo;
	
	private final List<CartLineInfo> cart = new ArrayList<CartLineInfo>();
	
	public CartInfo() {
		
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}
	
	public List<CartLineInfo> getCart(){
		return this.cart;
	}
	private CartLineInfo findLineByCode(String code) {
        for (CartLineInfo line : this.cart) {
            if (line.getProductInfo().getPuppyCode().equals(code)) {
                return line;
            }
        }
        return null;
    }
 
    public void addProduct(ProductInfo productInfo, int quantity) {
        CartLineInfo line = this.findLineByCode(productInfo.getPuppyCode());
 
        if (line == null) {
            line = new CartLineInfo();
            line.setQuantity(0);
            line.setProductInfo(productInfo);
            this.cart.add(line);
        }
        int newQuantity = line.getQuantity() + quantity;
        if (newQuantity <= 0) {
            this.cart.remove(line);
        } else {
            line.setQuantity(newQuantity);
        }
    }
 
    public void validate() {
 
    }
    
    public void updateProduct(String code,int quantity) {
    	CartLineInfo line = this.findLineByCode(code);
    	
    	if (line != null) {
    		if (quantity <= 0) {
    			this.cart.remove(line);
    		}
    		else {
    			line.setQuantity(quantity);
    		}
    	}
    }
    
    public void removeProduct(ProductInfo productInfo) {
    	CartLineInfo line = this.findLineByCode(productInfo.getPuppyCode());
    	if (line != null) {
    		this.cart.remove(line);
    	}
    }
    
    public boolean isEmpty() {
    	return this.cart.isEmpty();
    }
    public boolean isValidCustomer() {
    	return this.customerInfo != null && this.customerInfo.isValid();
    }
    public int getQuantityTotal() {
    	int quantity = 0;
    	for (CartLineInfo cart:this.cart) {
    		quantity += cart.getQuantity();
    	}
    	return quantity;
    }
    
    public double getAmountTotal() {
    	double amount = 0;
    	for (CartLineInfo cart: this.cart) {
    		amount += cart.getAmount();
    	}
    	return amount;
    }
    
    public void updateQuantity(CartInfo cartForm) {
    	if (cartForm != null) {
    		List<CartLineInfo> lines = cartForm.getCart();
    		for(CartLineInfo line:lines) {
    			this.updateProduct(line.getProductInfo().getPuppyCode(),line.getQuantity());
    		}
    		
    	}
    }

}
