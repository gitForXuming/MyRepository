package com.transactionForThreadLocalAndProxy;


public class DaoImpl1 implements Dao{

	@Override
	public void doWork() {
		 System.out.println(this.getClass().getName() + "." + "doWork  Invoke");  
		  
	        String sql = "insert into userinfo (t_username ,t_password) values('xu' ,'111111')";  
	  
	        TransactionHelper.executeSql(sql);
	        
	      /*  
	    	Dao dao = (Dao)BeanFactory.getBean("dao2");
	    	dao.doWork();*/
		
	}

}
