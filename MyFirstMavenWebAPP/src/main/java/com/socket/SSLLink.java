package com.socket;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

public class SSLLink extends TcpLink{

	private static final String TAG = "SSLLink";
	
	/**
	 * 名称：ERRMASK_SSL_BASE <br/>
	 * 类型：int <br/>
	 * 描述：SSL错误的掩码 <br/>
	 */
	public static final int ERRMASK_SSL_BASE = 0x100;
	
	/**
	 * 名称：ERR_SSL_INIT <br/>
	 * 类型：int <br/>
	 * 描述：SSL初始化错误 <br/>
	 */
	public static final int ERR_SSL_INIT = 0x101;
	
	/**
	 * 名称：ERR_SSL_CONNECT <br/>
	 * 类型：int <br/>
	 * 描述：建立SSL连接失败 <br/>
	 */
	public static final int ERR_SSL_CONNECT = 0x102;

	private X509KeyManager[] sslKeyManagers = null;
	private X509TrustManager[] sslCertTrustManagers = null;
	
	/**
	 * 构造函数：SSLLink <br/>
	 * 描述：构造SSL长连接 <br/>
	 * @param uri 服务端URI，格式为"服务端地址:服务方端口"。 <br/>
	 * @param sslKeyManager X509私钥管理器 <br/>
	 * @param sslCertTrustManager X509证书管理器 <br/>
	 */
	protected SSLLink(String uri, X509KeyManager sslKeyManager,
			X509TrustManager sslCertTrustManager) {
		super(uri);
		sslKeyManagers = (sslKeyManager == null) ? null
				: new X509KeyManager[] { sslKeyManager };
		sslCertTrustManagers = (sslCertTrustManager == null) ? null
				: new X509TrustManager[] { sslCertTrustManager };
		
	}

	public static SSLLink createInstance(String uri, X509KeyManager sslKeyManager, X509TrustManager sslCertTrustManager){
		try{
			final SSLLink link = new SSLLink(uri, sslKeyManager, sslCertTrustManager);
			new Thread() {
				@Override
				public void run() {
					try {
						link.connect();
					} catch (Exception e) {
						System.out.println("连接服务端失败");
					}
				}
			}.start();
			return link;
		}catch(Exception e){
			
		}
		return null;
	}
	
	@Override
	protected Socket createSocket() throws Exception {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(sslKeyManagers, sslCertTrustManagers, null);
			SSLSocketFactory factory = ctx.getSocketFactory();
			return factory.createSocket(getIp(), getPort());
		} catch (NoSuchAlgorithmException e) {
			System.out.println("缺少TLS算法库");
			throw new Exception("SSL初始化异常", e);
		} catch(KeyManagementException e) {
			System.out.println("connect: Exception: " + e.toString());
			throw new Exception("RSA证书及密钥初始化失败", e);
		} catch (IOException e) {
			System.out.println("connect: Exception: " + e.toString());
			throw new Exception("IO异常", e);
		}
	}
	
	
	public void setX509KeyManager(X509KeyManager sslKeyManager)
	{
		sslKeyManagers = (sslKeyManager == null) ? null : new X509KeyManager[] { sslKeyManager };		
	}
	
	public void setX509TrustManager(X509TrustManager sslCertTrustManager)
	{
		sslCertTrustManagers = (sslCertTrustManager == null) ? null : new X509TrustManager[] { sslCertTrustManager };
	}
}
