package com.designPattern.prototype;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DeepCopy implements Cloneable ,Serializable{
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
		DeepCopy sourceClone = new DeepCopy();
		sourceClone.setId("123");
		sourceClone.setInfo(new Info("lisi",10));
		DeepCopy targetClone= null;
		
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(sourceClone);
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			targetClone = (DeepCopy)ois.readObject();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		//深复制则是把对象中的所有对象全部序列化 然后再反序列化 那么对象中所有的对象都必须实现 serializable
		sourceClone.setId("456");
		sourceClone.getInfo().setAge(100);
		sourceClone.getInfo().setName("zhangsan");
		System.out.println(targetClone.getId());
		System.out.println(targetClone.getInfo().getName());
	}
	
	public static class Info implements Serializable{
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
