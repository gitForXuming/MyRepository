package com.transactionForThreadLocalAndProxy;

public class TestTransaction {
	public static void main(String[] args) {
		Dao dao = (Dao)BeanFactory.getBean("dao1");
		
		dao.doWork();
		
		Dao dao2 = (Dao)BeanFactory.getBean("dao2");
		
		dao2.doWork();
	}
}
