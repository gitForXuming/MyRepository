package com.designPattern.proxyPattern;


public class ProxyTest {
	public static void main(String[] args) {
		Sourceabled source = new Source();
		
		Sourceabled proxy = new Proxy(source); //通过代理类去执行方法
		
		proxy.proxyMethod();
	}
}