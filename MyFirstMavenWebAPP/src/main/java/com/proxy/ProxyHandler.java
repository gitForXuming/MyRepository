package com.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class ProxyHandler implements InvocationHandler {
	public static final Logger logger = Logger.getLogger(ProxyHandler.class);
	private Object target;
	
	public ProxyHandler(Object target){
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		logger.info("开始执行"+target.getClass().getName() +"的" + method.toString() +"方法。参数："+args.toString());
		PerformanceMonitor.begin(target.getClass().getName(), method.toString());
		Object obj = method.invoke(target, args);
		PerformanceMonitor.end();
		
		return obj;
	}

}
