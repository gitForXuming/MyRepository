package com.designPattern.observPattern;

import java.util.ArrayList;
import java.util.List;

public class ObserveTest {
	private static List<String> list = new ArrayList<String>();
	public static void main(String[] args) {
		
		SubjectImpl subject = new SubjectImpl();
		Observer1 obs1 = new Observer1();
		
		subject.add(obs1);
		subject.add(new Observer(){

			@Override
			public void update() {
				System.out.println("我是通监听方式添加的观察者哦.....");
				list.add("123"); //看看是不是很熟悉 监听器就是通过观察者 + CallBack
				
			}
		});
		
		subject.notifyObserve();
		
		System.out.println(list.get(0));
	}
	
	
	public static void operate(){
		System.out.println("监听者反向调用 CallBack");
	}
}
