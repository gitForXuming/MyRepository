package com.Thread;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadLocalTest {
	private static ThreadLocal<DateFormat> tl = new ThreadLocal<DateFormat>(){
		@Override
		protected DateFormat initialValue() {
			return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	
	public static Date parse(String date) throws ParseException{
		
		return tl.get().parse(date);
	}
}
