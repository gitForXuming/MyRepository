package com.RMI;

import java.io.Serializable;

/*
 * 必须实现序列化
 */
public class RmiModel implements Serializable{
	private static final long serialVersionUID = -4235897126129470645L;
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
