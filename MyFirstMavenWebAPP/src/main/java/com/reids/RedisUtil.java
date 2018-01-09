package com.reids;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

	private static final Logger logger = Logger.getLogger(RedisUtil.class);
	// Redis服务器IP 后期配置到app.properties
	private static String ADDR = "192.168.86.128";

	// Redis的端口号 后期配置到app.properties
	private static int PORT = 6379;
	
	// 访问密码 需要在reids.conf里面配置启用密码
	private static String AUTH = "admin";

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = 1024;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 200;

	// 最大建立连接等待时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = 10000;
	//
	private static int TIMEOUT = 10000;

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;

	// 用volatile修饰 支持多线程
	private static volatile JedisPool jedisPool = null;

	public static void main(String[] args) {
		Set<String> bankFlagSet = new HashSet<String>(7);
		Jedis jedis = RedisUtil.getJedis();
		String ss = jedis.get("test");
		System.out.println(ss);
		//bankFlagSet = jedis.smembers("test");
		for (String s : bankFlagSet) {
			System.out.println(s);
		}

	}

	/*  
   *//**
	 * 初始化Redis连接池 etCertUserAction
	 */
	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWait(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
		} catch (Exception e) {
			logger.error("初始化Redis连接池异常", e);
			e.printStackTrace();
		}
	}

	private RedisUtil() {
		// 可要可不要 此类中只有static属性和方法
	}

	// 延时加载方法 慎用 （内部类在使用的时候才会去加载）
	static class RedisUtilHolder {
		private static final JedisPool jedisPool;
		static {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(RedisUtil.MAX_ACTIVE);
			config.setMaxIdle(RedisUtil.MAX_IDLE);
			config.setMaxWait(RedisUtil.MAX_WAIT);
			config.setTestOnBorrow(RedisUtil.TEST_ON_BORROW);
			jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
		}

	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public static Jedis getJedis() {
		/*
		 * try { if (jedisPool != null) { Jedis resource =
		 * jedisPool.getResource(); return resource; } else { return null; } }
		 * catch (Exception e) { e.printStackTrace(); return null; }
		 */

		try {
			if (jedisPool != null) {
				Jedis js = RedisUtilHolder.jedisPool.getResource();
				return RedisUtilHolder.jedisPool.getResource();
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null && null != jedisPool) {
			jedisPool.returnResource(jedis);
		}
	}
}
