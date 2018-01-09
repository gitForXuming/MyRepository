package com.designPattern.factoryMethod;

public class MailSender implements Sender{

	@Override
	public void send(String message) {
		System.out.println("mail message send success");
		
	}

}
