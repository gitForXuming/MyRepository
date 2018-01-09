package com.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.JsonObject;
import org.aspectj.apache.bcel.util.ClassLoaderRepository.SoftHashMap;

import com.model.VO.Hongbao;
import com.reids.RedisUtilForEnum.ReidsEnum;


public class Test {
	public static int count =0; 
	public final int co = 1234;
	private static final Map map = new HashMap();
	static{
		map.put("1", "2");
	}
	
	public enum ColorEnum {
        red, green, blue, yellow, black;
    }

	public enum Color {
        RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4){
        	@Override
        	public int getIndex() {
        		// TODO Auto-generated method stub
        		return super.getIndex();
        	}
        };
        // 成员变量
        private String name;
        private int index;

        // 构造方法
        private Color(String name, int index) {
            this.name = name;
            this.index = index;
        }

        // 普通方法
        public static String getName(int index) {
            for (Color c : Color.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

	public interface EunmInterface{
		void print();
	}
	
	public enum Stauts implements EunmInterface{
        SUCCESS("成功", 1), FAIL("失敗", 2), PROCESSING("交易处理中", 3), WAITINGAUTH("待授权", 4);
        // 成员变量
        private String name;
        private int value;

        // 构造方法
        private Stauts(String name, int index) {
            this.name = name;
            this.value = index;
        }

        // 普通方法
        public static String getName(int value) {
            for (Stauts c : Stauts.values()) {
                if (c.getValue() == value) {
                    return c.name;
                }
            }
            return null;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
		//重写方法
		@Override
		public void print() {
			System.out.println(String.format("name:%s,value:d%",name,value));
			JsonObject json = new JsonObject();
			json.addProperty("name",name);
			json.addProperty("value",value);
			System.out.println(json.toString());
		}
    }

	 // 构造方法

    public Test() {

    }

 
	public static void main(String[] args) {
		
		//List list =["123",12];
		/*List list =Arrays.asList("123",12);
		Collections.sort(list,(String a,String b)-> {
			return b
		});*/
		
		System.out.println();
		String payCompanyName = "上海青客公共租赁住房租赁经营管理股份有限公司".length()>10?"上海青客公共租赁住房租赁经营管理股份有限公司".substring(0, 9).concat("..."):"上海青客公共租赁住房租赁经营管理股份有限公司";
		System.out.println(payCompanyName);
		
		System.out.println("is ok");
		String date = "002216082016402114082016";
		System.out.println(date.substring(0, 2));
		System.out.println(date.substring(2, 4));
		System.out.println(date.substring(4, 6));
		System.out.println(date.substring(6, 8));
		System.out.println(date.substring(8, 12));
		
		System.out.println(date.substring(12, 14));
		System.out.println(date.substring(14, 16));
		System.out.println(date.substring(16, 18));
		System.out.println(date.substring(18, 20));
		System.out.println(date.substring(20, 24));
		if(true){
			
			TreeMap tm = new TreeMap();
			tm.put(3, 9);
			tm.put(4, 8);
			tm.put(1, 10);
			tm.put(2, 5);
			
			for(Object a:tm.keySet()){
				System.out.println(a);
			}
			
			return ;
		}
		
		
		ReidsEnum.INSTANCE.getJedis();
		ReidsEnum.INSTANCE.getJedis();
		ReidsEnum.INSTANCE.getJedis();
		
		
		EnumSet<Color> weekSet = EnumSet.allOf(Color.class);
        for (Color c : weekSet) {
            System.out.println(c);
        }

        // EnumMap的使用
        EnumMap<Color, String> weekMap = new EnumMap(Color.class);
        weekMap.put(Color.RED, "红色");
        weekMap.put(Color.GREEN, "绿色");
        // ... ...

        for (Iterator<Entry<Color, String>> iter = weekMap.entrySet().iterator(); iter.hasNext();) {
            Entry<Color, String> entry = iter.next();
            System.out.println(entry.getKey().name() + ":" + entry.getValue());
        }

        
        
		if(true){
			System.out.println(Color.RED.getName());
			System.out.println(Color.RED.getIndex());
			System.out.println(Color.RED.ordinal());
			
			System.out.println(Color.RED.getName(2));
			return;
		}
		
		for(ColorEnum color:ColorEnum.values()){
			System.out.println(color.toString());
		}
		ColorEnum c = ColorEnum.black;
		System.out.println(c.compareTo(ColorEnum.red));
		System.out.println(c.getDeclaringClass().getName());
		System.out.println(c.name());
		System.out.println(c.ordinal());
		System.out.println(ColorEnum.valueOf("red").ordinal());
		switch(c){
			case red:
				System.out.println("red");
				break;
			case green:
				System.out.println("green");
				break;
			case blue:
				System.out.println("blue");
				break;
			default:
				System.out.println("yellow or black");
		}
		
		Integer t1 = 1;
		Integer t2 = 2;
		Integer t3 = 3;
		
		System.out.println(t3==(t1+t2));
		System.out.println(t3.equals(t1+t2));
		//线程上下文类加载器 让父类加载器委派子类加载器去加载class
		Thread t = new Thread();
		t.setContextClassLoader(Thread.currentThread().getContextClassLoader());
		ClassLoader cl1 = ClassLoader.getSystemClassLoader();
		ClassLoader cl2 = Test.class.getClassLoader();
		ClassLoader cl3 = Thread.currentThread().getContextClassLoader();
		System.out.println(cl1);
		System.out.println(cl2);
		System.out.println(cl3);
		
		System.out.println(cl1==cl2);
		System.out.println(cl1==cl3);
		Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
		t.setContextClassLoader(ClassLoader.getSystemClassLoader());
		
		System.out.println(true&false);
		Object o = new Object();
		System.out.println("Object hashcode: " + o.hashCode());
		System.out.println("Object hashcode Binary: " + Integer.toBinaryString(o.hashCode()));
		
		Hongbao hob = new Hongbao();
		System.out.println("Object hashcode: " + hob.hashCode());
		System.out.println("Object hashcode Binary: " + Integer.toBinaryString(hob.hashCode()));
		
		Integer i1 = 1;
		Integer i2 = 1;
		int i3 = 2;
		Integer i4 = 255;
		Integer i5 = 255;
		System.out.println(i1==i2);
		System.out.println(i4==i5);
		System.out.println(i1>i2);
		
		
		System.out.println(new Test().toString());
		System.out.println(new Test().hashCode());
		
		Hashtable table = new Hashtable();
		table.put("", "");
		AtomicIntegerArray aa = new AtomicIntegerArray(5);
		AtomicInteger ai = new AtomicInteger(1);
		System.out.println(ai.addAndGet(3));
		
		AtomicReference<String> ar = new AtomicReference<String>();
		ar.set("a");
		for(;;){
			if(ar.compareAndSet("a", "b")){
				break;
			}
		}
		
		URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
		for (int i = 0; i < urls.length; i++) {  
		    System.out.println(urls[i].toExternalForm());  
		}
		System.out.println(Test.class.getClassLoader());
		
		try {
			Class<?> cls  = Class.forName("com.test.Outer");
			
			//System.out.println(cls);
			Class<?> cls1  =Test.class.getClassLoader().loadClass("com.test.Outer");
			System.out.println(cls);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(String.class.getClassLoader());
		try {
			List<String> list = new ArrayList<String>();
			list.add("1");
			list.add("2");
			list.add("3");
			list.remove(0);
			CopyOnWriteArrayList clist = new CopyOnWriteArrayList();
			//clist.add(e);
			System.out.println(list.get(0));
			Object[] ob = list.toArray();
			System.out.println(ob);
			"123".equals("456");
			List<String> ldlist = new LinkedList<String>();
			ldlist.add("");
			Map<Object, Object> objectMap = new WeakHashMap<Object, Object>(); 
			for (int i = 0; i < 1000; i++) {     
				objectMap.put(String.valueOf(i), new Object()); 
				System.gc();     
				//System.out.println("Map size :" + objectMap.size()); 
			}
			
			System.out.println(map.get("1"));
			final int a = -15;
			final int ab;
			System.out.println(a >> 2);
			BufferedInputStream bis = new BufferedInputStream(null);
			Collections.sort(new ArrayList());
			ConcurrentHashMap<String, Object> cmap = new ConcurrentHashMap<String, Object>();
			cmap.put("", new Object());
			Vector<Object> vc = new Vector<Object>();
			Set<String> linkedSet = new LinkedHashSet<String>();
			System.out.println("1".hashCode());
			System.out.println("2".hashCode());
			System.out.println("3".hashCode());
			System.out.println("4".hashCode());
			linkedSet.add("2");
			linkedSet.add("4");
			linkedSet.add("1");
			linkedSet.add("3");
			linkedSet.add("1");
			System.out.println("LinkedHashSet:"+linkedSet);
			Set<String> hashSet = new HashSet<String>();
			hashSet.add("2");
			hashSet.add("4");
			hashSet.add("1");
			hashSet.add("3");
			hashSet.add("1");
			hashSet.iterator();
			System.out.println("HashSet:"+hashSet);
			
			
			TreeMap treeMap = new TreeMap();
			treeMap.put("", "");
			treeMap.remove("");
			treeMap.keySet();
			
			SoftHashMap shm = new SoftHashMap();
			shm.put("", "");
			Hongbao hb = new Hongbao();//强引用
			hb.setId("1");
			hb.setMoney("1111111111.00");
			hb.setUserId("xuming");
			vc.add(hb);
			cmap.put("test", hb);
			//hb= null;//此时 hb 变成了软引用
			hb.setId("2");
			System.gc();
			System.out.println(((Hongbao)vc.get(0)).getUserId());
			System.out.println(((Hongbao)cmap.get("test")).getUserId());
			//软引用
			Hongbao hb1 = new Hongbao();
			hb1.setId("1");
			hb1.setMoney("1111111111.00");
			hb1.setUserId("xuming");
			SoftReference<Object> softReference = new SoftReference<Object>(hb1);
			
			//弱引用
			Hongbao hb2 = new Hongbao();
			hb2.setId("1");
			hb2.setMoney("1111111111.00");
			hb2.setUserId("xuming");
			WeakReference<Object> weakReference = new WeakReference<Object>(hb2);
			System.out.println(weakReference.get()==null?"null":((Hongbao)weakReference.get()).getUserId());
			Object obj = hb2;
			System.out.println("Object hashcode: " + Integer.toBinaryString(obj.hashCode()));
			vc.add(weakReference);
			System.out.println(((WeakReference<Hongbao>)vc.get(1)).get()==null?"null":(((WeakReference<Hongbao>)vc.get(1)).get()).getUserId());
			System.gc();
			System.out.println(((WeakReference<Hongbao>)vc.get(1)).get()==null?"null":(((WeakReference<Hongbao>)vc.get(1)).get()).getUserId());
			vc.get(1);
			System.out.println(weakReference.get()==null?"null":((Hongbao)weakReference.get()).getUserId());
			
			RandomAccessFile ra = new RandomAccessFile("E:\\nio.txt", "rw");
			FileChannel channel = ra.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(10);
			int bytesRead = channel.read(buf);
			System.out.println(bytesRead);
			
			while(bytesRead!=-1){
				buf.flip();
				byte b = buf.get();
				byte[] b1 = new byte[3];
				buf.get(b1);
				//buf.get(b1, 0, 1);
				  while(buf.hasRemaining())
	                {
	                    System.out.print((char)buf.get());
	                }
				  buf.compact();
	                bytesRead = channel.read(buf);
			}
			
			Charset cs = Charset.forName ("GBK");
			RandomAccessFile fis =  new RandomAccessFile("E:\\nio.txt","rw");
			FileChannel channel1 = fis.getChannel();
			
			long size = channel1.size();
			RandomAccessFile tofile = new RandomAccessFile("E:\\nio_out.txt", "rw");
			FileChannel outChannel = tofile.getChannel();
			outChannel.transferFrom(channel1,0, size);
			
			ByteBuffer bb = ByteBuffer.allocate(1024);
			channel1.read(bb);
			
			bb.position(5);
			bb.limit(7);
			ByteBuffer slice = bb.slice();//获得一个分片区（子缓存区）
			bb.position(0);
			bb.limit(bb.capacity());
			
			//bb.flip();
			CharBuffer cb = cs.decode(bb); 
			System.out.println();
			while(cb.hasRemaining()){
				
				System.out.print(cb.get());
			}
			
			SocketChannel socketChannel =null;
			ByteBuffer byteBuffer =null;
			try{
				Selector selector = Selector.open();
				socketChannel = SocketChannel.open();
				socketChannel.configureBlocking(false);
				socketChannel.connect(new InetSocketAddress("168.7.62.237",3900));
				if(socketChannel.finishConnect()){
					byteBuffer = ByteBuffer.wrap("123".getBytes());
					 while(byteBuffer.hasRemaining()){
	                        System.out.println(byteBuffer);
	                        socketChannel.write(byteBuffer);
	                    }
				}
			}catch(Exception e){
				if(null!=socketChannel){
					socketChannel.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try{
			URL url = new URL("http://127.0.0.1:8080/springMvc/grabController");
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
		}catch(Exception e){
			System.out.println("请求结束");
		}*/
		/*// TODO Auto-generated method stub
		String string = "string";
		String string2 ="string";
		String string3 ="str"+"ing";
		
		System.out.println(string==string);
		System.out.println(string.equals(string2));
		System.out.println(string==string3&&string2==string3);
		int a1 =5;
		int a2 =5;
		System.out.println(a1==a2);
		
		WeakHashMap<Object , Object> whm = new WeakHashMap<Object, Object>();
		Map hm = new HashMap();
		hm.put("123", "123");
		hm.put("123", "123");
		
		//反串
		char ch[] = string.toCharArray();
		char ch1[] = new char[ch.length];
		for(int i = 0 ;i<=ch.length-1 ;i++){
			ch1[ch.length - i -1] =ch[i];
		}
		System.out.println(new String(ch1));
		
		String si[] ={"a","b","b","c","d","d"};
		
		Set set = new HashSet();
		for(String s:si){
			int begin =set.size();
			set.add(s);
			int end =set.size();
			if((begin == end)&&set.size()!=1){
				System.out.println("重复项："+s.toString());
			}
			
		}
		byte b = (byte) 1234;
		
		int o =1 ;
		int n =2;
		o= o+n;
		n = o-n;
		o= o-n;
		
		System.out.println(o );
		System.out.println(n);
				
		
		//byte 转 long最快的方式就是 或 运算
		byte by = -12;
		Long l =0l;
		Long lo;
		lo = (by|l);
		System.out.println(lo);
		System.out.println((by|l));
		
		System.out.println(b);
		
		int i = 129;
		
		 byte c1 = 46;
		 byte c2 = 95;
		 byte c3 = (byte)(c1 + c2);
		 System.out.println("c3=" + c3);
		 
		byte bb[] = Integer.toString(i).getBytes();
		//0000010011010010
		//11010010 表示负数 先取反-> 10101101 再加1-> 10101110  -46  
		
		byte y = 127;
		byte t = 127;
		//byte e = y+t;
		y+=t;
		
		String str1 ="123";
		
		System.out.println(Runtime.getRuntime().totalMemory());
		System.out.println(Runtime.getRuntime().freeMemory());
		System.out.println(Runtime.getRuntime().maxMemory());
		
		
		
		Vector vt = new Vector();
		vt.add("12");
		vt.add("34");
		
		System.out.println(vt.elementAt(1));
		Map map1 = new HashMap();
		map1.put(str1, "123");
		str1 = "456";
		System.out.println(map1.get(str1));
		
		Map table = new Hashtable();
		
		System.out.println("123".hashCode());
		System.out.println("123".hashCode());
		
		System.out.println(Integer.toBinaryString(1234));
		System.out.println(Integer.toBinaryString(-46));
		String str ="123<string name=\"123\">45782a 中</string>456\t\n789";
		System.out.println(str.replaceAll("<string name=\"123\">[\\d\\D]*</string>", ""));
//		//string.replaceAll("", "");
		System.out.println(Pattern.compile("<string name=\"123\">[\\d\\D]*</string>").matcher(str).replaceAll(""));
//		
		System.out.println(Pattern.compile("a[a-z]{0,}b").matcher("bcacccbad").replaceAll(""));
		
		System.out.println(Pattern.compile("a[a-z]b").matcher("bacbd").replaceAll(""));

		int a = 00;
		System.out.println(a);
		
		Map map = new HashMap();
		
		int[] tjjc ={2015 ,11,31 ,21 ,0 ,2016, 0, 1 ,8,0};
		
		map.put("tjjc", tjjc);
		
		map.containsKey("tjjc");
		System.out.println(((int[])map.get("tjjc"))[0]);
		
		String url ="/user/ajaxQuery";
		
		Hashtable<String, Object> ht = new Hashtable<String, Object>();
		ht.size();
		System.out.println(url.contains("ajax"));
		*/
		/*long start = System.currentTimeMillis();
		try{
			Thread.sleep(2000);
		}catch(Exception e){
			
		}
		long end = System.currentTimeMillis();
		
		System.out.println(end -start);
		int a[] = {1, 3 ,5,7};
		int b[] ={2 ,3,4,6,2,8};
		
		int c[] = new int[10];
		
		Arrays.sort(b);
		
		AtomicInteger integer = new AtomicInteger();
		System.out.println("");*/
		
		/*UserInfo user = new UserInfo();
		user.setUsername("admin");
		user.setPassword("123456");
		user.setNumber("15813841113");
		
		Field fields[] = user.getClass().getDeclaredFields();
		for(Field f:fields){
			System.out.println(f.getAnnotations()[0].annotationType());
			//System.out.println(f.getModifiers());
			if(f.getType() == String.class){
				
			}
			System.out.println(f.getName());


			System.out.println(f.getType());
		}*/
		//fileDelte(new File("E:\\tmp\\performance\\"));
		
	/*	long time = Math.round(Math.random()*1000);
		System.out.println(time);
		String a ="1";
		String b="2";
		System.out.println(a.hashCode());
		System.out.println(b.hashCode());
		
		int i=15;
		System.out.println(Math.abs(15));
		System.out.println(~i+1);
		System.out.println(-i);*/
	}
	public static void fileDelte(File file){
		if(file.isDirectory()){
			if(file.list().length>0){
				for(String fileName:file.list()){
					if(new File(file ,fileName).isDirectory()){
						fileDelte(new File(file ,fileName));
					}else{
						new File(file ,fileName).delete();
					}
				}
			}
		}else{
			file.delete();
		}
	}
}