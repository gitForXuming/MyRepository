package com.designPattern.builder;
/*
 * 建造者模式
 */
public class Person {
	private String head;
	private String eye;
	private String ear;
	private String leg;
	private String foot;
	
	public static Person create(){
		return new Person();
	}
	
	public Person builderHead(String head){
		this.head = head;
		return this;
	}
	
	public Person builderEye(String eye){
		this.eye = eye;
		return this;
	}
	
	public Person builderEar(String ear){
		this.ear = ear;
		return this;
	}
	
	public Person builderLeg(String leg){
		this.leg = leg;
		return this;
	}
	
	public Person builderFoot(String foot){
		this.foot = foot;
		return this;
	}
	
	
	
	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getEye() {
		return eye;
	}

	public void setEye(String eye) {
		this.eye = eye;
	}

	public String getEar() {
		return ear;
	}

	public void setEar(String ear) {
		this.ear = ear;
	}

	public String getLeg() {
		return leg;
	}

	public void setLeg(String leg) {
		this.leg = leg;
	}

	public String getFoot() {
		return foot;
	}

	public void setFoot(String foot) {
		this.foot = foot;
	}

	public static void main(String[] args) {
		Person person1 = Person.create().builderHead("head").builderEye("eye").builderEar("ear");
		System.out.println(person1.getHead());
	}
}
