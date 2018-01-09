package com.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SpringMvcHttpSessionListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		// TODO Auto-generated method stub
		//System.out.println("创建了一个会话,session id:"+ event.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		//System.out.println("销毁了一个会话,session id:"+ event.getSession().getId());
	}

}
