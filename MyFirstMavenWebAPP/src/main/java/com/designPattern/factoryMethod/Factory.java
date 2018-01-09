package com.designPattern.factoryMethod;

public class Factory {
	public static Sender getSender(String type){
		if("phone".equals(type)){
			return new PhoneSender();
		}else if("mail".equals(type)){
			return new MailSender();
		}
		return new MailSender();
	}
	public static void main(String[] args) {
		Sender sender = Factory.getSender("mail");
		sender.send("收到请回复我！");
	}
}
