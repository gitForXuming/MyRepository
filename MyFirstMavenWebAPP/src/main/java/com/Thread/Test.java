package com.Thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import com.test.Test.ColorEnum;

public class Test {
	public static String s = "abc";
	public static void main(String[] args) {
		try{
			int[] obj = new int[]{1 ,3 ,5 ,7};
			System.out.println(obj.toString());
			
			ColorEnum color = ColorEnum.red;
			ArrayList al = new ArrayList(5);
			al.add("030700");
			al.add("030101");
			if(al.contains("030201")){
				System.out.println("c存在");
			}
			
			Calendar StartTime = Calendar.getInstance();
			StartTime.set(2015,11,31,19 ,00,00);
			
			Calendar endTime = Calendar.getInstance();
			endTime.set(2016,00,01,6,00,00);
			
			String currtTime = "20160101080001";
			Calendar currt = Calendar.getInstance(); 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			
			currt.setTime(sdf.parse(currtTime));
			
			if(currt.after(StartTime) && currt.before(endTime)){
				
				System.out.println("at betweent startTime and entTime");
			}
			System.out.println(sdf.format(StartTime.getTime()));
			
			System.out.println(sdf.format(endTime.getTime()));
			
			System.out.println(sdf.format(currt.getTime()));
			
			HashMap hm = new HashMap();
			hm.put("", "");
			ConcurrentHashMap chm = new ConcurrentHashMap();
			
			
			Executors.newCachedThreadPool();
			//Executors.newScheduledThreadPool();
		}catch(Exception e){
			
		}	
		/*ThreadOne t1 = new ThreadOne();
		ThreadTwo t2 = new ThreadTwo();
		
		for(int i=0;i<2;i++){
			if(i==0){
				t1.start();
				continue;
			}
			t2.start();
			
		}*/
	}
}
