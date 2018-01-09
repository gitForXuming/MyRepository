package com.transactionForThreadLocalAndProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class TransactionProxy implements InvocationHandler{
	public static final Logger logger = Logger.getLogger(TransactionProxy.class);
	private Object target;
	
	public TransactionProxy(Object o){
		target = o;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		boolean existsTransaction =TransactionHelper.existsTransaction();
		
		if(existsTransaction==false){
			logger.info("当前事物环境没有事物，重新开启一个事物");
			TransactionHelper.beginTransaction();
			
		}else{
			logger.info("已经存在一个事物，直接加入事物即可");
		}
		
		Object result = null;
		try{
			result = method.invoke(target, args);
		}catch(InvocationTargetException e){
			 // 捕获调用目标异常，如果目标异常是运行时异常则设置回滚标志  
            Throwable cause = e.getCause();  
            if (cause instanceof RuntimeException) {  
                TransactionHelper.setRollBack(true);  
                System.out.println("出现运行时异常，事务环境被设置为必须回滚");  
            } else {  
                System.out.println("出现非运行时异常，忽略");  
            }  
		}
		
		
		logger.info("处理事物，commit or rollback");
		
		if(existsTransaction == false ){
			logger.info("当前无事物。");
		}
		else if (existsTransaction == true  
                && TransactionHelper.needRollBack() == false) {  
            TransactionHelper.commit();  
            System.out.println("事务已提交成功");  
        } else if (existsTransaction == true  
                && TransactionHelper.needRollBack() == true) {  
            TransactionHelper.rollBack();  
            System.out.println("事务已回滚");  
        } 
		
		return result;
	}

}
