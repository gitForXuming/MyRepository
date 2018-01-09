package com.Thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {
	private static  final CountDownLatch startGate = new CountDownLatch(1);
	private static CountDownLatch countDownLatch = new CountDownLatch(3);
	
	public static void main(String[] args) {
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("one  to do something!");
				countDownLatch.countDown();
				System.out.println("one to do something success");
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("two  to do something!");
				countDownLatch.countDown();
				System.out.println("two to do something success");
			}
		});
		
		Thread t3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					 startGate.await(5 ,TimeUnit.SECONDS); //堵住线程 等待所有线程就绪后让大家一起跑 相当于跑步比赛必须大家一起跑
					                                        //这种双重阻塞    
					Thread.sleep(2000);
					System.out.println(countDownLatch.getCount());
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					System.out.println("failed");
					e.printStackTrace();
				}
				System.out.println("three  to do something!");
				
				countDownLatch.countDown();
			}
		});
		
		//startGate.countDown(); // 线程同时跑
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("we are successly!");
		
	}
}
