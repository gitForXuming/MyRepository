package com.designPattern.proxyPattern;

public class Proxy implements Sourceabled{
	private Sourceabled source;
	
	public Proxy(Sourceabled source){
		this.source = source;
	}
	@Override
	public void proxyMethod() {
		this.before();
		
		source.proxyMethod();
		
		this.after();
		
	}
	
	private void before(){
		System.out.println("我是来打酱油的，通过代理增加的执行前方法........");
	}
	
	
	private void after(){
		System.out.println("酱油打完了，通过代理增加的执行后方法........");
	}
}
