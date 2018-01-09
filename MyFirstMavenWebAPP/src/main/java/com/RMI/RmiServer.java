package com.RMI;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RmiServer {

	public static void main(String[] args) {
		try {
			RmiServiceImpl rmi = new RmiServiceImpl();
			
			//注册通讯端口
			LocateRegistry.createRegistry(6600);
			//注册通讯路径
			Naming.rebind("rmi://127.0.0.1:6600/rmi", rmi);
			System.out.println("Service Start!");
			
		} catch (Exception e) {
				
		}

	}

}
