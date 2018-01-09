package com.util;

/**
 * 
 * @Title: IdWorker.java
 * @Package com.util
 * @Description: TODO(Twitter 雪花算法生产唯一id)
 * @author xuming
 * @date 2017年1月7日 下午5:11:53
 * @version V1.0 第一位为未使用（实际上也可作为long的符号位）， 接下来的41位为毫秒级时间， 然后5位datacenter标识位，
 *          5位机器ID（并不算标识符，实际是为线程标识）， 然后12位该毫秒内的当前毫秒内的计数，加起来刚好64位
 */
public class IdWorker {
	//这个参数意义所在：当前时间戳减去此代码编写的时间  意义就是不用从格林威时间1970年01月01日00时00分00秒计算时间差
	//就从代码编写时间计算时间差 没太大意义 为了是最终结果值变小一点 短一点？ 不过我测试过确实会短一些
	//6223437285140987914  	从格林威开始
	//817671501045039192	从当前开始
	private final long twepoch = 1483783210651L;
	//private final long twepoch = 0L;

	private final long workerIdBits = 5L;

	private final long datacenterIdBits = 5L;
	// 直接一点long maxWorkerId =31L; 
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

	private final long sequenceBits = 12L;

	private final long workerIdShift = sequenceBits;

	private final long datacenterIdShift = sequenceBits + workerIdBits;
	//22
	private final long timestampLeftShift = sequenceBits+datacenterIdBits;
	// 相当于 11111111111 11个1
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long workerId;

	private long datacenterId;

	private long sequence = 0L;
	// 相当于1111111111111111111111111111111111111111111111111111111111111111
	// 一般是用来做位运算的 (&)
	private long lastTimestamp = -1L;

	public IdWorker(long workerId, long datacenterId) {

		if (workerId > maxWorkerId || workerId < 0) {

			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					maxWorkerId));

		}

		if (datacenterId > maxDatacenterId || datacenterId < 0) {

			throw new IllegalArgumentException(String.format(
					"datacenter Id can't be greater than %d or less than 0",
					maxDatacenterId));

		}

		this.workerId = workerId;

		this.datacenterId = datacenterId;

	}

	public synchronized long nextId() {

		long timestamp = timeGen();

		if (timestamp < lastTimestamp) {// 基本上不会小于的 为啥要这么写 难道系统会出现bug?

			throw new RuntimeException(
					String.format(
							"Clock moved backwards.  Refusing to generate id for %d milliseconds",
							lastTimestamp - timestamp));

		}

		if (lastTimestamp == timestamp) {// 如果最后一次时间戳是当前时间戳 说明这一毫秒我不是第一个跑进来的那么
											// sequence就要接着上一次做加法 要不然就是重新从0开始
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				// 这个地方为啥要判断 sequence == 0 如果计算机运行速度够快
				// 如果sequence大于4095 （4096）&4095 就等于0了 他这个地方考虑的符号
				timestamp = tilNextMillis(lastTimestamp);

			}

		} else {

			sequence = 0L;

		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift)
				| sequence;

	}

	/**
	 * 
	 * @Title: tilNextMillis
	 * @Description: TODO(只会在sequence 大于 4095的时候跑进来)
	 * @param @param lastTimestamp
	 * @param @return
	 * @return long 返回类型
	 * @throws
	 */
	protected long tilNextMillis(long lastTimestamp) {

		long timestamp = timeGen();

		while (timestamp <= lastTimestamp) {

			timestamp = timeGen();

		}

		return timestamp;

	}

	protected long timeGen() {

		return System.currentTimeMillis();

	}

	public static void main(String[] args) {

		IdWorker idWorker = new IdWorker(0, 0);

		for (int i = 0; i < 1000; i++) {

			long id = idWorker.nextId();
			System.out.println(id);
			//System.out.println(Long.toBinaryString(id));

		}

	}
}
