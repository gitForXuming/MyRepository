package com.Thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class InterruptExceptionTest {
	  private static final ReentrantLock lock = new ReentrantLock();
	    /** Condition to wait on until tripped */
	    private static final Condition trip = lock.newCondition();
	    
	public static void main(String[] args) {
		
		Thread t = new Thread(new Work());
		t.start();  //启动线程
		
		
		Thread t2 = new Thread(new Work2());
		  //启动线程
		
		try{
			Thread.sleep(1000);
			
			}catch(Exception e){
				
			}
		t2.start();
		
		/*System.out.println("1:"+t.interrupted()); //此时线程还在运行 还未终止掉
		t.interrupt();  //终止线程
		
		System.out.println("2:"+t.interrupted()); // 此处调用 线程要么运行完毕了 要么已经被终止掉了
*/		
	}

	static class Work implements  Runnable {
		@Override
		public void run() {
		/*	try{*/
			lock.lock();
			try{
			for(;;){
				System.out.println("0");
				try {
					Thread.sleep(10000);
					trip.await();
				} catch (Exception e) {
					
				}
				
				System.out.println(1);
				/*Thread.sleep(1000);
				System.out.println(1);*/
			}
			}finally{
				//lock.unlock();
			}
			/*}catch(Exception e){
				Thread.currentThread().interrupt(); //再次让线程终止掉
			}*/
			/*try{
				Thread.sleep(10000);
			}catch(InterruptedException e){ //捕获IngterruptException 当你捕获了这个异常线程会恢复继续运行 状态肯定不是Interrupted
				System.out.println("1");
				Thread.currentThread().interrupt(); //再次让线程终止掉
			}
			System.out.println("3:"+Thread.interrupted());// 这个地方打印的就是 true 线程已经终止掉了
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				
			}
			System.out.println("4:"+Thread.interrupted());*/
		}
	}
	static class Work2 implements  Runnable{

		@Override
		public void run() {
			lock.lock();
			
			System.out.println(2);
			
			lock.unlock();
			
		}
		
	} 
}
