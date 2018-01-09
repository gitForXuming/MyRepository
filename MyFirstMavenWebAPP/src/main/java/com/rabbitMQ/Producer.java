package com.rabbitMQ;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
	private static final Logger logger = Logger.getLogger(Producer.class);
	public static final String QUEUE_NAME = "hello";
	public static final String MQ_HOST = "127.0.0.1";
	//指定创建线程的总个数
	public static final int count =5000;
	//5000个并发SUCCESS 都出现了线程不安全 用 volatile修饰都不行
	//public static volatile int SUCCESS = 0;
	
	public static volatile AtomicInteger SUCCESS = new AtomicInteger(0); 
	
	public static Channel channel;
	public static void main(String[] args) {
		
		try{

			
			//创建MQ连接
			ConnectionFactory factory = new ConnectionFactory();
			
			//设置MQ 主机地址
			factory.setHost(MQ_HOST);
			
			// 创建一个连接
			Connection connection = factory.newConnection();
			
			 //创建一个渠道
			channel = connection.createChannel();
			
			//创建一个队列
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			Thread t1 = new Thread(new testInterruptThread());
			t1.start();
			t1.interrupt();
			
			Executors.newCachedThreadPool();
			/*//发送的消息
			String message = "hello world!";
			//往队列中发出一条消息
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());*/
			
			/*
			 * 第一个参数：5:核心线程大小（当任务够多是同时存在多少个线程执行当前任务）
			 * 第二个参数：10 最大线程数 （当线程数超过核心线程大小 且队列也满了（最后一个）那么就在创建 10-5个）
			 * 线程空闲最大时间，超过就直接死掉
			 * 时间单位
			 * 线程等待队列
			 */
			ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 100, 20, TimeUnit.MILLISECONDS, 
	                 new ArrayBlockingQueue<Runnable>(5000));
			int i =0;
			long time = 0l;
			long beging  = System.currentTimeMillis();
			while(i<count){
				i++;
				//放入线程池
				Thread t = new Thread(new Send("xu", Integer.toString(i)),"testThread"+i);
				executor.execute(t);
				logger.info("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
			             executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
			}
			
			while(SUCCESS.get()<5000){
				//等待所有线程执行结束
				logger.info("成功发送了：" + SUCCESS +"个消息了");
			}
			
			long end = System.currentTimeMillis();
			time = end - beging;
			logger.info("发送："+ i+ "个消息耗时：" + time +" 毫秒；");
			//关闭频道和连接
			channel.close();
			connection.close();

		}catch(Exception e){
			
		}
	}
	
	/*
	 * 将对象直接转换成字节数组
	 */
	public static byte[] ObjectTObyte(Object obj) throws Exception{
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try{
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
		}catch(Exception e){
			logger.error("对象转换字节数组失败" ,e);
			throw e;
		}finally{
			bos.close();
			oos.close();
		}
		return bos.toByteArray();
	}
	
	/**
	 * 
	 * @author lenovo
	 *消息发送线程实现
	 */
	public static class Send implements Runnable{
		private String username;
		private String password;
		
		public Send(String username ,String password){
			this.username = username;
			this.password = password;
		}
		
		@Override
		public void run() {
			TestBean tb =new TestBean();
			tb.setUsername(username);
			tb.setPassword(password);
			
			
			//往队列中发出一条消息
			try{
				channel.basicPublish("", QUEUE_NAME, null, Producer.ObjectTObyte(tb));
			}catch(Exception e){
				logger.error("消息发送队列失败！" ,e);
			}
			try{
			Thread.sleep(10000);
			}catch(Exception e){
				
			}
			/*tb =null;
			System.gc();*/
			SUCCESS.incrementAndGet();
		}
	}
	
	public static class testInterruptThread extends Thread{

		@Override
		public void run() {
			try{
				System.out.println(1);
				
				Thread.currentThread().sleep(5000);
				
				System.out.println(2);
			}catch(Exception  e){
				Thread.currentThread().interrupt();//从新设置线程的中断标志    
			}
		}
	}
}
