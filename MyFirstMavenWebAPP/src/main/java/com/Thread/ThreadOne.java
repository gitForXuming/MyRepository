package com.Thread;

public class ThreadOne  extends Thread{

	@Override
	public void run() {
			
		synchronized(LockTestClass.a){
			System.out.println("线程1 拿到 a 对象");
			try{
				System.out.println("线程1即将等待50毫秒");
				//Thread.sleep(50);
			}catch(Exception e){
				
			}
			synchronized(LockTestClass.b){
				System.out.println("线程1 拿到 b 对象");
			}
		};
		
		System.out.println("线程1运行结束");
	}
}
