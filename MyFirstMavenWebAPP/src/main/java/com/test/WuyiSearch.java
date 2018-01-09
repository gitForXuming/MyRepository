package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.util.StringUtil;

/**
 * 
* @Title: WuyiSearch.java 
* @Package com.test 
* @Description: 大数据去重复
* @author xuming  
* @Date 2017年3月12日 下午12:17:37 
* @Version V1.0
 */
public class WuyiSearch {
	/**
	 * 总记录数
	 */
	public static final int RECORD = 50000;
	/**
	 * 内存中至多可以保存的记录数
	 */
	public static volatile int RECORD_IN_MEMORY_MAX = 5000;
	/**
	 * 拆分片段数 通过计算 record/sliceCount + 1 
	 */
	public static final int SLICE_RECORD=5;//大写
	/**
	 * 主文件是否还有记录未被解析
	 */
	public static volatile boolean FLAG = true;
	/**
	 * 主文件是否还有记录未被解析
	 */
	public static volatile boolean WRITE_NOW = false;
	/**
	 * 当前内存中记录数
	 */
	public static volatile AtomicInteger RECORD_IN_MEMORY = new AtomicInteger(0);
	/**
	 * 当前已经查出重复的片段 序列  默认升序
	 */
	public static volatile AtomicInteger THIS_SLICE =new AtomicInteger(0);
	/**
	 * 重复记录集合
	 */
	public static  Set<Integer> REPEAT = new HashSet<Integer>();
	
	public static Map<Integer ,List<Integer>> MAP;
	
	public static Set<Thread> THREAD_SET = new HashSet<Thread>();
	/**
	 *线程启动结束屏障
	 */
	public static  CountDownLatch START_GATE ;
	/**
	 * 锁
	 */
	public static Lock LOCK = new ReentrantLock();
	public static Condition MEMORY_IS_EMPTY = LOCK.newCondition();
	public static Condition MEMORY_NOT_EMPTY = LOCK.newCondition();
	
	static{
		START_GATE = new CountDownLatch(Runtime.getRuntime().availableProcessors());
	}
	public static void main(String[] args) {
		Long beginTime = System.currentTimeMillis();
		File file;
		FileOutputStream fos =null;
		FileChannel foc = null;
		ByteBuffer b = null;
		try{
			file = new File("E://tmp//wuyi");
			if(file.isDirectory()){
				for(File f:file.listFiles()){
					f.delete();
				}
			}
			file = new File("E://tmp//wuyi//wuyi.txt");
			fos = new FileOutputStream(file);
			foc = fos.getChannel();
			if(!file.exists()){
				file.createNewFile();
			}
			Random random = new Random();
			for(int i=0;i<RECORD;i++){
				int tmp = random.nextInt(RECORD);
				byte[] byteTmp = Integer.toString(tmp).getBytes("UTF-8");
				b= ByteBuffer.allocate(byteTmp.length+"\r\n".getBytes().length);
				b.put(byteTmp);
				b.put("\r\n".getBytes());
				b.flip();//由写入模式改成读模式
				foc.write(b);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(fos!=null){
					fos.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(foc!=null){
					foc.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Long endTime = System.currentTimeMillis();
		System.out.println(String.format("生成:%d条int类型记录耗时:%s 毫秒", RECORD ,endTime-beginTime));
		
		//ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		beginTime = System.currentTimeMillis();
		MAP = new ConcurrentHashMap<Integer, List<Integer>>();
		for(int i=0;i<SLICE_RECORD;i++){
			MAP.put(i, new LinkedList<Integer>());
		}
		
		RandomAccessFile raf =null;
		try{
			//空起线程 wait
			for(int i=0 ;i< Runtime.getRuntime().availableProcessors();i++){
				Thread none = new Thread(new ProcessThread());
				THREAD_SET.add(none);
				none.join();
				none.start();
			}
			Thread.sleep(1000);//让主线程先睡眠等待一下 有可能工作线程都还没有进入等待主线程就执行到了 signalAll  导致所有线程都假死
			                   //最好是通过countDown
			System.out.println("线程启动完毕");
			raf = new RandomAccessFile(new File("E://tmp//wuyi//wuyi.txt"), "r");
			String line = raf.readLine();
			
			LOCK.lock();
			while(!StringUtil.isEmpty(line)){
				try{
					int temp = 0;
					if(!StringUtil.isEmpty(line)){
						temp = new Integer(line).intValue();
					}
					
					if(RECORD_IN_MEMORY.intValue()>=RECORD_IN_MEMORY_MAX){
						for(Thread t:THREAD_SET){
							Thread.State  state = t.getState();
							System.out.println(state.name()+"的线程：" + t.getName());
						}
						
						//达到内存可保存的最大记录数  强制将内存中记录写到文件
						WRITE_NOW = true;
						THIS_SLICE.set(0);
						MEMORY_NOT_EMPTY.signalAll();
						MEMORY_IS_EMPTY.await();
					}
					LinkedList<Integer> list = (LinkedList<Integer>)MAP.get(temp%SLICE_RECORD);
					synchronized(list){//必须上锁 可能线程还在执行 copy到 tmpList
						list.add(temp);
						//volatile + 原子操作 保证线程安全
						RECORD_IN_MEMORY.incrementAndGet();
					}
					line = raf.readLine();
				}finally{
					
				}
			}
			FLAG = false;
			WRITE_NOW = true;
			MEMORY_NOT_EMPTY.signalAll();
			LOCK.unlock();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(null!=raf){
					raf.close();
				}
			}catch(Exception e){
				e.printStackTrace();	
			}
		}
		for(Thread t:THREAD_SET){
			Thread.State  state = t.getState();
			if(state ==Thread.State.WAITING ){
				//如果线程处于阻塞状态 强制中断限额
				//System.out.println("阻塞的线程：" + t.getName());
			/*	StackTraceElement stack[]  = t.getStackTrace();
				System.out.println("#################阻塞线程堆栈 begin##############################");
				for(StackTraceElement element:stack){
					System.err.println(element.getClassName()+":"+element.getMethodName()+"(" + element.getLineNumber()+")");
				}
				System.out.println("#################阻塞线程堆栈 end################################");*/
				//两种方式 要么再通知一次 要么暴力中断 
				LOCK.lock();
				MEMORY_NOT_EMPTY.signalAll();
				LOCK.unlock();
				
				//直接interupt  在线程中显示捕获一下异常
				//t.interrupt();
			}
		}
		try{
			START_GATE.await();
		}catch(Exception e){
			e.printStackTrace();
		}
		THIS_SLICE.set(0); //初始化一下 其他地方有用到可能未复原到0值
		START_GATE = new CountDownLatch(Runtime.getRuntime().availableProcessors());
		for(int i=0 ;i< Runtime.getRuntime().availableProcessors();i++){
			Thread t = new Thread(new SearchRepeatThread());
			try{
				t.join();
				t.start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
			START_GATE.await();
		}catch(Exception e){
			
		}
		endTime = System.currentTimeMillis();
		
		System.out.println(String.format("总耗时：%s 毫秒", endTime-beginTime));
		System.out.println("重复记录数："+ REPEAT.toArray().length);
		System.out.println("重复记录："+ Arrays.toString(REPEAT.toArray()));
	}
	
	/**
	 * 
	* @Title: WuyiSearch.java 
	* @Package com.test 
	* @Description: 拆分slice 线程
	* @author xuming  
	* @Date 2017年3月16日 上午11:04:24 
	* @Version V1.0
	 */
	public static class ProcessThread implements Runnable{
		
		private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<SimpleDateFormat>(){//SimpleDateFormat线程不安全
			@Override
			protected SimpleDateFormat initialValue() {
				return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			}
		}; 
		
		@Override
		public void run() {
			String currentDate = tl.get().format(new Date());
			File file = null;
			List<Integer> list ;
			List<Integer> tmpList =null;
			
			try{
				while(FLAG){//如果主文件还有记录 线程持续扫描哪些片段需要写到文件中
					if(WRITE_NOW){//需要强制将内存中记录全部保存到磁盘 
						if(RECORD_IN_MEMORY.intValue()==0){
							try{
								LOCK.lock();
								if(WRITE_NOW){//双检
									WRITE_NOW = false;
									MEMORY_IS_EMPTY.signalAll();
									if(FLAG){
										MEMORY_NOT_EMPTY.await();
									}
								}
							}finally{
								LOCK.unlock();
							}
							continue;
						}
						
						int index = 0;
						System.out.println(Thread.currentThread().getName());
						if(WRITE_NOW&&THIS_SLICE.intValue()<SLICE_RECORD){
									
							index = THIS_SLICE.getAndAdd(1);
							list = MAP.get(index);
							if(null==list){
								continue;
							}
							synchronized(list){
								if(list==null||list.isEmpty()){
									continue;
								}else{
									tmpList = new LinkedList<Integer>(list);
									list.clear();
								}
							}
							
							int count = tmpList.size();
							RECORD_IN_MEMORY.addAndGet(-count);
							
							file = new File("E://tmp//wuyi//wuyi"+index +".txt");
							//写数据到对应文件
							fileWrite(file,tmpList);
							tmpList=null;
						}
						
					}else{
						while(FLAG==true&&!WRITE_NOW){
							LOCK.lock();
							MEMORY_NOT_EMPTY.await();
							LOCK.unlock();
						}
					}
				}
				//flag = false
				// 主文件已经解析完毕 存在片段条数未达到sliceCount 最大条数的情况
				int index = 0;
				while(!MAP.isEmpty()){//ConcurrentHashMap 不用synchronized
					Set<Integer> set = new HashSet<Integer>(MAP.keySet());//不new会报错 要么new 要么加锁synchronized
					if(set!=null && !set.isEmpty()){
						index =(int)set.toArray()[0];
						tmpList = MAP.remove(index);
						if(tmpList==null||tmpList.isEmpty()){
							continue;
						}else{
							file = new File("E://tmp//wuyi//wuyi"+index +".txt");
							//写数据到对应文件
							fileWrite(file,tmpList);
							RECORD_IN_MEMORY.addAndGet(-tmpList.size());
							tmpList=null;
						}
					}
				}
			}catch(InterruptedException ie){
				//强制中断线程 下面方法不能省略
				Thread.currentThread().interrupt();
			}finally{
				System.out.println("线程： "+ Thread.currentThread().getName() +"将关闭！");
				START_GATE.countDown();
			}
		}
	}
	
	/**
	 * 
	* @Title: WuyiSearch.java 
	* @Package com.test 
	* @Description: 读取slice 排序 查重复
	* @author xuming  
	* @Date 2017年3月16日 上午10:50:29 
	* @Version V1.0
	 */
	public static class SearchRepeatThread implements Runnable{
		@Override
		public void run() {
			while(THIS_SLICE.intValue()<SLICE_RECORD){
				int index ;
				index = THIS_SLICE.getAndAdd(1);
				
				File file =new File("E://tmp//wuyi//wuyi"+index +".txt");
				
				List<Integer> list = fileReader(file);
				if(null==list || list.isEmpty()){
					continue;
				}
				//排序 - 快排
				//quickSort(list, 0, list.size()-1);// 当 n 很大时快排性能不如归并
				Collections.sort(list); //Collections.sort 默认使用的是归并排序 但是归并排序会额外增加内存的使用量
				int i =0;
				while(i<list.size()-2){
					if(list.get(i).intValue()== list.get(i+1).intValue()){
						REPEAT.add(list.get(i));
					}
					i++;
				}
			}
			START_GATE.countDown();
		}
	} 
	

	/**
	 * 
	* @Title: fileWrite 
	* @Date 2017年3月15日 上午10:26:53 
	* @Description: 写记录到文件
	* @Param @param file
	* @Param @param list    
	* @Return void    返回类型 
	* @Throws
	 */
	public static void fileWrite(File file ,List<Integer> tmpList){
		//write for nio
		FileOutputStream fos = null;
		FileChannel channel = null;
		ByteBuffer buff;
		try{
			fos = new FileOutputStream(file,true);
			channel = fos.getChannel();
			
			for(Iterator<Integer> it = tmpList.iterator();it.hasNext();){
				String temp = Integer.toString(it.next());
				byte[] b = temp.getBytes();
				buff = ByteBuffer.allocate(b.length+"\r\n".getBytes().length);
				buff.put(b);
				buff.put("\r\n".getBytes());
				buff.flip();
				channel.write(buff);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=channel){
				try{
					channel.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null!=fos){
				try{
					fos.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
	}
	
	/**
	 * 
	* @Title: fileReader 
	* @Date 2017年3月16日 上午10:50:06 
	* @Description: 读文件
	* @Param @param file
	* @Param @return    
	* @Return List<Integer>    返回类型 
	* @Throws
	 */
	public static List<Integer> fileReader(File file){
		Reader reader =null;
		BufferedReader br =null;
		List<Integer> list = new ArrayList<Integer>();
		try{
			reader = new FileReader(file);
			br = new BufferedReader(reader);
			String line = br.readLine();
			while(null!=line&&!StringUtil.isEmpty(line)){
				list.add(new Integer(line).intValue());
				line = br.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=br){
				try{
					br.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null!=reader){
				try{
					reader.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	/**
	 * 
	* @Title: quickSort 
	* @Date 2017年3月16日 上午11:10:56 
	* @Description: 快速排序
	* @Param @param list
	* @Param @param start
	* @Param @param end
	* @Param @return    
	* @Return List<Integer>    返回类型 list
	* @Throws
	 */
	public static List<Integer> quickSort(List<Integer> list,int start ,int end){
		int i ,j ;
		i = start;
		j = end;
		while(i<j){
			while(i<j && list.get(i).intValue()<=list.get(j).intValue()){
				j--;
			}
			if(i<j){
				int temp = list.get(i);
				list.set(i, list.get(j));
				list.set(j, temp);
			}
			
			while(i<j&&list.get(i).intValue()<list.get(j).intValue()){
				i++;
			}
			if(i<j){
				int temp = list.get(i);
				list.set(i, list.get(j));
				list.set(j, temp);
			}
		}
		if(i-start >1){
			quickSort(list ,0 ,i-1);
		}
		if(end -j >1){
			quickSort(list,j+1 ,end);
		}
		
		//System.out.println("排序后结果："+Arrays.toString(list.toArray()));
		return list;
	}
}
