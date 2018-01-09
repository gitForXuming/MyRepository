package com.Thread;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {
	private static final Date data ;
	private final String s ;
	/*{
		s="";
	}*/
	static{
		data = new Date();
		System.out.println(data);
	}
	CyclicBarrierTest(){
		s="";
		System.out.println(2);
	}
	public static void main(String[] args) {
		int num =11;
		CyclicBarrierTest test = new CyclicBarrierTest();
		CyclicBarrier barrier = new CyclicBarrier(num, new Runnable() {
			
			@Override
			public void run() {
				
				System.out.println("我们都执行完成了");
			}
		});
		
		for(int i=1;i<=num-1 ;i++){
			new Thread(new CyclicBarrierWorker(i, barrier)).start();
		}
	}
	
	
	
	static class CyclicBarrierWorker implements Runnable{
		private int id;
		private CyclicBarrier barrier;
		
		CyclicBarrierWorker(int id , CyclicBarrier barrier){
			this.id= id;
			this.barrier = barrier;
		}
		@Override
		public void run() {
			if(id==9){
				try{
					Thread.sleep(10000);
				}catch(Exception e){
					
				}
			}
			System.out.println("我："+ id +"执行完成了,等待其他线程执行完成");
			try {
				barrier.await();
				
				System.out.println(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
		
	}
}
