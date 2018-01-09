package com.proxy;

import org.apache.log4j.Logger;

public class PerformanceMonitor {
	public static final Logger logger = Logger.getLogger(PerformanceMonitor.class);
	
	public static int testVar = 15;
	private static ThreadLocal<PerformanceMethod> thl = new ThreadLocal<PerformanceMethod>();
	
	public static void begin(String className , String method){
		
		PerformanceMethod pm = new PerformanceMethod(className, method);
		thl.set(pm);
	}
	
	public static void end (){
		PerformanceMethod pm = thl.get();
		pm.performance();
	}
	
	private static class PerformanceMethod{
		public static final Logger logger = Logger.getLogger(PerformanceMethod.class);
		private  String className;
		private String method;
		private  long begin;
		private  long end;
		
		public PerformanceMethod(){
		}
		
		public  PerformanceMethod(String className ,String method){
			this.className =className;
			this.method= method;
			begin = System.currentTimeMillis();
		}
		
		public void performance(){
			this.end =System.currentTimeMillis();
			long elapse  = this.end - this.begin;
			
			logger.info("线程：" + Thread.currentThread().getName()+","+"执行"+className +"的"+method+"方法耗时：" + elapse );
		}
	}
	
}
