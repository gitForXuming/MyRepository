package com.Thread.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CountTask extends RecursiveTask<Integer>{
	private final static int MAX = 2;
	private int start;
	private int end;
	
	public CountTask(int start,int end){
		this.start =start;
		this.end = end;
	}
	@Override
	protected Integer compute() {
		int sum =0;
		if((end -start)<MAX){
			for(int i =start; i<=end;i++){
				//System.out.println(start);
				
				if(start ==1){
				
				}
				sum+=i;
			}
			return sum;
		}else{
			
			int middle = (end + start)>>1;
			CountTask leftTask = new CountTask(start, middle);
			CountTask rightTask = new CountTask(middle+1,end);
			
			leftTask.fork();
			rightTask.fork();
			leftTask.invoke();
			
			return leftTask.join() + rightTask.join();
			
		}
	}

	public static void main(String[] args) {
		CountTask ct = new CountTask(1,10);
		ForkJoinPool fjpPool =new ForkJoinPool();
		ForkJoinTask<Integer> result = fjpPool.submit(ct);
		
		try {
			System.out.println(result.get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
