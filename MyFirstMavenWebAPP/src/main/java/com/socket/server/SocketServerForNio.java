package com.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import com.socket.MoppPackage;
import com.socket.PkgUtil;
import com.socket.TcpLink;

public class SocketServerForNio {
	public static int PACKAGE_HEAD_SIZE = 9;
	
	public static void main(String[] args) {
		Selector selector = null;
		ServerSocketChannel serverSocketChannel = null;
		List<SelectionKey> keysConllection = new ArrayList<SelectionKey>();
		
		try{
			
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			//设置通道不堵塞
			serverSocketChannel.configureBlocking(false);
			InetSocketAddress isa = new InetSocketAddress("127.0.0.1",3600);
			//由渠道创建serverSocket
			ServerSocket serverSocket = serverSocketChannel.socket();
			//绑定监听端口
			serverSocket.bind(isa);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			while(true){
			   int nKeys = selector.select(1000); 
			   if (nKeys == 0) { 
	                continue; 
	            } 
				for(SelectionKey sk:selector.selectedKeys()){
					System.out.println("开始处理以下key");
					System.out.println(sk);
					//处理前先从 selector中移除
					selector.selectedKeys().remove(sk);
					if(sk.isAcceptable()){//如果sk对应的Channel包含客户端的连接请求
						keysConllection.add(sk);
						// 调用accept方法接收连接，产生服务器段的SocketChennal
						SocketChannel sc = serverSocketChannel.accept();
						if(null==sc){
							continue;
						}
						// 设置采用非阻塞模式
						sc.configureBlocking(false);
						// 将该SocketChannel注册到selector
						sc.register(selector, SelectionKey.OP_READ);

					}else if(sk.isReadable()){// 如果sk对应的Channel有数据需要读取
						// 获取该SelectionKey对应的Channel，该Channel中有客户端的数据
						SocketChannel sc = (SocketChannel) sk.channel();
						ByteBuffer buff = ByteBuffer.allocate(PACKAGE_HEAD_SIZE);
						
						try{
							sc.read(buff);
							byte[] lenHeadBuf = new byte[PACKAGE_HEAD_SIZE];
							buff.flip ();
							lenHeadBuf = buff.array();
							int bodyLen = (int)((lenHeadBuf[1]<<24)|(lenHeadBuf[2]<<16)|(lenHeadBuf[3]<<8)|(lenHeadBuf[4] & 0xff ));
							int sequence = (int)((lenHeadBuf[5]<<24)|(lenHeadBuf[6]<<16)|(lenHeadBuf[7]<<8)|(lenHeadBuf[8] & 0xff ));
						  
							if(bodyLen-MoppPackage.PACKAGE_HEAD_SIZE>0){
								ByteBuffer bodyBuff = ByteBuffer.allocate(PACKAGE_HEAD_SIZE);
								byte[] body = new byte[bodyLen-MoppPackage.PACKAGE_HEAD_SIZE];
								sc.read(bodyBuff);
								bodyBuff.flip();
								body= bodyBuff.array();
								System.out.println(String.format("发送的内容是：%s",new String(body)));
							}
						}catch(IOException e){
							e.printStackTrace();
							// 从Selector中删除指定的SelectionKey
							sk.cancel();
							if (sk.channel() != null) {
								sk.channel().close();
							}

						}
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
