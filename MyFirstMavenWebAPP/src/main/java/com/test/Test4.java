package com.test;

public class Test4 {
    public void set(User user){
        user.setName("hello world");
        user= new User();
        user.setName("change");
    }
    
    public static void main(String[] args) {
    	
    	
        
        Test4 test = new Test4();
        User user = new User();
        test.set(user);
        System.out.println(user.getName());
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				Integer i =5;
				String s ="123";
				synchronized (s) {
					try{
						System.out.println("线程1 拿到对象锁");
						Thread.sleep(5000);
					}catch(InterruptedException e){
						
					}
				}
				System.out.println("线程1执行完毕");
			}
		}).start();
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				Integer i =5;
				String s ="123";
				synchronized (s) {
					try{
						System.out.println("线程2 拿到对象锁");
					}catch(Exception e){
						
					}
				}
				
				System.out.println("线程2执行完毕");
			}
		}).start();;
    }
    
    
   static class User{
    	 private String name;

    	    public String getName() {
    	        return name;
    	    }

    	    public void setName(String name) {
    	        this.name = name;
    	    }

    }
}
