package com.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringMvcServletContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("服务器开始启动");
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("服务器即将停止");
		
	}

	

}
