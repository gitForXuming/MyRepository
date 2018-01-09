package com;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class JlyhCertTypeNoReplace {
	public static void main(String[] args) {
		FileOutputStream fos=null;
		BufferedReader br=null;
		BufferedOutputStream bos =null;
		try{
			File file = new File("C:\\Users\\lenovo\\Desktop\\newOldCertAndHostCustNo\\dgzhzjdzb.txt");
			FileInputStream fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));
			
			File exchangeFile = new File("C:\\Users\\lenovo\\Desktop\\newOldCertAndHostCustNo\\1303x.sql");
			fos = new FileOutputStream(exchangeFile,true);
			bos = new BufferedOutputStream(fos);
			
			String line ="";
			line = br.readLine();
			StringBuffer sb = null;
			while(null!=line && line.length()>0){
				String[] arr = line.split("\\|");
				sb= new StringBuffer("insert into old_new_cert values ('");
				//0231
				sb.append(arr[0].trim()).append("','");
				sb.append(arr[1].trim()).append("','");
				sb.append(arr[2].trim()).append("','");
				sb.append(arr[3].trim()).append("');\n");
				bos.write(sb.toString().getBytes("UTF-8"));
				bos.flush();
				line = br.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				br.close();
				bos.close();
				fos.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}

