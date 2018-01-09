package com.designPattern;

public class BuilderPattern {
	private String head;
	private String eye;
	public static BuilderPattern create(){
		return new BuilderPattern();
	}
	public BuilderPattern builderHead(String head){
		this.head=head;
		return this;
	} 
	
	public BuilderPattern buidlerEye(String eye){
		this.eye = eye;
		return this;
	}
	
	public static void main(String[] args) {
		BuilderPattern person = BuilderPattern.create().builderHead("head").buidlerEye("eye"); 
		BuilderPattern person1 = BuilderPattern.create().builderHead("head1").buidlerEye("eye1");
		
		System.out.println(person.eye);
		System.out.println(person1.eye);
	}
}
