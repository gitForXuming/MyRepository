package com.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
/**
 * 
* @Title: MyFirstZookeeper.java 
* @Package com.zookeeper 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuming  
* @date 2017年1月7日 下午11:46:23 
* @version V1.0
 */
/**
 * 
* @Title: MyFirstZookeeper.java 
* @Package com.zookeeper 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuming  
* @date 2017年1月7日 下午11:46:23 
* @version V1.0
 */
public class MySecondZookeeper {
	 private static CountDownLatch latch = new CountDownLatch(1);
	 
	public static void main(String[] args) {
		ZooKeeper zk =null;
		try {
			zk = new ZooKeeper("127.0.0.1:2181" , 1000, new Watcher(){
				@Override
				public void process(WatchedEvent even) {
					if(even.getState() == KeeperState.SyncConnected ){
						latch.countDown();
					}
					
					if(even.getType()==EventType.NodeCreated){
						
					}
					System.out.println(String.format("已触发了：%s事件", even.getType().toString()));
					
				}
			});
			latch.await();//等连接建立了再执行逻辑
			
			List<ACL>	acls = new ArrayList<ACL>();
			Id id1 = new Id("world","anyone");//任何人都能读
			acls.add(new ACL(ZooDefs.Perms.ALL, id1));
			zk.create("/test", "1024".getBytes(), acls, CreateMode.PERSISTENT);
			zk.setData("/test", "test".getBytes(), -1);
			acls.clear();
			
			Id id2 = new Id("digest",DigestAuthenticationProvider.generateDigest("admin:admin123"));
			acls.add(new ACL(ZooDefs.Perms.ALL,id2)); //所有权
			
			Id id3 = new Id("digest",DigestAuthenticationProvider.generateDigest("guset:guest123"));
			acls.add(new ACL(ZooDefs.Perms.READ,id3));
			zk.create("/test/server1", "1024".getBytes(), acls, CreateMode.PERSISTENT);
			acls.clear();
			
			Id id4 = new Id("ip", "192.168.86.128"); //限定ip
			acls.add(new ACL(ZooDefs.Perms.READ,id4));
			zk.create("/test/server2", "1024".getBytes(), acls, CreateMode.PERSISTENT);
			
			try{
				zk.getData("/test/server1", null, null);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			try{
				zk.addAuthInfo("digest", "admin:admin123".getBytes());
				//zk.addAuthInfo("digest", "guset:guest123".getBytes());
				System.out.println(new String(zk.getData("/test/server1", null, null)));
				zk.setData("/test/server1", "ser".getBytes(), -1);
				System.out.println(new String(zk.getData("/test/server1", null, null)));
				
				
				System.out.println(new String(zk.getData("/test/server2", null, null)));
			}catch(Exception e){
				e.printStackTrace();
			}
			//zk.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	public static class Wc implements Watcher{

		@Override
		public void process(WatchedEvent even) {
			System.out.println(String.format("已触发了：%s事件", even.getType().toString()));
		}
		
	}
}
