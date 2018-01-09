package com.designPattern.abstractFactory;

public class PhoneSender implements Sender{

	@Override
	public void send(String message) {
		System.out.println("phone message send success");
		
	}

}
