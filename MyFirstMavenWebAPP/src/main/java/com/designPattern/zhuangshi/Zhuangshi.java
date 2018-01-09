package com.designPattern.zhuangshi;



public class Zhuangshi implements Sourceable{
	private Sourceable source;
	private Object obj = new Object();
	public Zhuangshi(Sourceable source){
		this.source= source;
	}
	@Override
	public void method() {
		synchronized(obj){//将source实现线程安全的访问
			source.method();
			//Collections.synchronizedList(list)
		}
	}
	
	public static void before(){
		System.out.println("before dosomething");
	}
	
	public static void after(){
		System.out.println("after dosomething");
	}
}
