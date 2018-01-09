package com.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

//此为远程对象的实现类，须继承UnicastRemoteObject
public class RmiServiceImpl extends UnicastRemoteObject implements RmiService{
	private static final long serialVersionUID = -8073404305626076413L;

	protected RmiServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public List<RmiModel> getModelList() throws RemoteException {
		List<RmiModel> list = new ArrayList<RmiModel>();
		RmiModel model = new RmiModel();
		model.setUsername("xu");
		model.setPassword("111111");
		
		list.add(model);
		return list;
	}

}
