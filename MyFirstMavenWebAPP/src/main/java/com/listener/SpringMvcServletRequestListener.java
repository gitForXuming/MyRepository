package com.listener;

import java.util.Enumeration;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class SpringMvcServletRequestListener implements ServletRequestListener ,ServletRequestAttributeListener{
	long startTime =0;
	long endTime=0;
	long time=0;
	
	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		// TODO Auto-generated method stub
		long endTime = System.currentTimeMillis();
		//System.out.println("结束时间："+endTime );
		time = endTime-startTime;
		//System.out.println("请求结束：请求名：" + event.getServletRequest().getServerName() +"所用时间："+ time);
	}

	@Override
	public void requestInitialized(ServletRequestEvent event) {
		// TODO Auto-generated method stub
		startTime = System.currentTimeMillis();
		//System.out.println("开始时间："+startTime);
		
		
		Enumeration eu = event.getServletRequest().getAttributeNames();
		
		while(eu.hasMoreElements()){
			String name = (String)eu.nextElement();
			//System.out.println( event.getServletRequest().getAttribute(name));
			}
		HttpServletRequest httpRequest = (HttpServletRequest)event.getServletRequest();
		httpRequest.getSession().setAttribute("userName", "001");
		httpRequest.getSession().setAttribute("password", "123456");
		httpRequest.getSession().setAttribute("test", "1");
		//System.out.println("即将发生一个请求：请求名：" +httpRequest.getRequestURL()+"会话ID："+ httpRequest.getSession().getId());
	}

	@Override
	public void attributeAdded(ServletRequestAttributeEvent event) {
		
		HttpServletRequest httpRequest = (HttpServletRequest)event.getServletRequest();
		httpRequest.getSession().removeAttribute("password");
		//System.out.println("请求增加了："+event.getName()+"属性，对应值为："+event.getValue());
		
	}

	@Override
	public void attributeRemoved(ServletRequestAttributeEvent event) {
		//System.out.println("请求移除了："+event.getName()+"属性，对应值为："+event.getValue());
		HttpServletRequest httpRequest = (HttpServletRequest)event.getServletRequest();
		httpRequest.getSession().setAttribute("test", "2");;
		
	}

	@Override
	public void attributeReplaced(ServletRequestAttributeEvent event) {
		//System.out.println("请求修改了："+event.getName()+"属性，对应值为："+event.getValue());	
	}

}
