package com.designPattern.template;

public class TemplateTest {
	public static void main(String[] args) {
		Template student = new Student("小明");
		Template teather = new Teacher("王丽");
		
		student.goSchool();
		teather.goSchool();
	}
}
