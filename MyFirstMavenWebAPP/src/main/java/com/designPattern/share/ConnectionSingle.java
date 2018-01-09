package com.designPattern.share;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;



public enum ConnectionSingle {
	INSTANCE;
	private Connection conn;
	//数据库URL名
	private static final String URl ="jdbc:oracle:thin:@168.7.56.122:1521:ebank";
	//数据库驱动名
	private static final String DRIVER_CLASS ="oracle.jdbc.driver.OracleDriver";
	//数据库用户名
	private static final String USERNAME = "ebankUAT";
	//数据库密码
	private static final String PASSWORD = "ebankUAT";
	private ConnectionSingle(){
		try{
			conn = DriverManager.getConnection(URl, USERNAME, PASSWORD);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Statement stmt=null;
		PreparedStatement pstmt =null;
		Connection conn =null;
		try{
			conn = ConnectionSingle.INSTANCE.conn;
			stmt=conn.createStatement();       
			String sql="insert into data_copmare (CERTNO ,NEWCSTNO,OLDCSTNO) values ('8902188148363117187','8902188148363117187','8902188148363117187')"; 
			pstmt=conn.prepareStatement(sql);      
			int rs =pstmt.executeUpdate();
			System.out.println(rs);
			stmt.close();
			pstmt.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
}
