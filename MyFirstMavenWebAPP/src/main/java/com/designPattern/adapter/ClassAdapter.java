package com.designPattern.adapter;
/*
 * 适配器分为类适配器模式 对象适配器模式 接口适配器模式
 * 接口适配器模式就是extends 的类包含所有implements定义的方法
 * 
 * 类适配器模式
 */
public class ClassAdapter extends Source implements Targetable {

	@Override
	public void methodTwo() {
		System.out.println("methodTwo doSomeThing");
		
	}

	public static void main(String[] args) {
		Targetable adapter = new ClassAdapter();
		adapter.methodOne();//实际上调用的是 source的methodOne的方法 
		adapter.methodTwo();
	}
}
