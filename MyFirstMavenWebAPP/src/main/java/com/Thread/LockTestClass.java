package com.Thread;

public class LockTestClass {
	public static String a ="12";
	public static String b="13";
	
	public synchronized static String a(){
		try{
			Thread.sleep(500);//do something
			
		}catch(Exception e){}
		return "a";
	}
	
	public synchronized static String b(int count){
		try{
			for(int i=0;i<count; i++){
				System.out.println(i);
			}//do something
		}catch(Exception e){}
		return "b";
	}
}
