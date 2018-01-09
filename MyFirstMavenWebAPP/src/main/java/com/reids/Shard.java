package com.reids;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 *
* @Title: Shard.java
* @Package com.reids
* @Description: 一致性hash
* @author xuming
* @Date 2016年12月28日 下午2:56:33
* @Version V1.0
 */
public class Shard<N> {
	private TreeMap<Long, Shard.Node> nodes; // 虚拟节点
	private List<Shard.Node> shards; // 真实机器节点
	private final static int SHARD_NUM = 2;
	private final static int NODE_NUM = 10; // 每个机器节点关联的虚拟节点个数
	private boolean flag = false;

	public Shard(List<Shard.Node> shards) {
		super();
		this.shards = shards;
		init();
	}

	private void init() { // 初始化一致性hash环
		nodes = new TreeMap<Long, Node>();
		for (int i = 0; i != shards.size(); ++i) { // 每个真实机器节点都需要关联虚拟节点
			Shard.Node shardInfo = shards.get(i);

			for (int n = 0; n < NODE_NUM; n++)
				// 一个真实机器节点关联NODE_NUM个虚拟节点
				nodes.put(
						hash("SHARD-" + shardInfo.getMasterHostIp() + "-NODE-" + n),
						shardInfo);
		}
		// System.out.println(nodes);
		System.out.println("虚拟节点数:" + nodes.size());
	}

	public Node getShardInfo(String key) {
		// tailMap 是treeMap的一个方法 public SortedMap<K,V> tailMap(K fromKey)
		// 返回此映射的部分视图，其键大于等于 fromKey
		SortedMap<Long, Node> tail = nodes.tailMap(hash(key)); // 沿环的顺时针找到一个虚拟节点（也就是找打印hash值但是和hash值最接近的一个值）
		if (tail.size() == 0) {
			return nodes.get(nodes.firstKey());
		}
		return tail.get(tail.firstKey()); // 返回该虚拟节点对应的真实机器节点的信息
	}

	// 增加一个主机
	public void addNode(Node node) {
		for (int i = 0; i < NODE_NUM; i++) {
			addVirtualNodes(hash("SHARD-" + node.getMasterHostIp() + "-NODE-" + i),
					node);
		}
	}

	// 增加虚拟节点
	public void addVirtualNodes(Long lg, Node node) {
		nodes.put(lg, node); // 增加节点及虚拟节点原节点和原虚拟节点不能动 不然影响命中导致雪崩
	}

	// 删除真实主机
	public void deleteNode(Node node) {
		if (null == node) {
			return;
		}
		for (int i = 0; i < NODE_NUM; i++) {
			nodes.remove(hash("SHARD-" + node.getMasterHostIp() + "-NODE-" + i));
		}
	}

	/**
	 * MurMurHash算法，是非加密HASH算法，性能很高，
	 * 比传统的CRC32,MD5，SHA-1（这两个算法都是加密HASH算法，复杂度本身就很高，带来的性能上的损害也不可避免）
	 * 等HASH算法要快很多，而且据说这个算法的碰撞率很低. http://murmurhash.googlepages.com/
	 */
	private Long hash(String key) {

		ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
		int seed = 0x1234ABCD;

		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);

		long m = 0xc6a4a7935bd1e995L;
		int r = 47;

		long h = seed ^ (buf.remaining() * m);

		long k;
		while (buf.remaining() >= 8) {
			k = buf.getLong();

			k *= m;
			k ^= k >>> r;
			k *= m;

			h ^= k;
			h *= m;
		}

		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(
					ByteOrder.LITTLE_ENDIAN);
			// for big-endian version, do this first:
			// finish.position(8-buf.remaining());
			finish.put(buf).rewind();
			h ^= finish.getLong();
			h *= m;
		}

		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		buf.order(byteOrder);
		return h;
	}

	public static void main(String[] args) {
		int count = 100;
		List<Shard.Node> shards = new ArrayList<Node>();
		for (int i = 0; i < SHARD_NUM; i++) {
			Node node = new Node("shard" + i, "192.168.86."+ String.valueOf(128 + i*2), 6379
					,"shard"+i,"192.168.86."+ String.valueOf(128 + i*2+1), 6379); //读写分离 128写 129读 130写131读
			shards.add(node);
		}

		Shard<Node> shard = new Shard(shards);
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
					Jedis jedis = null;
					Node node = null;
					while (i < count) {
						i++;
						try {
							long key = random.nextLong();
							keySet.add(String.valueOf(key));
							node = shard.getShardInfo(String.valueOf(key));
							jedis = node.getMasterJedis();
							jedis.set(String.valueOf(key), String.valueOf(key)); //添加到redis
							UUID uuid = UUID.randomUUID();
							//jedis.expire(String.valueOf(key), 60);//设置有效期60秒
							node.returnMasterResource(jedis);
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
				String.valueOf(shards.size()), String.valueOf(end - begin)));

		for (Node s : shards) {
			Jedis jedis = s.getSlaveJedis();

			System.out.println(jedis.keys("*").size());
			s.returnSlaveResource(jedis);
		}
		begin = System.currentTimeMillis();
		Jedis jedis = null;
		boolean f = false;
		for (String key : keySet) {
			Node n = shard.getShardInfo(String.valueOf(key));
			jedis = n.getSlaveJedis();
			if (!key.equals(jedis.get(key))) {
				System.out.println("存在差错");
				f=true;
			}
			n.returnSlaveResource(jedis);
		}
		if(f){
			System.out.println("数据插入存在差错");
		}
		end = System.currentTimeMillis();
		System.out.println(String.format(
				"检索%s数据在插入%s台分布式部署的redis总耗时： %s",
				String.valueOf(count
						* (Runtime.getRuntime().availableProcessors() + 1)),
				String.valueOf(shards.size()), String.valueOf(end - begin)));
		// flushall 删除所有

	}

	/**
	 * 分布式读写分离
	 * @author lenovo
	 *
	 */
	private static class Node {
		// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
		private static int MAX_ACTIVE = 100;
		// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
		private static int MAX_IDLE = 200;
		// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
		private static int MAX_WAIT = 10000;
		private static int TIMEOUT = 5000;
		// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
		private static boolean TEST_ON_BORROW = true;

		private String masterHostName;
		private String masterHostIp;
		private int masterHostPort;

		private String slaveHostName;
		private String slaveHostIp;
		private int slaveHostPort;
		//redis写对象池
		private volatile JedisPool jedisMasterPool = null;
		//redis读对象池
		private volatile JedisPool jedisSlavePool = null;

		public Node(String masterHostName, String masterHostIp, int masterHostPort,
				String slaveHostName, String slaveHostIp, int slaveHostPort) {
			super();
			this.masterHostName =masterHostName;
			this.masterHostIp =masterHostIp;
			this.masterHostPort =masterHostPort;
			this.slaveHostName =slaveHostName;
			this.slaveHostIp =slaveHostIp;
			this.slaveHostPort =slaveHostPort;
			initPool();
		}

		/**
		 *  初始化连接池
		 */
		private void initPool() {
			try {
				JedisPoolConfig masterConfig = new JedisPoolConfig();
				masterConfig.setMaxActive(MAX_ACTIVE);
				masterConfig.setMaxIdle(MAX_IDLE);
				masterConfig.setMaxWait(MAX_WAIT);
				masterConfig.setTestOnBorrow(TEST_ON_BORROW);
				jedisMasterPool = new JedisPool(masterConfig, masterHostIp, masterHostPort, TIMEOUT);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				JedisPoolConfig slaveConfig = new JedisPoolConfig();
				slaveConfig.setMaxActive(MAX_ACTIVE);
				slaveConfig.setMaxIdle(MAX_IDLE);
				slaveConfig.setMaxWait(MAX_WAIT);
				slaveConfig.setTestOnBorrow(TEST_ON_BORROW);
				jedisSlavePool = new JedisPool(slaveConfig, slaveHostIp, slaveHostPort, TIMEOUT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 获取写Jedis实例
		 *
		 * @return
		 */
		public Jedis getMasterJedis() {
			try {
				if (jedisMasterPool != null) {
					return jedisMasterPool.getResource();
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 释放写jedis资源
		 *
		 * @param jedis
		 */
		public void returnMasterResource(Jedis jedis) {
			if (jedis != null && null != jedisMasterPool) {
				jedisMasterPool.returnResource(jedis);
			}
		}

		/**
		 * 获取写Jedis实例
		 *
		 * @return
		 */
		public Jedis getSlaveJedis() {
			try {
				if (jedisSlavePool != null) {
					return jedisSlavePool.getResource();
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 释放写jedis资源
		 *
		 * @param jedis
		 */
		public void returnSlaveResource(Jedis jedis) {
			if (jedis != null && null != jedisSlavePool) {
				jedisSlavePool.returnResource(jedis);
			}
		}

		public String getMasterHostName() {
			return masterHostName;
		}

		public void setMasterHostName(String masterHostName) {
			this.masterHostName = masterHostName;
		}

		public String getMasterHostIp() {
			return masterHostIp;
		}

		public void setMasterHostIp(String masterHostIp) {
			this.masterHostIp = masterHostIp;
		}

		public int getMasterHostPort() {
			return masterHostPort;
		}

		public void setMasterHostPort(int masterHostPort) {
			this.masterHostPort = masterHostPort;
		}

		public String getSlaveHostName() {
			return slaveHostName;
		}

		public void setSlaveHostName(String slaveHostName) {
			this.slaveHostName = slaveHostName;
		}

		public String getSlaveHostIp() {
			return slaveHostIp;
		}

		public void setSlaveHostIp(String slaveHostIp) {
			this.slaveHostIp = slaveHostIp;
		}

		public int getSlaveHostPort() {
			return slaveHostPort;
		}

		public void setSlaveHostPort(int slaveHostPort) {
			this.slaveHostPort = slaveHostPort;
		}
	}
}
