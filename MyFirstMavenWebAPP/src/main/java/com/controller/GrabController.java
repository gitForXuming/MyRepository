package com.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import com.core.ApplicationContextUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.model.VO.Hongbao;
import com.rabbitMQ.MessageProducer;
import com.reids.RedisUtil;

public class GrabController  implements Controller{
	private String totalPeople;
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("message", "Hello World!");
		mav.setViewName("qianghongbaoSuccess");
		//final CountDownLatch latch = new CountDownLatch(count)
		
		//从reidis队列中取出红包
		Jedis jedis = RedisUtil.getJedis();
		
		jedis.lpush("hongbaolist", "{\"id\":\"01\",\"money\":\"66.66\" ,\"userId\":\"\"}");
		jedis.lpush("hongbaolist", "{\"id\":\"02\",\"money\":\"88.88\" ,\"userId\":\"\"}");
		jedis.lpush("hongbaolist", "{\"id\":\"03\",\"money\":\"99.99\" ,\"userId\":\"\"}");
		boolean b = false;
		
		Transaction tx =jedis.multi();
		tx.exec();
		b=jedis.sismember("qiangdao", "xuming");
		if(b){
			return mav;//抢到不能再抢了
		}
		String hongbao = jedis.lpop("hongbao");// redis list lpop操作是原子性  后期可以把判断 和取红包 通过lua脚本方式做成原子性
		if(null!=hongbao&&!"".equals(hongbao)){
			JsonObject json = (JsonObject)new JsonParser().parse(hongbao);
			//设置得到红包的用户ID
			json.addProperty("userId", "xuming");
			
			Hongbao hb = new GsonBuilder().create().fromJson(json,Hongbao.class);
			//将红包添加到消息队列 后期红包扣账 写数据库 入账 等操作可能比较费时 放到消息队列慢慢执行 让界面快速返回
			MessageProducer messageProducer = (MessageProducer) ApplicationContextUtil.getBean("messageProducer");
			messageProducer.sendMessageToQueueOne(hb);
			
			//抢到红包不能再抢
			jedis.sadd("qiangdao", "xuming");
		}
		
		//抢红包lua 原子操作 redis 在执行lua命令的时候不会执行任何其他命令 所以是原子操作
		String hongbaolist ="hongbaolist";
		String hongbaoConsumedList ="hongbaoConsumedList";
		String hongboConsumedMap = "hongbaoConsumedMap ";
		
		String reidsLuaScript = "" 
				+ "local consumedFlag = redis.call('hexists',KEYS[3],KEYS[4] ); --获取消费记录看看当前用户是否已经抢过\n"
				+"if consumedFlag ~= 0 then \n" + 
						"print('该用户已经抢过红包');\n" +
						"return nil;  --如果记录存在说明抢过直接返回空 nil \n" +
				"else \n" +
					"local hongbao = redis.call('lpop' ,KEYS[1]);  --从未消费的红包里面获取一个 \n" +
					"print('hongbao:',hongbao) \n" +
					"if hongbao then \n" +
						"local x = cjson.decode(hongbao); --将json格式的红包转成对象形式 \n" +
						"x['userId'] = KEYS[4]; --将取出的红包指派给当前用户  \n" +
						"print(x) \n"+
						"local re = cjson.encode(x); --将红包对象转成json格式 \n" +
						"print('最终的红包：' ,re); \n" +
						"redis.call('hmset' ,KEYS[3],KEYS[4], KEYS[4]); --将红包消费记录到map \n" +
						"redis.call('lpush',KEYS[2], re); --将红包记录到已消费集合 \n" +
						"print('红包发放成功') \n" +
						"return re; \n" +
					"end \n" +
				"end	\n"  ;
		
		Object obj = jedis.eval(reidsLuaScript,4 , hongbaolist, hongbaoConsumedList,hongboConsumedMap ,"xuming" ,"param1" ,"param2");
		JsonObject json = (JsonObject)new JsonParser().parse(obj.toString());
		Hongbao hb = new GsonBuilder().create().fromJson(json,Hongbao.class);
		MessageProducer messageProducer = (MessageProducer) ApplicationContextUtil.getBean("messageProducer");
		messageProducer.sendMessageToQueueOne(hb);
		
		
		RedisUtil.returnResource(jedis);
		System.out.println("123");
		
		return mav;
	}
	public String getTotalPeople() {
		return totalPeople;
	}
	public void setTotalPeople(String totalPeople) {
		this.totalPeople = totalPeople;
	}
}
