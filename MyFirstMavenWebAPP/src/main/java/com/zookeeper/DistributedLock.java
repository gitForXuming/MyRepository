package com.zookeeper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * http://agapple.iteye.com/blog/1184040
 * 
 * @Title: DistributedLock.java
 * @Package com.zookeeper
 * @Description: TODO(zookeeper 实现分布式锁)
 * @author xuming
 * @date 2017年1月10日 下午12:52:02
 * @version V1.0
 */
public class DistributedLock {
	private static final String HOST = "127.0.0.1:2181";
	private static final int TIME_OUT = 3000;
	final static CountDownLatch startCounter = new CountDownLatch(10);
	final static CountDownLatch overCounter = new CountDownLatch(10);

	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			Thread process = new Thread(new Runnable() {
				@Override
				public void run() {
					Task task = new Task();
					task.creatConn();
					task.creatPath();
				}
			});
			process.start();
			startCounter.countDown();
		}
		try {
			startCounter.await();
		} catch (Exception e) {

		}
		System.out.println("线程全部启动完毕");

		try {
			overCounter.await();
		} catch (Exception e) {

		}
		System.out.println("线程全部运行结束");
	}

	/**
	 * 
	* @Title: DistributedLock.java 
	* @Package com.zookeeper 
	* @Description: TODO(模拟一台物理机) 
	* @author xuming  
	* @date 2017年1月14日 下午12:10:23 
	* @version V1.0
	 */
	public static class Task implements  Watcher{
		private final String locksRootNode = "/locks";
		private final String locksSub = "sub";
		private final CountDownLatch gate = new CountDownLatch(1);
		private ReentrantLock lock = new ReentrantLock();
		private String thisPath;// 当前节点
		private String waitPath;// 等待前一个锁
		private ZooKeeper zk;

		/**
		 * 
		* @Title: creatConn 
		* @Description: TODO(创建Zookeeper连接) 
		* @param     
		* @return void    返回类型 
		* @throws
		 */
		private void creatConn(){
			try {
				zk = new ZooKeeper(HOST, TIME_OUT, this);
				Stat stat = zk.exists("/locks", false);
				if (null == stat) {
					zk.create(locksRootNode, "locksRoot".getBytes(),
							Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				gate.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//System.out.println("zk 连接成功！");
		}
		
		
		private void creatPath(){
			try {
				thisPath = zk.create(locksRootNode + "/" + locksSub,
						"lock".getBytes(), Ids.OPEN_ACL_UNSAFE,
						CreateMode.EPHEMERAL_SEQUENTIAL);
				if(checkMin(thisPath)){
					doSomething();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			}
			/*try{
				Thread.sleep(100000);
			}catch(Exception e){
				
			}*/
		}
		/**
		 * 
		* @Title: checkMin 
		* @Description: TODO(每次执行任务的时候都要检查自己是不是最小) 
		* @param @param path
		* @param @return    
		* @return boolean    返回类型 
		* @throws
		 */
		private boolean checkMin(String path){
			try{
				List<String> childNodes = zk.getChildren(locksRootNode, false);

				// 说明只有自己一个创建了目录 说明没有
				String thisNode = thisPath.substring((locksRootNode + "/").length());
				Collections.sort(childNodes);
				int index = childNodes.indexOf(thisNode);
				switch(index){
					case -1:
						return false;
					case 0:
						return true;
					default:
						this.waitPath = locksRootNode + "/"
								+ childNodes.get(index - 1);
						// 在waitPath上注册监听器, 当waitPath被删除时,
						// zookeeper会回调监听器的process方法
						// 此处是重新注册监听器 如果没有指定新监听器 就注册在默认的监听器上
						// 也就是zookeeper构造函数指定的监听器
						// 此处有bug 假如执行到这的时候 child总共是10 而我排在第2 当我刚想添加的时候监听的时候 1 运行完毕后
						// 删除了 那我就注册不上了
						try{
							zk.getData(waitPath, true, new Stat());
						}catch(Exception e){
							if(zk.exists(waitPath, false)==null){
								if(checkMin(waitPath)){
									doSomething();
								}
							}
						}
						return false;
				}
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}finally{
			}
		}
		/**
		 * 
		* @Title: doSomething 
		* @Description: TODO(需要执行的任务) 
		* @param     
		* @return void    返回类型 
		* @throws
		 */
		private void doSomething() {
			try {
				System.out.println(Thread.currentThread().getId());
			} finally {
				try {
					//Thread.sleep(1000);// 此处必须延时 不然doSomething 执行完成线程结束 而watcher
					// 还没来的及执行
					zk.delete(thisPath, -1);
					System.out.println("删除：" + thisPath + "路径成功");
				} catch (InterruptedException | KeeperException e) {
					e.printStackTrace();
				}finally{
				}
				overCounter.countDown();
			}

		}

		@Override
		public void process(WatchedEvent even) {
			if (KeeperState.SyncConnected == even.getState()) {
				gate.countDown();
			}

			if (Event.EventType.NodeDeleted == even.getType()
					&& even.getPath().equals(waitPath)) {
				try{
					if(checkMin(waitPath)){
						doSomething();
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
				}
			}				
		}
	}
}
