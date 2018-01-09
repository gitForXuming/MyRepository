package com.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public enum ZookeeperClient implements Watcher{
	INSTANCE("127.0.0.1:2181",3000);
	private ZooKeeper zk;
	private ZookeeperClient(String host ,int tomeOut){
		try{
			this.zk = new ZooKeeper(host, tomeOut, null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public ZooKeeper getInstance(){
		return zk;
	}
	
	@Override
	public void process(WatchedEvent arg0) {
		System.out.println("123");
		
	}
}
