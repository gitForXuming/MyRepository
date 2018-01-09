package com.socket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class SocketServerTest {
	public static final String filePathRoot="E:\\tmp\\performance\\";
	private static final int HEARTBEAT_PERIOD=60000;//心跳周期 长连接维系时间
	private static JsonParser moppParser = new JsonParser();
	
	public static void main(String[] args) {
		ServerSocket server = null;
		try{
			server=new ServerSocket(3600);
		//创建一个ServerSocket在端口3600监听客户请求
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("监听失败");
		}
		
		Socket socket = null;
		
		while(true){
			try{
				socket = server.accept();
				socket.setSoTimeout(HEARTBEAT_PERIOD);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			InputStream is =null;
			OutputStream os = null;
			File file = null;
			try{
				
				is = socket.getInputStream();
				os = socket.getOutputStream();
				do{
					if(null==socket||socket.isClosed()){
						System.out.println("网络异常或者客户端主动断开");
						break;
					}
					try{
					byte[] lenHeadBuf = new byte[MoppPackage.PACKAGE_HEAD_SIZE];
					PkgUtil.readTotalBytes(is, lenHeadBuf);
					System.out.println(String.format("当前连接版本号是：%d", (int)lenHeadBuf[0]));
				  
					int bodyLen = (int)((lenHeadBuf[1]<<24)|(lenHeadBuf[2]<<16)|(lenHeadBuf[3]<<8)|(lenHeadBuf[4] & 0xff ));
					int sequence = (int)((lenHeadBuf[5]<<24)|(lenHeadBuf[6]<<16)|(lenHeadBuf[7]<<8)|(lenHeadBuf[8] & 0xff ));
				  
					if(bodyLen-MoppPackage.PACKAGE_HEAD_SIZE>0){
						byte[] body = new byte[bodyLen-MoppPackage.PACKAGE_HEAD_SIZE];
						PkgUtil.readTotalBytes(is, body);
						String bodyContent = moppParser.parse(new String(body, TcpLink.Encoding))
						.getAsJsonObject().toString();
					  
						System.out.println(String.format("发送的内容是：%s",bodyContent));
						Thread.sleep(1000);//模拟需要五秒处理完
					  
					  
						String response = "{{\"交易日期\":\"20160721\",\"交易账号\":\"123456789\",\"交易金额\":\"300000000.00\",\"交易标识\":\"1\"},{\"交易日期\":\"20160722\",\"交易账号\":\"123456789\",\"交易金额\":\"2000.00\",\"交易标识\":\"1\"}}";
						byte[] bodyBuff  =(bodyContent == null)?null:bodyContent.toString().getBytes(TcpLink.Encoding);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						try{
							byte version = 1; 
							PkgUtil.writeByte(baos, version);
							PkgUtil.writeInt(baos, (bodyBuff ==null)? MoppPackage.PACKAGE_HEAD_SIZE:
								MoppPackage.PACKAGE_HEAD_SIZE + bodyBuff.length);
							PkgUtil.writeInt(baos, sequence);
						}finally{
							baos.close();
						}
						byte[] headerBuffer = baos.toByteArray();
						PkgUtil.writeTotalBytes(os, headerBuffer);
						if(null!=bodyBuff){
							PkgUtil.writeTotalBytes(os, bodyBuff);
						}
						os.flush();
				  }else{
					  System.out.println("收到客户端心跳包");
					  ByteArrayOutputStream baos = new ByteArrayOutputStream();
					  byte[] bodyBuff =null;
						try{
							byte version = 1; 
							PkgUtil.writeByte(baos, version);
							PkgUtil.writeInt(baos, (bodyBuff ==null)? MoppPackage.PACKAGE_HEAD_SIZE:
								MoppPackage.PACKAGE_HEAD_SIZE + bodyBuff.length);
							PkgUtil.writeInt(baos, sequence);
						}finally{
							baos.close();
						}
						byte[] headerBuffer = baos.toByteArray();
						PkgUtil.writeTotalBytes(os, headerBuffer);
						os.flush();
				  		}
					}catch(SocketTimeoutException e){
						//e.printStackTrace();
						System.out.println("读超时");
					}
				}while(true);
				  
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(null!=is){
					try{
						//is.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(null!=os){
					try{
						//os.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(null!=socket){
					try{
						//socket.close();
					}catch(Exception e){
						
					}
				}
			}
		}
	}
}
