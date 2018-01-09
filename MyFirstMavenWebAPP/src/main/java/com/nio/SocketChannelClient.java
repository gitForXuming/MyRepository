package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class SocketChannelClient {
	public static void main(String[] args) {
		SocketChannel socketChannel =null;
		ByteBuffer byteBuffer =null;
		try{
			Selector selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress("127.0.0.1",3600));
			if(socketChannel.finishConnect()){
				byteBuffer = ByteBuffer.wrap("123".getBytes());
				 while(byteBuffer.hasRemaining()){
                       // System.out.println(byteBuffer);
                        socketChannel.write(byteBuffer);
                    }
				/* Thread.sleep(5000);
				 ByteBuffer bb = ByteBuffer.allocate(1024);
				 socketChannel.read(bb);
				 bb.flip();
				 Charset cs = Charset.forName ("UTF-8");
				 CharBuffer cb = cs.decode(bb); 
				 while (cb.hasRemaining()) {
					System.out.print(cb.get());
				}*/
			}
		}catch(Exception e){
			e.printStackTrace();
			if(null!=socketChannel){
				try {
					socketChannel.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
