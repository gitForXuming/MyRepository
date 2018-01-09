package com.designPattern.single;
/*
 * 静态内部类单例模式 延时加载 调用 SingleHolder.instance 的时候SingleHolder 才会被加载
 */
public class SingleForStaticInnerClass {
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private static class SingleHolder{
		private static SingleForStaticInnerClass instance = new SingleForStaticInnerClass();
	}
	
	public static SingleForStaticInnerClass getInstance(){
		return SingleHolder.instance;
	}
	
	public static void main(String[] args) {
		SingleForStaticInnerClass.getInstance();
	}
}
