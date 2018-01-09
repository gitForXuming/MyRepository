package com.socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PkgUtil {

	/**
	 * 方法名：writeByte <br/>
	 * 描述：往输出流中输出一个字节 <br/>
	 * @param output 输出流
	 * @param value 指定的字节
	 * @throws IOException <br/>
	 */
	public static void writeByte(OutputStream output,  byte value) 
			throws IOException
	{
		output.write(value);
	}
	
	/**
	 * 方法名：writeInt <br/>
	 * 描述：往输出流中输出一个整数  <br/>
	 * @param output 输出流
	 * @param value 指定的整数
	 * @throws IOException <br/>
	 */
	public static void writeInt(OutputStream output,  long value) 
			throws IOException
	{
		byte[] buff= new byte[4];
		buff[0] = (byte)(value >> 24);
		buff[1] = (byte)(value >> 16);
		buff[2] = (byte)(value >> 8);
		buff[3] = (byte)(value);
		 
		output.write(buff);
		
	}
	
	/**
	 * 方法名：writeTotalBytes <br/>
	 * 描述：往输出流中输出指定缓冲区的所有内容 <br/>
	 * @param output 输出流
	 * @param buff 指定缓冲区
	 * @throws IOException <br/>
	 */
	public static void writeTotalBytes(OutputStream output, byte[] buff) 
			throws IOException
	{
		output.write(buff);
	}	
	
	/**
	 * 方法名：readTotalBytes <br/>
	 * 描述：从流中读取内容直到填满缓冲区指定的空间 <br/>
	 * @param input 输入流对象
	 * @param buff 接收缓冲区
	 * @param pos 接收缓冲区指定的起始位置
	 * @param len 接收缓冲区指定的大小
	 * @throws IOException <br/>
	 */
	public static void readTotalBytes(InputStream input, byte[] buff, int pos, int len) 
			throws IOException
	{
		if(pos+ len > buff.length){
			throw new IOException("传入的缓冲空间不足");
		}
		while(len > 0){
			int rs =input.read(buff, pos, len);
			if(rs < 0){
				throw new EOFException("读取失败");
			}
			pos += rs;
			len = len - rs;
		}
	}
	/**
	 * 方法名：readTotalBytes <br/>
	 * 描述：从输入流中读取内容直到填满缓冲区全部的空间 <br/>
	 * @param input 输入流对象
	 * @param buff 接收缓冲区
	 * @throws IOException <br/>
	 */
	public static byte[] readTotalBytes(InputStream input ,  byte[] buff )
	throws IOException{
		
		readTotalBytes(input, buff, 0 , buff.length);
		return buff;
	}

	/**
	 * 方法名：readTotalBytes <br/>
	 * 描述：从输入流中读取指定的字节数 <br/>
	 * @param input 输入流对象
	 * @param bytesToRead 读取字节的数目
	 * @return 字节数组
	 * @throws IOException <br/>
	 */
	public static byte[] readTotalBytes(InputStream input , int bytesToRead )
	throws IOException{
		
		byte[]  buff = new byte[bytesToRead];
		readTotalBytes(input, buff);
		return buff;
	}
	/**
	 * 方法名：readByte <br/>
	 * 描述：从输入流中读取一个字节 <br/>
	 * @param input 输入流
	 * @return byte
	 * @throws IOException <br/>
	 */
	public static byte readByte(InputStream input)
			throws IOException
	{
		int result = input.read();
		if(result < 0) {
			throw new EOFException("读取失败");
		}
		return (byte)result;
	}
	
	/**
	 * 方法名：readInt <br/>
	 * 描述：从输入流中读取一个整数  <br/>
	 * @param input 输入流
	 * @return int
	 * @throws IOException <br/>
	 */
	public static int readInt(InputStream input)
			throws IOException{
		byte[] buff = readTotalBytes(input, 4);
		return  (int)((buff[0]<<24)|(buff[1]<<16)|(buff[2]<<8)|(buff[3] & 0xff ));
	}
}
