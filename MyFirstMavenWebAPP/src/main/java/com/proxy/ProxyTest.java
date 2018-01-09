package com.proxy;

import java.lang.reflect.Proxy;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.service.IUserService;
import com.service.impl.UserServiceImpl;

public class ProxyTest {
	public static void main(String[] args) {
	
		Thread t1 = new Thread(new ThreadTest( Long.parseLong("3000")) ,"thrad1");
		Thread t2 = new Thread(new ThreadTest( Long.parseLong("4000")) ,"thrad2");
		Thread t3 = new Thread(new ThreadTest( Long.parseLong("5000")) ,"thrad3");
		Lock lock = new ReentrantLock();
		Condition c = lock.newCondition();	
		lock.lock();
		try{
			System.out.println("begin do something.....");
			try {
				System.out.println("begin do something..... wait");
				//c.await();//然自己等待  直到谁从 c 通知我了我再启动
				
				System.out.println("begin do something..... singnal");
				c.signalAll(); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("do something.....");
		}finally{
			lock.unlock();
		}
		t1.start();
		t2.start();
		t3.start();
	}
	
	public final void test(){
		new ThreadTest( Long.parseLong("3000"));
	}
	private static class ThreadTest  implements  Runnable{
		private long time;
		public ThreadTest(long time){
			this.time = time;
		}
		@Override
		public void run() {
			
			UserServiceImpl usi = new UserServiceImpl();
			
			ProxyHandler ph = new ProxyHandler(usi);
			
			IUserService proxy = (IUserService)Proxy.newProxyInstance(usi.getClass().getClassLoader(), usi.getClass().getInterfaces(), ph);
			//usi.getClass().newInstance();
			Class<?> t = proxy.getClass();
			proxy.testProxy(1, 2, "3" ,time);
			
			proxy.testProxy(1, 2, "3" ,time);
		}
	}
}
