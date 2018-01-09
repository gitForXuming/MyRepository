package com.model.VO;

public class UserInfoVO extends BeanVO{
	private static final long serialVersionUID = 9210130513464501449L;

	@SuppressWarnings("unchecked")
	private String username;
	
	@SuppressWarnings("unchecked")
	private String password;
	private String number;
	private String errorMessage;
	private int index;

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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
