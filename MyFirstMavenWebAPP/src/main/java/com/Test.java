package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Test {
	public volatile boolean flag =true;
	public static void main(String[] args) {
		//branch master edit for master 
		//version 8
		String hc = "ab";
		System.out.println(hc.hashCode());
		Set set = new HashSet();
		set.add("ab");
		set.add(3104);
		set.add(3105);
		set.add(3106);
		StringBuilder sb = new StringBuilder();
		StringBuffer sb1 = new StringBuffer();
		BufferedWriter bw=null;
		BufferedReader br=null;
		
		Test t = new Test();
		t.test();
		
		try{
			double ll = Double.valueOf("1000000000000000.00");
			BigDecimal d = BigDecimal.valueOf((Double.valueOf("10600.00")));
			System.out.println(d.toString());
			
			File file = new File("C:\\Users\\lenovo\\Desktop\\达州\\perbank.sql");
			FileInputStream fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis,"GBK"));
			
			File exchangeFile = new File("C:\\Users\\lenovo\\Desktop\\达州\\perbank_exchang.sql");
			FileOutputStream fos = new FileOutputStream(exchangeFile);
			bw = new BufferedWriter( new OutputStreamWriter(fos,"GBK"));
			int l =0;
			String line ="";
			String outLine="";
			line = br.readLine();
			while(""!=line && line.length()>0){
				l++;
				System.out.println(l);
				if(line.contains("pb_cstinf_net")&&line.contains("values")){
					String[] s = line.split(",");
					s[23] = s[23].split(" ")[0]+s[23].split(" ")[1].split(":")[0]+s[23].split(" ")[1].split(":")[1]+s[23].split(" ")[1].split(":")[2];
					s[24] = s[24].split(" ")[0]+s[24].split(" ")[1].split(":")[0]+s[24].split(" ")[1].split(":")[1]+s[24].split(" ")[1].split(":")[2];
					for(String ss:s){
						outLine = outLine+ss;
					}
				}else{
					outLine = line;
				}
				bw.write(outLine);
				bw.write("\n");
				bw.flush();
				outLine ="";
				line = br.readLine();
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				bw.close();
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
	
	
	public void test(){
		new Thread(){
			public void run() {
				int count =0;
				while(flag){
					count++;
					try{
						System.out.println(String.valueOf(count));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			};
		}.start();
		
		new Thread(){
			public void run() {
				try{
					flag = false;
				}catch(Exception e){
					e.printStackTrace();
				}
			};
		}.start();
		
	}

}
