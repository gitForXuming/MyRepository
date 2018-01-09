package com.designPattern.prototype;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class QianCopy implements Cloneable{
	private String id;
	private Info info;
	
	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static void main(String[] args) {
		QianCopy sourceClone = new QianCopy();
		sourceClone.setId("123");
		sourceClone.setInfo(new Info("lisi",10));
		QianCopy targetClone= null;
		try {
			targetClone = (QianCopy) sourceClone.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.out.println(sourceClone.getId());
		System.out.println(targetClone.getInfo().getName());
		sourceClone.setId("456");
		sourceClone.getInfo().setName("zhangsan");
		System.out.println(targetClone.getId());
		System.out.println(sourceClone.getInfo().name);
		//虽然targetClone克隆了 targetClone中的info引用对象也克隆了 但是info对象
        //引用的对象没有被复制 基本类型可以被克隆 但是非基本类型就不能被clone
        //String 特殊 因为String不可变
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream ois = new ObjectOutputStream(baos);
			ois.writeObject(sourceClone);
		}catch(Exception e){
			
		}
	}
	
	public static class Info{
		private String name ;
		private int age;
		
		public Info(String name ,int age){
			this.age=age;
			this.name=name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		} 
	}
}
