package com.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class ServerSocketServer {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		InputStream in = null;
		OutputStream out = null;
		try{
			serverSocket = new ServerSocket(8086);
			int recvMsgSize = 0;
			byte[] recvBuf = new byte[1024];
			while(true){
				Socket socket = serverSocket.accept();
				SocketAddress clientAddress = socket.getRemoteSocketAddress();
	            System.out.println("Handling client at "+clientAddress);
	            in = socket.getInputStream();
	           /* while((recvMsgSize=in.read(recvBuf))<=3){
	                   byte[] temp = new byte[recvMsgSize];
	                   System.arraycopy(recvBuf, 0, temp, 0, recvMsgSize);
	                   System.out.println(new String(temp));
	               }*/
	            //in.close();
	            
	            out = socket.getOutputStream();
	            out.write("456".getBytes());
	            out.flush();
	            out.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				serverSocket.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
