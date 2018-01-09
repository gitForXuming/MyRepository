package com.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZIPFileUtil {
	public static final String filePath = "E:\\tmp\\performance\\";
	
	
	public static void main(String[] args) {
		
		String a ="123";
		String b =a;
		b = b.replace("3", "4");
		System.out.println(a);
		System.out.println(b);
		String bankFlag="dzsh";
		File file;
		OutputStream out = null;
		GZIPOutputStream gzipos =null;	
		BufferedOutputStream bos= null;
		StringBuffer fileContent = new StringBuffer("123456789");
		String realPath =filePath+bankFlag+File.separator + "test.unl.00.gz";
		try{
			file = new File(filePath+bankFlag+File.separator);
			//检查目录是否存在 不存在先创建目录
			if(!file.exists()){
				file.mkdir();
			}
			file = new File(realPath);
			if(!file.exists()){
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			gzipos = new GZIPOutputStream(out);
			bos = new BufferedOutputStream(gzipos);
			
			bos.write(fileContent.toString().getBytes("GB18030"));
			bos.flush();
		}catch(Exception e){
			System.out.print("文件处理失败！");
		}finally{
			try {
				if(null!=bos){
					bos.close();
				}
				if(null!=gzipos){
					gzipos.close();
				}
				if(null!=out){
					out.flush();
				}
				
			} catch (IOException e) {
				System.out.print("关闭文件流失败！");
			}
			fileContent = null;
		}
	}
}
