package com.test;

import java.util.Hashtable;
import java.util.PriorityQueue;

public class Test3 {
	private static boolean foo(char c){
		System.out.println(c);
		return true;
	}
	public static void main(String[] args) {
		PriorityQueue<String> pq = new PriorityQueue<String>();
		pq.add("String");
		String s = pq.poll();
		System.out.println(s);
		
		Hashtable ht = new Hashtable();
		ht.size();
		ht.put("", "value");
		
		System.out.println(Integer.toBinaryString(-21>>>30));
		System.out.println(Integer.toBinaryString(-21>>30));
    	System.out.println(Integer.toBinaryString(21>>>4 & 0xf));
		
		int x=4;
		int xu= x * 1 >>> 3;
		System.out.println(xu);
		System.out.println((0.0/0)==(0.0/0));
		double in = 1000;
		int nt = 0;
		System.out.println(in/nt);
		
		int ii = 129;
		Integer iii = 129;
		Integer iiii = 129;
		System.out.println(ii==iii);
		System.out.println(iii==iiii);
		
		String a ="str" + "ing";
		String b = "string";
		String c = new String("string");
		String d = "str".concat("ing");
		String e = "str";
		String f = "ing";
		String g = e+f;
		String h = e+f;
		String j = c.intern();
		
		System.out.println(a==b);
		System.out.println(a==c);
		System.out.println(b==c);
		System.out.println(b==d);
		System.out.println(b==g);
		System.out.println(a==g);
		System.out.println(g==h);
		System.out.println(b==j);
		
		
		
		int i =0;
		for(foo('A');foo('B') && (i<2);foo('C')){
			foo('D');
			i++;
			//ABDCBDC
		}
	}
}
