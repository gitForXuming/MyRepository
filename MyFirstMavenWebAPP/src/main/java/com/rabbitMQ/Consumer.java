package com.rabbitMQ;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Consumer {
	private static final Logger logger = Logger.getLogger(Consumer.class);
	//队列名称  
    private final static String QUEUE_NAME = "hello";  
    public static final String MQ_HOST = "127.0.0.1";
	public static void main(String[] args) {
		try{
			//打开连接和创建频道，与发送端一样  
	        ConnectionFactory factory = new ConnectionFactory();  
	        factory.setHost(MQ_HOST);  
	        Connection connection = factory.newConnection();  
	        Channel channel = connection.createChannel();  
	        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。  
	        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
	        System.out.println(" [*] Waiting for messages. To exit press CTRL+C"); 
	        
	        //创建队列消费者  
	        QueueingConsumer consumer = new QueueingConsumer(channel);  
	        //指定消费队列  
	        channel.basicConsume(QUEUE_NAME, false, consumer);  
	        /*第二参数控制 consumer接收到消息后是否回复 如果为true 则接收到消息后立马回复 不管处理成功与否
	         * 如果这个地方为false 则需要手动回复 如果始终没有回复那么消息将会发送到其他 consumer*/
			int i=0;
			long time = 0l;
			long beging  = System.currentTimeMillis();
			
	        while (true)  
	        {  
	        	i++;
	        	if(i>=Producer.count){
	        		break;
	        	}
	        	
                 //var body = ea.Body;
                 //var message = Encoding.UTF8.GetString(body);
	            //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）  
	            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
	            Object obj = Consumer.byteTOobject(delivery.getBody());  
	            if(null!=obj){
	            	TestBean tb =null;
	            	try{
	            		tb = (TestBean)obj;
	            	}catch(Exception e){
	            		logger.error("类型转换出错", e);
	            	}
	            	
	            	//如果basicConsume 参数为false 则此处需要手工回复 
	            	channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
	            	
	            	logger.info("第："+ i+ "个消息内容是：" + tb.getUsername() +"  "+tb.getPassword());
	            }
	        }  
	        
	    	long end = System.currentTimeMillis();
			time = end - beging;
			logger.info("处理："+ i+ "个消息耗时：" + time +" 毫秒；");
		}catch(Exception e){
			
		}
	}

	public static Object byteTOobject(byte[] b){
		ByteArrayInputStream bis =null; 
		ObjectInputStream ois =null;
		Object obj = null;
		try{
			bis = new ByteArrayInputStream(b);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		}catch(Exception e){
			logger.error("读取对象失败", e);
		}finally{
			try{
				if(null!= null){
					bis.close();
				}
				if(null!=ois){
					ois.close();
				}
				
			}catch(Exception e){
				logger.error("关闭输入流失败！" , e);
			}
		}
		return obj;
	}
}
