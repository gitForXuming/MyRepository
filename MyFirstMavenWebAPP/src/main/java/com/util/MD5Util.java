package com.util;

import java.security.MessageDigest;

public class MD5Util {
	
	public final static String MD5(String s,String encodeing) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        
        try {
        	System.out.println(Integer.toBinaryString(-21>>>4 & 0xf));
        	System.out.println(Integer.toBinaryString(21>>>4 & 0xf));
        	System.out.println(Integer.toBinaryString(2147483647));
        	System.out.println(Integer.valueOf(0xff));
        	char bb = hexDigits[0];
        	System.out.println(Integer.toHexString(5));
            byte[] btInput = s.getBytes(encodeing);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文  为
            byte[] md = mdInst.digest(); 
            // 把密文转换成十六进制的字符串形式 为啥要转换成16进制 因为为了大家好看
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; 
                str[k++] = hexDigits[byte0 & 0xf];
                //& 0xf  相当于 + 15
                //11010101 可以理解成是 11010000 代表的整数数加上 00000101代表的整数相加
                //那么 byte0 就可以理解成高4位+低4位相加
                //那么为啥是 >>>  二不是 >> 。正数还好理解 正数的 >>> 和 >>是一样的
                //负数要忽略符号位 所以用 >>>
                //此种写法是 Integer.toHexString 的写法
               
            }	
            //以下方式好理解
            int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < md.length; offset++) {
				i = md[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			//32位加密
			System.out.println(buf.toString());

            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
    public static void main(String[] args) {
    	System.out.println("imy测试报文124455");
        System.out.println(MD5Util.MD5("","UTF-8"));
        String md5 = "D41D8CD98F00B204E9800998ECF8427E";
        char[] c = new char[md5.length()];
        byte[] b = new byte[md5.length()>>1];
        c = md5.toCharArray();
        //反向转字节
        int i=0;
        int j=0;
        do{
        	
        	int hight = Integer.parseInt(String.valueOf(c[i]),16) << 4;
        	int low = Integer.parseInt(String.valueOf(c[i+1]),16);
        	b[j] = (byte)(hight | low);
        	//b[j] = (byte)Integer.parseInt((c[i]+""+c[i+1]).toString(),16);
        	i+=2;
        	j++;
        	
        }while(i<c.length);
        System.out.println("b");
    }
   
}
