package com.core;

import com.listener.AppContextInit;

public class ApplicationContextUtil {
	/***
	 * 获取Service
	 * @param beanId
	 * @return
	 */
	public static Object getBean(String beanId) {
		return AppContextInit.getContext().getBean(beanId);
	}
}
