package com.ci6225.springboot.shoppingcart.form;

import org.springframework.web.multipart.MultipartFile;

import com.ci6225.springboot.shoppingcart.entity.Puppy;

public class ProductForm {
	
	private String puppyCode;
	private String puppyName;
	private double puppyPrice;
	private String puppyGender;
	private boolean newPuppy = false;
	
	//upload file
	private MultipartFile file;
	
	public ProductForm() {
		this.newPuppy = true;
	}
	
	public ProductForm(Puppy puppy) {
		this.puppyCode = puppy.getCode();
		this.puppyName = puppy.getName();
		this.puppyGender = puppy.getGender();
		this.puppyPrice = puppy.getPrice();
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

	public double getPuppyPrice() {
		return puppyPrice;
	}

	public void setPuppyPrice(double puppyPrice) {
		this.puppyPrice = puppyPrice;
	}

	public String getPuppyGender() {
		return puppyGender;
	}

	public void setPuppyGender(String puppyGender) {
		this.puppyGender = puppyGender;
	}

	public boolean isNewPuppy() {
		return newPuppy;
	}

	public void setNewPuppy(boolean newPuppy) {
		this.newPuppy = newPuppy;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	

}
