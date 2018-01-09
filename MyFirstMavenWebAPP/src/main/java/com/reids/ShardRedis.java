package com.reids;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class ShardRedis {
	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = 1024;
	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 200;
	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = 3000;
	private static int TIMEOUT = 3000;
	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;

	public static void main(String[] args) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(MAX_ACTIVE);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWait(MAX_WAIT);
		config.setTestOnBorrow(TEST_ON_BORROW);

		List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>();
		JedisShardInfo shardInfo1 = new JedisShardInfo("192.168.86.128", 6379);
		JedisShardInfo shardInfo2 = new JedisShardInfo("192.168.86.129", 6379);
		JedisShardInfo shardInfo3 = new JedisShardInfo("192.168.86.130", 6379);
		jedisShardInfoList.add(shardInfo1);
		jedisShardInfoList.add(shardInfo2);
		jedisShardInfoList.add(shardInfo3);

		ShardedJedisPool pool = new ShardedJedisPool(config, jedisShardInfoList);

		int count = 100000;
		CountDownLatch startGate = new CountDownLatch(1);
		CountDownLatch countDownLatch = new CountDownLatch(Runtime.getRuntime()
				.availableProcessors() + 1);
		Set<String> keySet = new HashSet<String>();
		long begin = System.currentTimeMillis();
		for (int i = 0; i < Runtime.getRuntime().availableProcessors() + 1; i++) {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						startGate.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int i = 0;
					final Random random = new Random();
					while (i < count) {
						i++;
						try {
							long key = random.nextLong();
							// keySet.add(String.valueOf(key));
							set(String.valueOf(key), "a", pool);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					countDownLatch.countDown();
				}
			}); 
			t.start();
		}

		startGate.countDown();

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(String.format(
				"%s数据异步插入%s台分布式部署的redis耗时 ：%s",
				String.valueOf(count
						* (Runtime.getRuntime().availableProcessors() + 1)),
				String.valueOf(jedisShardInfoList.size()),
				String.valueOf(end - begin)));
	}

	public static void set(String key, String value, ShardedJedisPool pool) {
		ShardedJedis shardedJedis = pool.getResource();
		shardedJedis.set(key, value);
		pool.returnResource(shardedJedis);

	}
}
