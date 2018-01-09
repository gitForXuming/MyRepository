package com.RMI;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class RmiClient {
	public static void main(String[] args) {
		try {

			List<String> a = new ArrayList<String>();
			a.add("1");
			a.add("2");
			for (String temp : a) {
			if("1".equals(temp)){
			a.remove(temp);
			}
			}
			
			RmiService service = (RmiService)Naming.lookup("rmi://127.0.0.1:6600/rmi");
			List<RmiModel> list = service.getModelList();
			
			System.out.println(list.get(0).getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
