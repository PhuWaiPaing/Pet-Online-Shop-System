package com.ci6225.springboot.shoppingcart.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ci6225.springboot.shoppingcart.entity.Order;
import com.ci6225.springboot.shoppingcart.entity.Puppy;

@Entity
@Table(name="order_details")
public class OrderDetails implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	private String id;
	private Order order;
	
	private Puppy puppy;
	private int quantity;
	private double price;
	private double amount;
	
	@Id
	@Column(name = "ID", length = 50, nullable = false)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ORDER_ID", nullable = false, foreignKey = @ForeignKey(name= "ORDER_DETAIL_ORD_FK"))
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PRODUCT_ID", nullable = false, foreignKey = @ForeignKey(name= "ORDER_DETAIL_PROD_FK"))
	public Puppy getPuppy() {
		return puppy;
	}
	public void setPuppy(Puppy puppy) {
		this.puppy = puppy;
	}
	@Column(name = "Price", nullable = false)
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	@Column(name = "Quantity", nullable = false)
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Column(name = "Amount", nullable = false)
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
