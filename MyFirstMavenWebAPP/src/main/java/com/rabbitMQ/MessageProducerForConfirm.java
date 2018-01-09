package com.rabbitMQ;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Service;

/*
 * 消息确实机制
 */
@Service
public class MessageProducerForConfirm{
	private Logger logger = Logger.getLogger(MessageProducer.class);

	@Resource
	private RabbitTemplate rabbitTemplate;


	public void sendMessageToQueueOne(Object message){
		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback(){
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (ack) {
					logger.info("消息确认发送到 queue");
				} else {
					//处理丢失的消息（nack）
					logger.info("消息未发送到 queue");
				}
			}
		});

		rabbitTemplate.convertAndSend("queueOneKey",message);
	}

	public void sendMessageToQueueTwo(Object message){
		rabbitTemplate.convertAndSend("queueTwoKey",message);
	}
}
