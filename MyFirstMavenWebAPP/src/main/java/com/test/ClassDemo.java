package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


	public class ClassDemo<T> implements Comparable<Object>{

	   public static void main(String[] args) {
	     HashSet set = new HashSet();
	     set.size();
		 System.out.println("3705151801".endsWith("3705151801"));  
	     ClassDemo cls = new ClassDemo();
	     Class c = cls.getClass();      
	     System.out.println(c);  
	   
	     Object obj = new A();        
	     B b1 = new B();
	     b1.show();
	        
	     // casts object
	     Object a = A.class.cast(b1);
	     Object d = (A)b1;
	     System.out.println(obj.getClass());
	     System.out.println(b1.getClass());
	     System.out.println(a.getClass());  
	     System.out.println(b1.getClass());
	     System.out.println(d.getClass());
	     
	     cls.get();
	     List list = new ArrayList();
	   }
	   
	  public T get(){
		  Map map = new HashMap();
		  return (T)map.get("");
	  }
	  static class A {
		   public static void show() {
		      System.out.println("Class A show() function");
		   }
		}

	  static class B extends A {
		   public static void show() {
		      System.out.println("Class B show() function");
		   }
		}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	} 