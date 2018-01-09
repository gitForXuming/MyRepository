package com.Thread;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
	public static String s = "abc";
	
	public String ss ="abc";
	
	public static void main(String[] args) {
		ReentrantLock lock = new ReentrantLock();
		
		ReentrantLockTest tt = new ReentrantLockTest();
		//tt.setSs("true");
		
		tt.setSs(new String("true"));
		
		String s1 = "true";
		String s2 = "true";
		
		String s3 = new String("true");
		String s4 = new String("true");
		
		System.out.println(s1 == s2);
		System.out.println(s1 == s3);
		System.out.println(s3 == s4);
		
		System.out.println("true" == tt.getSs());
		
		
		//System.out.println(s1 == s2);
		
		/*try {
			
			
			lock.lock();
			lock.tryLock(1, TimeUnit.SECONDS);
			lock.lockInterruptibly();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public void test(){
		System.out.println(this.ss == s);
	}

	public String getSs() {
		return ss;
	}

	public void setSs(String ss) {
		this.ss = ss;
	}
}
