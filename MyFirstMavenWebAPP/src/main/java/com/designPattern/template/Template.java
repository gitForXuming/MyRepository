package com.designPattern.template;

public abstract class Template {
	/*
	 * 模拟上学
	 */
	public void goSchool(){
		this.dressUp();
		this.eatBreakfast();
		this.gotoSchool();
	}
	/*
	 * 起床
	 */
	public abstract void dressUp();
	/*
	 * 吃早餐
	 */
	public abstract void eatBreakfast();
	/*
	 * 怎么去学校
	 */
	public abstract void gotoSchool();
}
