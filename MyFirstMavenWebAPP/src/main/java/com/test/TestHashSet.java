package com.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TestHashSet implements Runnable{
	// 实现Runnable 让该集合能被多个线程访问
	 Set<Integer> set = Collections.synchronizedSet(new HashSet<Integer>(10000));
	 
	public TestHashSet(){
		
	}
   
    // 线程的执行就是插入5000个整数
    @Override
    public void run() {
        for (int i = 0;i < 50;i ++) {
            set.add(i);
        }
    }

    public static void main(String[] args) {
    	 TestHashSet run2 = new TestHashSet();
         // 实例化两个线程
         Thread t6 = new Thread(run2);
         Thread t7 = new Thread(run2);
        try{
	         // 启动两个线程
	         t6.start();
	         t7.start();
	
	         // 当前线程等待加入到调用线程后
	         t6.join();
	         t7.join();
	        //Thread.sleep(10000);
        }catch(Exception e){
        	
        }
         // 打印出集合的size
         System.out.println(run2.set.size());
         Set s = run2.set;
         int i = 0;
         for (Iterator<Integer> it = run2.set.iterator();it.hasNext();) {
        	 i++;
        	 System.out.println(it.next());
		}
         System.out.println(i);
	}
}
