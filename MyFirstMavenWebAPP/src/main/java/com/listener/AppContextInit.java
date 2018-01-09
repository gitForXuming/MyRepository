package com.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.base.Constants;
import com.socket.SSLLink;
import com.socket.SSLTrustContext;
import com.socket.SSLTrustManager;
import com.socket.TcpLink;


public class AppContextInit extends ContextLoaderListener {
	protected final static Logger logger = Logger.getLogger(AppContextInit.class);

	private static ApplicationContext context;

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext ctx) {
		context = ctx;
	}
	
	private SSLTrustManager sslTrustManager = null;
	private static TcpLink tcpLink = null;
	
	public static TcpLink getTcpLink() {
		return tcpLink;
	}
	@SuppressWarnings("static-access")
	public void contextInitialized(ServletContextEvent context) {

		try {
			super.contextInitialized(context);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		
		this.setContext(WebApplicationContextUtils.getRequiredWebApplicationContext(context.getServletContext()));
		
		ServletContext servletContext = (ServletContext) context.getServletContext();
		
		// 连接初始化放在最后
		/*sslTrustManager = SSLTrustContext
		.load(context.getServletContext(), null, null,  new String[] { "ssl/child-ca.cer", "ssl/root-ca.cer" });
		tcpLink = SSLLink.createInstance(Constants.PPSERVER_URI, null, sslTrustManager.x509TrustManager());*/
		
	}
}
