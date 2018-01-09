package com.transactionForThreadLocalAndProxy;



public class DaoImpl2 implements Dao{
	@Override
	public void doWork() {
		 System.out.println(this.getClass().getName() + "." + "doWork  Invoke");  
		  
	        String sql = "insert into userinfo (t_username ,t_password) values('xu' ,'222222')";  
	  
	        try {  
	            TransactionHelper.executeSql(sql); 
	            
	           // throw new RuntimeException();
	        } catch (Exception e) {  
	            throw new RuntimeException(e.getMessage(), e);  
	        }  
	}
}
