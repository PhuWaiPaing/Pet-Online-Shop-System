package com.ci6225.springboot.shoppingcart.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="products")
public class Puppy implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	private String gender;
	private double price;
	private byte[] image;
	
	private Date createDate;
	
	public Puppy() {
		
	}
	@Id
	@Column(name = "Code", length = 20, nullable = false)
    public String getCode() {
        return code;
    }
 
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "Name", length = 255, nullable = false)
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "Gender", length = 255, nullable = false)
    public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Column(name = "Price", nullable = false)
    public double getPrice() {
        return price;
    }
 
    public void setPrice(double price) {
        this.price = price;
    }
 
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }
 
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
 
    @Lob
    @Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
    public byte[] getImage() {
        return image;
    }
 
    public void setImage(byte[] image) {
        this.image = image;
    }

}
