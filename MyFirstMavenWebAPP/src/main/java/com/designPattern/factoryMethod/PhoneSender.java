package com.designPattern.factoryMethod;

public class PhoneSender implements Sender{

	@Override
	public void send(String message) {
		System.out.println("phone message send success");
		
	}

}
