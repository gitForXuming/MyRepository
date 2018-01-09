package com.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
/**
 * 
* @Title: MyFirstZookeeper.java 
* @Package com.zookeeper 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuming  
* @date 2017年1月7日 下午11:46:23 
* @version V1.0
 */
public class MyFirstZookeeper {
	public static void main(String[] args) {
		ZooKeeper zk =null;
		try {
			zk = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183" , 1000, new Watcher(){
				@Override
				public void process(WatchedEvent even) {
					System.out.println("1:"+System.currentTimeMillis());
					System.out.println(String.format("已触发了：%s事件，", even.getType().toString()+ even.getPath()));
					
				}
			});
			//Thread.sleep(2000);
			/*zk.exists("/test", new Watcher() {
				@Override
				public void process(WatchedEvent even) {
					System.out.println(String.format("已触发了：%s事件", even.getType().toString()+ even.getPath()));
				}
			});*/
			System.out.println("2:"+System.currentTimeMillis());
			zk.create("/test", "znode1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT); 
			
			zk.getChildren("/test", true);
			//zk.exists("/test", true);
			//zk.delete("/test", -1);
			/*zk.exists("/test/server1", true);
			zk.create("/test/server1", "znode1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT); */
			/*zk.getChildren("/test", true);
			zk.setData("/test/server1", "1".getBytes(), -1);
			zk.delete("/test", -1);*/
		/*	zk.getData("/test", true,new Stat());
			zk.delete("/test", -1);*/
			/*zk.getData("/test", new Watcher() {
				@Override
				public void process(WatchedEvent even) {
					System.out.println(String.format("已触发了新watch的：%s事件，", even.getType().toString()+ even.getPath()));
				}
			}, new Stat());*/
			//zk.setData("/test", "12".getBytes(), -1);
			/*zk.getData("/test", true, new Stat()) ;
			zk.setData("/test", "13".getBytes(), -1);
			zk.getData("/test", true, new Stat()) ;
			zk.setData("/test", "145".getBytes(), -1);
			zk.getData("/test", true, new Stat()) ;
			zk.getData("/test", true, new Stat()) ;*/
			//zk.setData("/test", "12".getBytes(), -1);
			System.out.println("3:"+System.currentTimeMillis());
			Thread.sleep(5000);
		/*	System.out.println(s.getEphemeralOwner());
			System.out.println(s.getCversion());
			System.out.println(s.getCtime());
			System.out.println(s.getEphemeralOwner());
			zk.setData("/test", "12".getBytes(), -1);
			Thread.sleep(500);*/
			/*if(zk.exists("/test", true) == null){             
				zk.create("/test", "znode1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);         
			}
			zk.setData("/test", "123".getBytes(), -1);
			if(zk.exists("/test/server1", true) == null){             
				zk.create("/test/server1", "server1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);         
			} 
			zk.setData("/test/server1", "123".getBytes(), -1);*/ 
			//zk.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
}
