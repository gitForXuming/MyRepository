package com.reids;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
	
public class RedisUtilForEnum{
	public static void main(String[] args) {
		Jedis jedis = ReidsEnum.INSTANCE.getJedis();
		Set<String> set = jedis.smembers("bankFlag");
		for(String bankFlag:set){
			System.out.println(bankFlag);
		}
		ReidsEnum.INSTANCE.returnResource(jedis);
	}
	
	public static enum ReidsEnum{
	//唯一的实例
	INSTANCE;
	
	private String ADDR = "168.7.56.122";
	   
	//Redis的端口号 后期配置到app.properties
	private int PORT = 6379;
	   
	//访问密码 需要在reids.conf里面配置启用密码
	private String AUTH = "admin";
	   
	//可用连接实例的最大数目，默认值为8；
	//如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private int MAX_ACTIVE = 1024;
	   
	//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private int MAX_IDLE = 200;
	   
	//等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private int MAX_WAIT = 10000;
	   
	private int TIMEOUT = 10000;
	   
	//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private boolean TEST_ON_BORROW = true;
	   
	//此处不用volatile修饰 枚举在是jvm层面的线程安全
	private JedisPool jedisPool = null;
	
	private ReidsEnum(){
		System.out.println(".................初始化开始................");
		
		JedisPoolConfig config = new JedisPoolConfig();
	    config.setMaxActive(this.MAX_ACTIVE);
	    config.setMaxIdle(this.MAX_IDLE);
	    config.setMaxWait(this.MAX_WAIT);
	    config.setTestOnBorrow(this.TEST_ON_BORROW);
	    this.jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
	    
	    System.out.println(".................初始化结束................");
	   	//可要可不要 此类中只有static属性和方法
		}
		   
	   /**
	* 获取Jedis实例  仅有的两个 public 类型 
	* @return
	*/
	   public Jedis getJedis() {
	       try{
	    	   if (jedisPool != null) {
	    		   System.out.println("..............获取一个redis连接...............");
	    		   return this.jedisPool.getResource();
	    	   }else{
	    		   return null;
	    	   }
	       }catch(Exception e){
	    	   return null;
	       }
	   }
	   /**
	* 释放jedis资源
	* @param jedis
	*/
	   public void returnResource(final Jedis jedis) {
	       if (jedis != null && null != jedisPool) {
	           jedisPool.returnResource(jedis);
	       }
	   }
	}
}
