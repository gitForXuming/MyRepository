package com;

import java.util.ArrayList;
import java.util.List;

public class StackOverFlowTest {
	
	public static List list = new ArrayList();
	public static int i =0;
	
	public static void test(){
		i+=1;
		System.out.println(i);
		
		byte[] b = new byte[1024*1024];
		//list.add(b);
		StackOverFlowTest.test();
		
	}
	
	
	public static void pp(){
		StackOverFlowTest.test();
	}
	public static void main(String[] args) {
		String s = "abc";
		switch(s){
		case "abc":
			System.out.println("abc");
		case "ab":
			System.out.println("ab");
		default:
			System.out.println("1");
	}
		StackOverFlowTest.pp();
	}
}
