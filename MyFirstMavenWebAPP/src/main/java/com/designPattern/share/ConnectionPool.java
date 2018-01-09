package com.designPattern.share;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ConnectionPool {
	//默认初始化连接池大小
	private static final int DEFAULT_POOL_SIZE = 5;
	//最大连接池大小
	private static final int MAX_POOL_SIZE = 50;
	//最大空闲连接池个数
	private static final int MAX_IDLE = 20;
	//当前总连接数 线程安全机制可以直接使用int类型
	private static volatile AtomicInteger CURRENT_POOL_SIZE = new AtomicInteger(0);
	//连接池
	private static volatile Vector<Connection> POOL = null;
	//数据库URL名
	private static final String URl ="jdbc:oracle:thin:@168.7.56.122:1521:ebank";
	//数据库驱动名
	private static final String DRIVER_CLASS ="oracle.jdbc.driver.OracleDriver";
	//数据库用户名
	private static final String USERNAME = "ebankUAT";
	//数据库密码
	private static final String PASSWORD = "ebankUAT";
	//公平锁方式 先到先得原则
	private final static ReentrantLock LOCK = new ReentrantLock(true);
	//condition 锁控制器
	private final static Condition connCondition = LOCK.newCondition();
	//通过信号量控制当前访问最大个数get时 sem.acquire release时 release
	private Semaphore sem = new Semaphore(MAX_POOL_SIZE);
	
	static{
		Connection conn = null;
		POOL = new Vector<Connection>(DEFAULT_POOL_SIZE);
		for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
			try{
				Class.forName(DRIVER_CLASS);
	            conn = DriverManager.getConnection(URl, USERNAME, PASSWORD);
	            POOL.add(conn);
	            CURRENT_POOL_SIZE.getAndIncrement();
			} catch (ClassNotFoundException e) {
				System.out.println("oracle驱动未类未找到");
                e.printStackTrace();
                break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}
	}
	//获取数据库连接对象 如果获取不到无限等待下去 直到获取到为止
	public static  Connection getConnection(){
		Connection conn =null;
		LOCK.lock();
		try{
			if(POOL.size()==0 && CURRENT_POOL_SIZE.intValue() < MAX_POOL_SIZE){
				Connection newConn = create();
				if(null != newConn){
					//此处不要直接添加到POOL 使用完毕后再去添加 不然会存在重复添加情况
					CURRENT_POOL_SIZE.getAndIncrement();
					return newConn;
				}
			}
			// 如果取不到就继续等待
			while(POOL.size()==0){
				try{
					System.out.println(Thread.currentThread().getName()+"进入等待！");
					connCondition.await();
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
			conn =  POOL.get(0);
			if(null!=conn){
				POOL.remove(conn);
			}
		}finally{
			LOCK.unlock();
		}
		return conn;
	}
	
	/*
	 * 获取数据库连接对象 超时时间 timeOut
	 * 时间单位：SECONDS
	 */
	public static  Connection getConnection(long timeOut){
			Connection conn =null;
			LOCK.lock();
			try{
				if(POOL.size()==0 && CURRENT_POOL_SIZE.intValue() < MAX_POOL_SIZE){
					Connection newConn = create();
					if(null != newConn){
						//此处不要直接添加到POOL 使用完毕后再去添加 不然会存在重复添加情况
						CURRENT_POOL_SIZE.getAndIncrement();
						return newConn;
					}
				}else{
					if(POOL.size()==0){
						try{
							connCondition.await(timeOut ,TimeUnit.SECONDS); 
							//此种设计不靠谱 假如在规定的时间内对象lock一直被其他线程持有
							//此线程将不会被唤醒 直到能获取lock锁， 那么可能等待是时间可能更长
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
					if(POOL.size()==0){
						System.out.println("获取连接池对象超时");
					}else{
						conn =  POOL.get(0);
						if(null!=conn){
							POOL.remove(conn);
						}
					}
					return conn;
				}
				
				conn =  POOL.get(0);
				if(null!=conn){
					POOL.remove(conn);
				}
				
			}finally{
				LOCK.unlock();
			}
			return conn;
		}
	/*
	 * 返回到对象连接池管理
	 */
	public static void release(Connection conn){
		LOCK.lock();
		try{
			if(POOL.add(conn)){
				System.out.println("返回对象池成功"+ POOL.size());
				try{
					connCondition.signal();//通知等待的线程重新获取conn
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				System.out.println("返回连接到连接池失败！");
				CURRENT_POOL_SIZE.getAndDecrement();
			} 
		}finally{
			LOCK.unlock();
		}
	}
	
	/*
	 * 创建新连接对象
	 */
	private static Connection create(){
		LOCK.lock();
		try{
			Connection conn = null;
			try{
				Class.forName(DRIVER_CLASS);
				conn = DriverManager.getConnection(URl, USERNAME, PASSWORD);
	            return conn;
			} catch (ClassNotFoundException e) {
				System.out.println("oracle驱动未类未找到");
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}finally{
			LOCK.unlock();
		}
		return null;
	}
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		CountDownLatch countDownLatch = new CountDownLatch(100);
		CountDownLatch startGate = new CountDownLatch(1);
		for (int i = 0; i < 100; i++) {
			Thread t = new Thread(new Task(countDownLatch,startGate),"线程"+i);
			/*try{
				t.join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}*/
			t.start();
		}
		startGate.countDown();
		
	
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(String.format("执行完毕耗时：%s", String.valueOf(end-start)));
		
		System.out.println(String.format("最终线程大小为[%d]", CURRENT_POOL_SIZE.intValue()));
		System.out.println(String.format("最终线程大小为[%d]", POOL.size()));
	}
	/**
	 * 清理空闲线程池
	 * @author lenovo
	 *
	 */
	private static class EvicationTimer{
		private static Timer timer;
		
		//delay 延时  period 周期
		static synchronized void schedule(TimerTask task ,long delay,long period){
			if(null == timer){
				timer = new Timer();
			}
			timer.schedule(task, delay, period);
		}
		static synchronized void cancel(TimerTask task){
			if(null != timer){
				task.cancel();
			}
		}
	}
	private static class Task implements Runnable{
		private CountDownLatch  countDownLatch;
		private CountDownLatch  startGate;
		public Task(CountDownLatch countDownLatch,CountDownLatch startGate){
			super();
			this.countDownLatch =countDownLatch;
			this.startGate =startGate;
		}
		int i =0;
		@Override
		public void run() {
			try {
				startGate.await();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Statement stmt=null;
			PreparedStatement pstmt =null;
			Connection conn =null;
			while(i<5000){
				i++;
				conn = ConnectionPool.getConnection(1);
				if(null == conn){
					return;
				}
				try{
					stmt=conn.createStatement();      
					//建立ProparedStatement对象      
					//String sql="select * from data_copmare where userName=? and password=?";   
					String sql="insert into data_copmare (CERTNO ,NEWCSTNO,OLDCSTNO) values ('8902188148363117187','8902188148363117187','8902188148363117187')"; 
					pstmt=conn.prepareStatement(sql);      
					//pstmt.setString(1,"admin");      
					//pstmt.setString(2,"liubin"); 
					int rs =pstmt.executeUpdate();
					
				
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try{
						stmt.close();
						pstmt.close();
					}catch(Exception e){
						e.printStackTrace();
					}
					ConnectionPool.release(conn);
				}
			}
			
			countDownLatch.countDown();
		}
	}
}
