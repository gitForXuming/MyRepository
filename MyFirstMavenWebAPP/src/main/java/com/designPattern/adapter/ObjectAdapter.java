package com.designPattern.adapter;
/*
 * 适配器分为类适配器模式 对象适配器模式 接口适配器模式
 * 接口适配器模式就是extends 的类包含所有implements定义的方法
 * 
 * 对象配器模式
 */
public class ObjectAdapter implements Targetable{
	private Sourceable source;
	
	public ObjectAdapter(Sourceable source){
		this.source=source;
	}

	@Override
	public void methodOne() {
		
		source.methodOne();
	}

	@Override
	public void methodTwo() {
		System.out.println("methodTwo doSomeThing");
		
	}
	
	public static void main(String[] args) {
		Targetable adapter = new ObjectAdapter(new Source());
		adapter.methodOne();
		adapter.methodTwo();
	}
}
