package com.rabbitMQ;

import java.io.Serializable;

public class TestBean implements Serializable{

	private static final long serialVersionUID = -917952677862817358L;
	
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
