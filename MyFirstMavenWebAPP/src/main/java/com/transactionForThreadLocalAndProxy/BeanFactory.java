package com.transactionForThreadLocalAndProxy;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class BeanFactory {
	// Bean容器
	private final static Map<String, Object> beanContainer = new HashMap<String, Object>();

	// 初始化创建两个代理对象
	static {
		Dao dao1 = new DaoImpl1();
		Object dao1Proxy = createTransactionProxy(dao1);
		beanContainer.put("dao1", dao1Proxy);

		Dao dao2 = new DaoImpl2();
		Object dao2Proxy = createTransactionProxy(dao2);
		beanContainer.put("dao2", dao2Proxy);
	}

	// 创建代理对象
	private static Object createTransactionProxy(Object target) {
		// 使用 Proxy.newProxyInstance 方法创建一个代理对象
		Object proxy = Proxy.newProxyInstance(target.getClass()
				.getClassLoader(), target.getClass().getInterfaces(),
				new TransactionProxy(target));
		return proxy;
	}

	// 获取Bean
	public static Object getBean(String id) {
		return beanContainer.get(id);
	}
}
