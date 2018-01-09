package com.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketServerTest {
	public static final String filePathRoot="E:\\tmp\\performance\\";
	
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
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			InputStream is =null;
			OutputStream os = null;
			File file = null;
			try{
				is = socket.getInputStream();
				  byte[] lenHeadBuf = new byte[1];
				  int off = 0;
				  //获得包头
				  while (off < 1) 
				  {
				   off = off + is.read(lenHeadBuf, off, (1-off));
				   if (off < 1) 
				   {   
				    throw new Exception("Socket已关闭,无法读取数据。");
				   }
				  }
				  
				  if(lenHeadBuf[0]==2){
					  byte[] fileLenBuff = new byte[4];
					  int fileLen = is.read(fileLenBuff);
					  fileLen = (int) ((fileLenBuff[3] & 0xFF) 
								| ((fileLenBuff[2] & 0xFF)<<8) 
								| ((fileLenBuff[1] & 0xFF)<<16) 
								| ((fileLenBuff[0] & 0xFF)<<24));
					  
					  byte[] fileNameBuf = new byte[fileLen];
					  
					  int fileNameoff = 0;
					  //获得包头
					  while (fileNameoff < fileLen) 
					  {
						  fileNameoff = fileNameoff + is.read(fileNameBuf, fileNameoff, (fileLen-fileNameoff));
					   if (off < 0) 
					   {   
					    throw new Exception("Socket已关闭,无法读取数据。");
					   }
					  }
					  
					  String fileName = new String(fileNameBuf);
					  System.out.println(fileName);
					  file = new File(filePathRoot+"rec_"+fileName);
					  if(!file.exists()){
						  file.createNewFile();
					  }
					  os = socket.getOutputStream();
					  os.write("ok".getBytes());
					  os.flush();
					
					  while(true){
						  byte[] buff = new byte[1];
						  int aa=0;
						  while(aa<=0){
							  aa = is.read(buff);
						  }
						  if(buff.length>0&&2==buff[0]){
							  byte[] bodyLenBuff = new byte[4];
							  
							  int off1=0;
							  while(off1<4){
								  off1 = off1 + is.read(bodyLenBuff, off1, (4-off1));
							  }
							  
							  int bodyLen = (int) ((bodyLenBuff[3] & 0xFF) 
										| ((bodyLenBuff[2] & 0xFF)<<8) 
										| ((bodyLenBuff[1] & 0xFF)<<16) 
										| ((bodyLenBuff[0] & 0xFF)<<24));
							  
							  byte[] bodyContent = new byte[bodyLen];
							  
							  int bodyOff = 0;
							  //获得包头
							  while (bodyOff < bodyLen) 
							  {
								  bodyOff = bodyOff + is.read(bodyContent, bodyOff, (bodyLen-bodyOff));
								  if (bodyOff < 0) {   
									  throw new Exception("Socket已关闭,无法读取数据。");
								  }
							  }
							  
							  OutputStream out = new FileOutputStream(file);
							  out.write(bodyContent);
							  out.flush();
							  out.close();
							  
							  //返回给客户端 文件接收完毕
							  os.write(3);
							  
							  if(bodyOff == bodyLen){
								  break;
							  }
						  }
					  }
					  
					  System.out.println("文件接收完毕");
				  }else{
					  //说明是上送文件内容
					  
				  }
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(null!=is){
					try{
						is.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(null!=os){
					try{
						os.close();
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
