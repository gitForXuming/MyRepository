package com.designPattern.template;

public class Teacher extends Template{
	private String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Teacher(String name){
		super();//JDK默认会添加这一行
		this.name=name;
	}
	@Override
	public void dressUp() {
		System.out.println(String.format("老师[%s]起床，穿上漂亮的裙子，花了美美的妆。", this.name));
		
	}

	@Override
	public void eatBreakfast() {
		System.out.println(String.format("老师[%s]起床，做了自己和老公、孩子三人份的早餐，并吃了自己的早餐。", this.name));
		
	}

	@Override
	public void gotoSchool() {
		System.out.println(String.format("老师[%s]开着小轿车去学校", this.name));
		
	}

}
