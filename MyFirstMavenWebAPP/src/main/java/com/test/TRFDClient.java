/**
 * ==================================================================
 * The Huateng Software License
 *
 * Copyright (c) 2007-2008 Huateng Software System.  All rights
 * reserved.
 * @author MAIK.CHEN
 * create time :2008-02-17 16:45:38
 * ==================================================================
 */
package com.test;

/**
 * @author chenjz
 *
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

/**
 * @author chenjz trfd 文件传输客户端程序; TODO To change the template for this generated
 *         type comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class TRFDClient {

	protected static Logger logger = Logger.getLogger(TRFDClient.class);
	private DataInputStream in;
	private DataOutputStream out;

	private String sIP;
	private String sPort;
	private int cDATA = 2;
	private int cACK = 3;
	private int BUFFSIZE = 512;

	public TRFDClient(String sIP, String sPort) {
		this.sIP = sIP;
		this.sPort = sPort;
	}

	/**
	 * 使用Socket向服务端发送文件
	 *
	 * @param sFile:
	 *            文件名称(路径为默认路径)
	 * @return: 0-成功; 1-打开源文件出错; 2-连接不能建立; 3-不能创建目标文件; 4-传送出现异常;
	 */
	public int SendFile(String sFile) {
		try {
			logger.info("sendfile connect ip:" + sIP + ",port:" + sPort);

			ExSendFile ef = new ExSendFile('R');
			if (ef.OpenFile(sFile) != 0) {
				logger.error("open file " + sFile + "ERROR.");
				return 1;
			}

			// 设置超时为60秒
			Socket sock = new Socket(sIP, Integer.parseInt(sPort));
			sock.setSoTimeout(600000);//改为120秒

			out = new DataOutputStream(sock.getOutputStream());
			in = new DataInputStream(sock.getInputStream());

			// 发送filesd命令及文件名称,文件名称长度不能超过256;
			if (sFile.length() > 256)
				return 1;

			// out.writeByte(cDATA);
			String sCmd = "filesd" + sFile;
			// out.writeInt(sCmd.length());
			// out.write(sCmd.getBytes());
			// add by 20080531
			byte pakageflag = (byte) 2;
			byte[] pakage = (sCmd).getBytes();
			// 写到临时缓冲区
			java.io.ByteArrayOutputStream tmpbuff = new java.io.ByteArrayOutputStream();
			java.io.DataOutputStream tmpout = new java.io.DataOutputStream(
					tmpbuff);
			tmpout.writeByte(pakageflag);
			tmpout.writeInt(pakage.length);
			tmpout.write(pakage);
			tmpout.flush();

			out.write(tmpbuff.toByteArray());
			out.flush();
			tmpout.close();
			logger.info("FILECLIENT send cmd:" + sCmd);

			// 接收创建文件确认
			byte[] b = new byte[2];
			in.read(b);
			String sConfirm = new String(b);
			logger.info("FILECLIENT  get sConfirm:" + sConfirm);
			if (!sConfirm.equals("ok")) {
				logger.info("FILECLIENT  receive confirm:" + sConfirm);
				in.close();
				out.close();
				sock.close();

				return 3;
			}

			// 开始传送文件
			logger.info("FILECLIENT begin send file.");
			int lLen = ef.getFileLength();
			while (lLen > 0) {
				int k = (lLen > BUFFSIZE) ? BUFFSIZE : lLen;

				// 读取文件
				byte[] buf = new byte[k];
				ef.ReadBytes(buf);
				while (true) {
					out.writeByte(cDATA);
					out.writeInt(lLen);
					out.write(buf);
					out.flush();

					// in.read();
					// byte bb = in.readByte();
					// if (cACK != (int)bb)
					if (cACK != in.read()) {
						logger
								.info("FILECLIENT sendfile on road get confirm error!");
						continue;
					}

					break;
				}

				lLen = lLen - k;
			}

			logger.info("FILECLIENT Send file ok.");
			ef.CloseFile();
			in.close();
			out.close();
			sock.close();
		} catch (ConnectException e) {
			e.printStackTrace();
			return 2;
		} catch (SocketTimeoutException e) {
			return 4;
		} catch (SocketException e) {
			e.printStackTrace();
			return 4;
		} catch (IOException e) {
			e.printStackTrace();
			return 4;
		}

		return 0;
	}

	/**
	 * @title 用于送文件到ACE
	 * @param dateNumber
	 *            交易日期为文件路径
	 * @param sFile
	 *            文件名
	 * @return
	 */
	public int SendFile(String dateNumber, String sFile) {
		try {
			logger.info("sendfile connect ip:" + sIP + ",port:" + sPort);
			String relativeFilePath = dateNumber + File.separator + sFile;
			ExSendFile ef = new ExSendFile('R');
			if (ef.OpenFile(relativeFilePath) != 0) {
				logger.error("open file " + sFile + "ERROR.");
				return 1;
			}

			// 设置超时为60秒  300000=60秒
			Socket sock = new Socket(sIP, Integer.parseInt(sPort));
			sock.setSoTimeout(600000);//改为120秒

			out = new DataOutputStream(sock.getOutputStream());
			in = new DataInputStream(sock.getInputStream());

			// 发送filesd命令及文件名称,文件名称长度不能超过256;
			if (sFile.length() > 256)
				return 1;

			// out.writeByte(cDATA);
			String sCmd = "filesd" + sFile;
			// out.writeInt(sCmd.length());
			// out.write(sCmd.getBytes());
			// add by 20080531
			byte pakageflag = (byte) 2;
			byte[] pakage = (sCmd).getBytes();
			// 写到临时缓冲区
			java.io.ByteArrayOutputStream tmpbuff = new java.io.ByteArrayOutputStream();
			java.io.DataOutputStream tmpout = new java.io.DataOutputStream(
					tmpbuff);
			tmpout.writeByte(pakageflag);
			tmpout.writeInt(pakage.length);
			tmpout.write(pakage);
			tmpout.flush();

			out.write(tmpbuff.toByteArray());
			out.flush();
			tmpout.close();
			logger.info("FILECLIENT send cmd:" + sCmd);

			// 接收创建文件确认
			byte[] b = new byte[2];
			in.read(b);
			String sConfirm = new String(b);
			logger.info("get sConfirm:" + sConfirm);
			if (!sConfirm.equals("ok")) {
				logger.info("receive confirm:" + sConfirm);
				in.close();
				out.close();
				sock.close();

				return 3;
			}

			// 开始传送文件
			logger.info("begin send file.");
			int lLen = ef.getFileLength();
			while (lLen > 0) {
				int k = (lLen > BUFFSIZE) ? BUFFSIZE : lLen;

				// 读取文件
				byte[] buf = new byte[k];
				ef.ReadBytes(buf);
				while (true) {
					out.writeByte(cDATA);
					out.writeInt(lLen);
					out.write(buf);
					out.flush();

					// in.read();
					// byte bb = in.readByte();
					// if (cACK != (int)bb)
					if (cACK != in.read()) {
						logger.error("sendfile on road get confirm error!");
						continue;
					}

					break;
				}

				lLen = lLen - k;
			}

			logger.info("Send file ok.");
			ef.CloseFile();
			in.close();
			out.close();
			sock.close();
		} catch (ConnectException e) {
			e.printStackTrace();
			return 2;
		} catch (SocketTimeoutException e) {
			return 4;
		} catch (SocketException e) {
			e.printStackTrace();
			return 4;
		} catch (IOException e) {
			e.printStackTrace();
			return 4;
		}

		return 0;
	}

	/**
	 * @param dateNumber：交易日期
	 * @param sFile：文件名
	 * @param filePath：文件路径
	 * @return
	 */
	public int SendFile(String dateNumber, String sFile,String filePath) {
		try {
			logger.info("sendfile connect ip:" + sIP + ",port:" + sPort);
			String relativeFilePath = dateNumber + File.separator + sFile;
			ExSendFile ef = new ExSendFile('R');
			if (ef.OpenFile(relativeFilePath,filePath) != 0) {
				logger.error("open file " + sFile + "ERROR.");
				return 1;
			}
			
			// 设置超时为60秒  300000=60秒
			Socket sock = new Socket(sIP, Integer.parseInt(sPort));
			sock.setSoTimeout(600000);//改为120秒
			
			out = new DataOutputStream(sock.getOutputStream());
			in = new DataInputStream(sock.getInputStream());
			
			// 发送filesd命令及文件名称,文件名称长度不能超过256;
			if (sFile.length() > 256)
				return 1;
			
			// out.writeByte(cDATA);
			String sCmd = "filesd" + sFile;
			// out.writeInt(sCmd.length());
			// out.write(sCmd.getBytes());
			// add by 20080531
			byte pakageflag = (byte) 2;
			byte[] pakage = (sCmd).getBytes();
			// 写到临时缓冲区
			java.io.ByteArrayOutputStream tmpbuff = new java.io.ByteArrayOutputStream();
			java.io.DataOutputStream tmpout = new java.io.DataOutputStream(
					tmpbuff);
			tmpout.writeByte(pakageflag);
			tmpout.writeInt(pakage.length);
			tmpout.write(pakage);
			tmpout.flush();
			
			out.write(tmpbuff.toByteArray());
			out.flush();
			tmpout.close();
			logger.info("FILECLIENT send cmd:" + sCmd);
			
			// 接收创建文件确认
			byte[] b = new byte[2];
			in.read(b);
			String sConfirm = new String(b);
			logger.info("get sConfirm:" + sConfirm);
			if (!sConfirm.equals("ok")) {
				logger.info("receive confirm:" + sConfirm);
				in.close();
				out.close();
				sock.close();
				
				return 3;
			}
			
			// 开始传送文件
			logger.info("begin send file.");
			int lLen = ef.getFileLength();
			while (lLen > 0) {
				int k = (lLen > BUFFSIZE) ? BUFFSIZE : lLen;
				
				// 读取文件
				byte[] buf = new byte[k];
				ef.ReadBytes(buf);
				while (true) {
					out.writeByte(cDATA);
					out.writeInt(lLen);
					out.write(buf);
					out.flush();
					
					// in.read();
					// byte bb = in.readByte();
					// if (cACK != (int)bb)
					if (cACK != in.read()) {
						logger.error("sendfile on road get confirm error!");
						continue;
					}
					
					break;
				}
				
				lLen = lLen - k;
			}
			
			logger.info("Send file ok.");
			ef.CloseFile();
			in.close();
			out.close();
			sock.close();
		} catch (ConnectException e) {
			e.printStackTrace();
			return 2;
		} catch (SocketTimeoutException e) {
			return 4;
		} catch (SocketException e) {
			e.printStackTrace();
			return 4;
		} catch (IOException e) {
			e.printStackTrace();
			return 4;
		}
		
		return 0;
	}

	/**
	 * 使用Socket向服务端请求文件
	 *
	 * @param sFile:
	 *            文件名称(路径为默认路径)
	 * @return: 0-成功; 1-打开源文件出错; 2-连接不能建立; 3-不能创建目标文件; 4-传送出现异常;
	 */
	public int GetFile(String datepath, String sFile) {
		try {
			logger.info("FILECLIENT getfile connect ip:" + sIP + ",port:"
					+ sPort);
			String relativeFilePath = datepath + File.separator + sFile;
			System.out.println(relativeFilePath);
			ExRevFile ef = new ExRevFile('W');
			if (ef.OpenFile(relativeFilePath) != 0) {
				logger.info("FILECLIENT open file " + sFile + "ERROR.");
				return 3;
			}

			// 设置超时为60秒
			Socket sock = new Socket(sIP, Integer.parseInt(sPort));
			sock.setSoTimeout(600000);//改为120秒

			out = new DataOutputStream(sock.getOutputStream());
			in = new DataInputStream(sock.getInputStream());

			// 发送filesd命令及文件名称,文件名称长度不能超过256;
			if (sFile.length() > 256)
				return 1;
			 // out.writeByte(cDATA);
			 String sCmd = "filerc" + sFile;
			 // out.writeInt(sCmd.length());
			 // out.write(sCmd.getBytes());
			 logger.info("FILECLIENT send cmd:" + sCmd);

			 // ===============================================
			 byte pakageflag = (byte)2;
			 byte[] pakage = (sCmd ).getBytes();
			 // 写到临时缓冲区
			 java.io.ByteArrayOutputStream tmpbuff = new
			 java.io.ByteArrayOutputStream();
			 java.io.DataOutputStream tmpout = new
			 java.io.DataOutputStream(tmpbuff);
			 tmpout.writeByte(pakageflag);
			 tmpout.writeInt(pakage.length);
			 tmpout.write(pakage);
			 tmpout.flush();

			 out.write(tmpbuff.toByteArray());
			 out.flush();
			 tmpout.close();
//
//			out.writeByte(cDATA);
//			String sCmd = "filerc" + sFile;
//			out.writeInt(sCmd.length());
//			out.write(sCmd.getBytes());

			// =================================================
			// 接收文件确认
			byte[] b = new byte[2];
			in.read(b);
			String sConfirm = new String(b);
			logger.info("FILECLIENT get sConfirm:" + sConfirm);
			if (!sConfirm.equals("ok")) {
				logger.info("FILECLIENT receive confirm:" + sConfirm);
				in.close();
				out.close();
				sock.close();

				return 1;
			}

			// 开始接收文件
			while (true) {
				// 开始接收数据
				byte bb = in.readByte();
				if (cDATA != (int) bb) {
					logger.info("FILECLIENT receivefile on road get :" + bb);
					out.writeByte(cDATA);
					continue;
				}

				// 从SOCKET中读取lLen, 并取lLen和BUFFSIZE的最小值
				int lLen = in.readInt();
				int k = (int) ((lLen < BUFFSIZE) ? lLen : BUFFSIZE);
				byte[] buff = new byte[k];
				if (in.read(buff) < k) {
					out.writeByte(cDATA);
					continue;
				}

				out.writeByte(cACK);
				ef.WriteBytes(buff);

				// lLen<BUFFSIZE时说明文件已经接收完成,退出循环;
				/* modify by wuzhiwei 20101231
				 * 原判断有bug,if (lLen <BUFFSIZE) ,如果lLen=512,则无法break,导致一直死循环,报EOF错误.
				 * 逻辑判断修改为 <=
				 * */
				if (lLen <= BUFFSIZE)
					break;
			}

			ef.CloseFile();
			in.close();
			out.close();
			sock.close();
		} catch (ConnectException e) {
			e.printStackTrace();
			return 2;
		} catch (SocketTimeoutException e) {
			return 4;
		} catch (SocketException e) {
			e.printStackTrace();
			return 4;
		} catch (IOException e) {
			e.printStackTrace();
			return 4;
		}

		return 0;
	}

	/**
	 * 使用Socket向服务端请求文件
	 *
	 * @param sFile:
	 *            文件名称(路径为默认路径)
	 * @return: 0-成功; 1-打开源文件出错; 2-连接不能建立; 3-不能创建目标文件; 4-传送出现异常;
	 */
	public int GetFile(String sFile) {
		try {
			logger.info("FILECLIENT getfile connect ip:" + sIP + ",port:"
					+ sPort);

			ExRevFile ef = new ExRevFile('W');
			if (ef.OpenFile(sFile) != 0) {
				logger.info("FILECLIENT open file " + sFile + "ERROR.");
				return 3;
			}

			// 设置超时为60秒
			Socket sock = new Socket(sIP, Integer.parseInt(sPort));
			sock.setSoTimeout(300000);

			out = new DataOutputStream(sock.getOutputStream());
			in = new DataInputStream(sock.getInputStream());

			// 发送filesd命令及文件名称,文件名称长度不能超过256;
			if (sFile.length() > 256)
				return 1;
			// out.writeByte(cDATA);
			String sCmd = "filerc" + sFile;
			// out.writeInt(sCmd.length());
			// out.write(sCmd.getBytes());
			logger.info("FILECLIENT send cmd:" + sCmd);
			// ===============================================
			byte pakageflag = (byte) 2;
			byte[] pakage = (sCmd).getBytes();
			// 写到临时缓冲区
			java.io.ByteArrayOutputStream tmpbuff = new java.io.ByteArrayOutputStream();
			java.io.DataOutputStream tmpout = new java.io.DataOutputStream(
					tmpbuff);
			tmpout.writeByte(pakageflag);
			tmpout.writeInt(pakage.length);
			tmpout.write(pakage);
			tmpout.flush();

			out.write(tmpbuff.toByteArray());
			out.flush();
			tmpout.close();

			// =================================================
			// 接收文件确认
			byte[] b = new byte[2];
			in.read(b);
			String sConfirm = new String(b);
			logger.info("FILECLIENT get sConfirm:" + sConfirm);
			if (!sConfirm.equals("ok")) {
				logger.info("FILECLIENT receive confirm:" + sConfirm);
				in.close();
				out.close();
				sock.close();

				return 1;
			}

			// 开始接收文件
			while (true) {
				// 开始接收数据
				byte bb = in.readByte();
				if (cDATA != (int) bb) {
					logger.info("FILECLIENT receivefile on road get :" + bb);
					out.writeByte(cDATA);
					continue;
				}

				// 从SOCKET中读取lLen, 并取lLen和BUFFSIZE的最小值
				int lLen = in.readInt();
				int k = (int) ((lLen < BUFFSIZE) ? lLen : BUFFSIZE);
				byte[] buff = new byte[k];
				if (in.read(buff) < k) {
					out.writeByte(cDATA);
					continue;
				}

				out.writeByte(cACK);
				ef.WriteBytes(buff);

				// lLen<BUFFSIZE时说明文件已经接收完成,退出循环;
				if (lLen < BUFFSIZE)
					break;
			}

			ef.CloseFile();
			in.close();
			out.close();
			sock.close();
		} catch (ConnectException e) {
			e.printStackTrace();
			return 2;
		} catch (SocketTimeoutException e) {
			return 4;
		} catch (SocketException e) {
			e.printStackTrace();
			return 4;
		} catch (IOException e) {
			e.printStackTrace();
			return 4;
		}

		return 0;
	}

	/*
	 * 文件发送工具类
	 */
	public class ExSendFile{
		public static final String filePathRoot="E:\\tmp\\performance\\";
		public char flag ='R';
		public File file =null;
		public InputStream is = null;
		public  ExSendFile(char flag){
			this.flag = flag;
		}
		
		public int OpenFile(String fileName){
			file = new File(filePathRoot+fileName);
			if(file.exists()){
				return 0;
			}
			return 1;
		}
		
		public int OpenFile(String relativeFilePath ,String filePath){
			file = new File(filePath+relativeFilePath);
			if(file.exists()){
				return 0;
			}
			return 1;
		}
		
		public int getFileLength(){
			
			return (int)file.length();
		}
		//读取文件
		public void ReadBytes(byte[] buf){
			try{
				
				is= new FileInputStream(file);
				int len = is.read(buf);
				
			}catch(Exception e){
				
			}
		}
		
		public void CloseFile(){
			try{
				if(null!=is){
					is.close();
				}
			}catch(IOException e){
				
			}
		}
	}
	
	
	
	public class ExRevFile{
		public File file=null;
		public OutputStream os = null;
		public ExRevFile(char flag){
			
		}
		
		public int OpenFile(String fileName){
			file = new File(fileName);
			if(file.exists()){
				return 0;
			}
			return 1;
		}
		
		public void WriteBytes(byte buff[]){
			
		}
		
		public void CloseFile(){
			try{
				if(null!=os){
					os.flush();
					os.close();
				}
			}catch(Exception e){
				
			}
			
		}
	}
	/**
	 * 测试用
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// CommClient client = new CommClient("192.168.1.100", "9090");
		// System.out.println(client.SendCMD("0004test"));
		
		int b =256;
		
		byte[] bint = new byte[4];
		bint[3]=(byte)b;
		bint[2]=(byte)(b>>8);
		bint[1]=(byte)(b>>16);
		bint[0]=(byte)(b>>24);
		
		int a = (int) ((bint[3] & 0xFF)     
					| ((bint[2] & 0xFF)<<8) 
					| ((bint[1] & 0xFF)<<16) 
				| ((bint[0] & 0xFF)<<24));

		TRFDClient trfdClient = new TRFDClient("127.0.0.1", "3600");
		// System.out.println("trfd getfile return:" +
		// trfd.SendFile("ff3.txt"));
		// System.out.println("trfd getfile return:" +
		// trfd.GetFile("BCP_JR.ec"));
		int result = trfdClient.SendFile("send1.txt");
		if (result != 0) {
			System.out.println("send error");
		}
	}
}
