package com.Thread;

public class ThreadTwo extends Thread{

	@Override
	public void run() {
		
		synchronized(LockTestClass.b){
			System.out.println("线程2 拿到 b 对象");
			synchronized(LockTestClass.a){
				System.out.println("线程2 拿到 a 对象");
			}
		};
		System.out.println("线程2运行结束");
	}
}
