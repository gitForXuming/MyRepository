package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionInterceptor extends HandlerInterceptorAdapter{
	
	private String[] allUrls;
	
	
	public String[] getAllUrls() {
		return allUrls;
	}

	public void setAllUrls(String[] allUrls) {
		this.allUrls = allUrls;
	}

	/**
	 * This implementation always returns {@code true}.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		String logon = (String)request.getSession().getAttribute("logon");
		String url = request.getRequestURI().replace(request.getContextPath(), "");
		if(url.endsWith("/add")){
			return true;
		}else{
			if("1".equals(logon)){
				System.out.println("会话有效");
				return true;
			}else{
				if(url.contains("ajax")){
					System.out.println("会话无效");
					/*request.getSession().setAttribute("errorMessage", "会话已超时");
					request.setAttribute("errorMessage", "会话已超时");*/
					return true;
				}else{
					System.out.println("会话无效");
					request.getRequestDispatcher("/jsp/sessionTimeOut.jsp").forward(request, response);
					return false;
				}
				
			}
		}
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	/**
	 * This implementation is empty.
	 */
	public void afterConcurrentHandlingStarted(
			HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
	}
}
