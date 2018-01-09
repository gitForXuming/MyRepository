package com.Thread.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class ForkjoinNoResult extends RecursiveAction{
	private final static int MAX = 20;
	private int start;
	private int end;
	
	public ForkjoinNoResult(int start,int end){
		this.start =start;
		this.end = end;
	}
	@Override
	protected void compute() {
		if((end -start)< MAX){
			for(int i =start; i<end;i++){
				System.out.println(Thread.currentThread().getName() + ":"+i);
			}
		}else{
			int middle = (end + start)>>1;
			ForkjoinNoResult leftTask = new ForkjoinNoResult(start, middle);
			ForkjoinNoResult rightTask = new ForkjoinNoResult(middle,end);
			
			leftTask.fork();
			rightTask.fork();
		}
	}
	
	public static void main(String[] args) {
		ForkJoinPool fjpPool =new ForkJoinPool();//不带参数的代表创建默认cpu个数的task
		ForkjoinNoResult fjn = new ForkjoinNoResult(1, 1000);
		fjpPool.submit(fjn);
		try {
			fjpPool.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
		//fjp.shutdown();
	}

}
