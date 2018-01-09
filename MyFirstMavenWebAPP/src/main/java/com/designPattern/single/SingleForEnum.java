package com.designPattern.single;

public class SingleForEnum {
	public enum Single {
		instance;
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	private static class Test extends Thread{
		private Thread t;
		public Test(String ThreadName){
			super(ThreadName);
		}
		public Test(String ThreadName ,Thread t){
			super(ThreadName);
			this.t = t;
		}
		@Override
		public void run() {
			try{
				if(null!=t){
					Thread.sleep(500);
					t.join();
				}else{
					Single.instance.setName("xu");//让线程t 修改 如果其他线程打印的name不为null 就说明是同一个实例
					                              //或者更简单的方法就是打印 Single.instance 打印的是对象地址
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println(String.format("当前线程:%s 得到的name值为：%s",Thread.currentThread().getName()
					,Single.instance.getName()==null?"null": Single.instance.getName()));
		}
	}
	public static void main(String[] args) {
		Test t = new Test("t");
		Test t1 = new Test("t1",t);
		Test t2 = new Test("t2",t1);
		Test t3 = new Test("t3" ,t2);//保证执行顺序 t1 t2 t3
		
		t.start();
		t1.start();
		t2.start();
		t3.start();
		
	}
}
