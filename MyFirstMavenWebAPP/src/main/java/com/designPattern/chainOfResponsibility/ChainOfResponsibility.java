package com.designPattern.chainOfResponsibility;

public class ChainOfResponsibility {
	public static void main(String[] args) {
		Handler handler1 = new ObjectHandler();
		Handler handler2 = new DptementHandler();
		Handler handler3 = new BossHandler();
		
		handler1.setNextHandler(handler2);
		handler2.setNextHandler(handler3);
		
		handler1.handlerRequest("test", 8000.00);
	}
}
