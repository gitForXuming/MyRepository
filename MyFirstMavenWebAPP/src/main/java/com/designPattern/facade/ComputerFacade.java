package com.designPattern.facade;

public class ComputerFacade {
	public void start(){
		new MainBoard().start();
		new Cpu().start();
		new Memory().start();
		new Display().start();
		System.out.println("电脑启动成功");
	}
	public void shutdown(){
		new MainBoard().shutdown();
		new Cpu().shutdown();
		new Memory().shutdown();
		new Display().shutdown();
		System.out.println("电脑关机成功");
	}
	
	public static void main(String[] args) {
		ComputerFacade computer = new ComputerFacade();
		computer.start();
		computer.shutdown();
	}
}
