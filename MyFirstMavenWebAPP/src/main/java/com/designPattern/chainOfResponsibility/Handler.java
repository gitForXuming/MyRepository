package com.designPattern.chainOfResponsibility;
/*
 * 责任链模式  纯责任链模式
 * Filter 是非责任链模式
 * 
 */
public abstract class Handler {
	/*
	 * 下一个处理者
	 */
	private Handler nextHandler;

	public Handler getNextHandler() {
		return nextHandler;
	}

	public void setNextHandler(Handler nextHandler) {
		this.nextHandler = nextHandler;
	}
	
	/*
	 * 具体的处理方法
	 */
	public abstract void handlerRequest(String user,double fee);
}
