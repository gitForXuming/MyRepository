package com.rabbitMQ;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

//MessageListener 自动回复
//ChannelAwareMessageListener 手动回复
public class MessageConsumerrForConfirm  implements ChannelAwareMessageListener	 {

	private static final Logger logger = Logger.getLogger(Consumer.class);

	public void test(){
		System.out.println("test");
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		byte[] b = message.getBody();

		TestBean obj = (TestBean)Consumer.byteTOobject(b);
		logger.info("监听发现一个消息，消息内容：" + obj.getUsername() +"  "+obj.getPassword());

		//Thread.sleep(10000);
		logger.info("处理消息完毕");
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		logger.info("ack消息队列成功");
	}
}
