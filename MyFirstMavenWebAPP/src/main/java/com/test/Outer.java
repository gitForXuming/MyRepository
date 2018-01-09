package com.test;

public class Outer {
	static{
		System.out.println("我被加载了。。。");
	}
	public static void main(String[] args) {
		Outer outer = new Outer();
		Outer.Inner tt= outer.new Inner();
		System.out.println(tt.j);
		
	}
	
	
	public class Inner{
		private int i=1;
		private int j;
		
		public Inner(){
			System.out.println(j);
		}
		
		{
			j=2;
		}
		
		public int test(){
			
			return j;
		}
	}
}
