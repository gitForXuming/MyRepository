package com.rabbitMQ;

import com.google.gson.JsonObject;
import com.model.VO.Hongbao;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

//MessageListener 自动回复
//ChannelAwareMessageListener 手动回复
public class MessageConsumer  implements ChannelAwareMessageListener	 {

	private static final Logger logger = Logger.getLogger(Consumer.class);

	public void test(){
		System.out.println("test");
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		byte[] b = message.getBody();
		Object o = Consumer.byteTOobject(b);
		if(o instanceof TestBean){
			TestBean obj = (TestBean)o;
			logger.info("监听发现一个消息，消息内容：" + obj.getUsername() +"  "+obj.getPassword());

			//Thread.sleep(10000);
			logger.info("处理消息完毕");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			logger.info("ack消息队列成功");
		}else if(o instanceof Hongbao){
			Hongbao hb =(Hongbao)o;
			logger.info(hb.getUserId() +"抢到了一个"+hb.getMoney()+"的红包，后台处理成已经入账。");
		}else if(o instanceof JsonObject){
			JsonObject obj = (JsonObject)o;
			logger.info(obj.get("userId").toString() +"抢到了一个"+obj.get("money")+"的红包，后台处理成已经入账。");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}

	}
}
