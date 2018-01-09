package com;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class JlyhAccountReplace {

	public static void main(String[] args) {

		FileOutputStream fos=null;
		BufferedReader br=null;
		BufferedOutputStream bos =null;
		try{
			File file = new File("C:\\Users\\lenovo\\Desktop\\sgb_xjzh_13031_20171216\\sgb_xjzh_806.unl");
			FileInputStream fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			
			File exchangeFile = new File("C:\\Users\\lenovo\\Desktop\\sgb_xjzh_13031_20171216\\output.sql");
			fos = new FileOutputStream(exchangeFile,true);
			bos = new BufferedOutputStream(fos);
			
			String line;
			line = br.readLine();
			StringBuffer sb;
			while(null!=line && line.length()>0){
				String[] arr = line.split("\\|");
				sb= new StringBuffer("insert into old_new_account values ('");
				sb.append(arr[0].trim()).append("','").append(arr[1].trim()).append("','");
				sb.append(arr[2].trim()).append("');\n");
				bos.write(sb.toString().getBytes("UTF-8"));
				bos.flush();
				line = br.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (br != null) {
					br.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (fos != null) {
					fos.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
