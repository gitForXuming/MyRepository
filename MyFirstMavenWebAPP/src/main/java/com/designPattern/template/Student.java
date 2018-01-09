package com.designPattern.template;

public class Student extends Template{
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Student(String name){
		super();//JDK默认会添加这一行
		this.name=name;
	}
	@Override
	public void dressUp() {
		System.out.println(String.format("学生[%s]起床，穿上校服", this.name));
		
	}

	@Override
	public void eatBreakfast() {
		System.out.println(String.format("学生[%s]吃了妈妈做好的早饭", this.name));
		
	}

	@Override
	public void gotoSchool() {
		System.out.println(String.format("学生[%s]走路上学", this.name));
		
	}

}
