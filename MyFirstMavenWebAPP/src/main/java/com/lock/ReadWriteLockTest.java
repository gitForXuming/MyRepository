package com.lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
	public static ReadWriteLock lock =null;
	public static void main(String[] args) {
		lock = new ReentrantReadWriteLock(false);//不公平读写锁
		
		//读线程
		for(int i=0;i<5;i++){
			Thread t = new Thread(new ReadTask());
			try{
				t.join();
				t.start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		//写线程
		for(int i=0;i<5;i++){
			Thread t = new Thread(new WriteTask());
			try{
				t.join();
				t.start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	public static class ReadTask implements Runnable{

		@Override
		public void run() {
			lock.readLock().lock();
			try{
				System.out.println(String.format("线程：%s 正在读文件!", Thread.currentThread().getName()));
				Thread.sleep(2000);
				System.out.println(String.format("线程：%s 读文件结束!", Thread.currentThread().getName()));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				lock.readLock().unlock();
			}
		}
		
	}
	
	
	public static class WriteTask implements Runnable{

		@Override
		public void run() {
			lock.writeLock().lock();
			try{
				System.out.println(String.format("线程：%s 正在写文件!", Thread.currentThread().getName()));
				Thread.sleep(2000);
				System.out.println(String.format("线程：%s 写文件结束!", Thread.currentThread().getName()));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				lock.writeLock().unlock();
			}
			
		}
		
	}
}
