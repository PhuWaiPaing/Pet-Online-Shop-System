package com.ci6225.springboot.shoppingcart.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final String ROLE_EMPLOYEE = "employee";
	
	@Id
	@Column(name = "user_name", length = 20, nullable = false)
	private String userName;
	
	@Column(name = "active", length = 1, nullable = false)
	private boolean active;
	
	@Column(name = "password", length = 128, nullable = false)
	private String password;
	
	@Column(name = "user_role", length = 20, nullable = false)
	private String userRole;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@Override
	public String toString() {
		return "Account [userName=" + userName + ", active=" + active + ", password=" + password + ", userRole="
				+ userRole + "]";
	}
	
	

}
