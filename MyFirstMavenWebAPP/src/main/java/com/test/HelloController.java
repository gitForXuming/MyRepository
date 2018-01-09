package com.test;

import java.nio.channels.FileChannel;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.core.ApplicationContextUtil;
import com.model.VO.UserInfoVO;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rabbitMQ.MessageProducer;
import com.rabbitMQ.MessageProducerForConfirm;
import com.rabbitMQ.TestBean;
import com.service.IUserService;

public class HelloController implements Controller{
	
	@Resource
	private IUserService userService;
	
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		try{   
		       // 连接到 mongodb 服务
				MongoClient mongoClient = new MongoClient( "127.0.0.1" , 27017 );
		       
		         // 连接到数据库
				MongoDatabase mongoDatabase = mongoClient.getDatabase("runoob");  
		        System.out.println("Connect to database successfully");
		        
		        
		        MongoCollection<Document> collection; //= //mongoDatabase.getCollection("")
		     }catch(Exception e){
		    	 System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		     }
		
		UserInfoVO user = new UserInfoVO();
		user.setUsername("xu");
		
		List<UserInfoVO> list = (List<UserInfoVO>)userService.queryUsers(user);
		
		user.setUsername("xu");
		user.setPassword("666666");
		//userService.addUser(user);
		
	/*	SqlSession dao = UserDao.getDao();
		UserInfo user = new UserInfo();
		user.setUsername("xu");
		List<UserInfo> users = dao.selectList("queryUsers", user);
		
		
		Jedis jedis = RedisUtil.getJedis();
		jedis.set("zh", "中文");
		String zh = jedis.get("zh");
		String cn = jedis.get("cn");
		//String errorMessage = new String(jedis.get("error").getBytes("GBK"),"utf-8");
		String test  = jedis.get("test");
		Map<String ,List> map = new HashMap<String, List>();*/
		
		//jedis.hmset("", map);
		
		
		ModelAndView mav = new ModelAndView();
		
		System.out.println(request.getSession().getAttribute("userName"));
		
		mav.addObject("message", "Hello World!");
		
		mav.setViewName("helloWorld");
		
		/*MessageConsumer messageConsumer =(MessageConsumer)ApplicationContextUtil.getBean("messageReceiver");
		messageConsumer.test();*/
		
		//将任务添加到消息队列
		MessageProducer messageProducer = (MessageProducer) ApplicationContextUtil.getBean("messageProducer");
		TestBean tb = new TestBean();
		tb.setUsername("xu");
		tb.setPassword("111111");
		messageProducer.sendMessageToQueueOne(tb);
		
		TestBean tb1 = new TestBean();
		tb.setUsername("xu");
		tb.setPassword("222222");
		messageProducer.sendMessageToQueueTwo(tb);
		
		
		MessageProducerForConfirm messageProducerForConfirm = (MessageProducerForConfirm) ApplicationContextUtil.getBean("messageProducerForConfirm");
		TestBean tb3 = new TestBean();
		tb.setUsername("xu");
		tb.setPassword("333333");
		messageProducerForConfirm.sendMessageToQueueOne(tb);
		
		
		return mav;
	}

}
