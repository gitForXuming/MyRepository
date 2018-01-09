package com.rabbitMQ;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
	private Logger logger = Logger.getLogger(MessageProducer.class);

	@Resource
	private AmqpTemplate amqpTemplate;

	public void sendMessageToQueueOne(Object message){
	  amqpTemplate.convertAndSend("queueOneKey",message);
	}
	
	public void sendMessageToQueueTwo(Object message){
		  amqpTemplate.convertAndSend("queueTwoKey",message);
		}
}
