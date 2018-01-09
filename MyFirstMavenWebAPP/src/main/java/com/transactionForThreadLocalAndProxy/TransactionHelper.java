package com.transactionForThreadLocalAndProxy;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class TransactionHelper {
	public static final Logger logger = Logger.getLogger(TransactionHelper.class);
	
	private static ThreadLocal<Connection> connection_holder = new ThreadLocal<Connection>();
	
	private static ThreadLocal<Boolean> existsTransaction = new ThreadLocal<Boolean>(){
		@Override
		protected Boolean initialValue() {
			
			return Boolean.FALSE;
		}
	};
	
	private static ThreadLocal<Boolean> rollBack = new ThreadLocal<Boolean>(){
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};
	
	private static int count = 0;
	private final static Properties properties = new Properties();
	
	static {
		// 加载配置文件
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("config//env//connection.properties");
		try {

			properties.load(is);
			is.close();
			// 加载驱动程序
			Class.forName(properties.getProperty("driverClassName"));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("驱动未找到", e);
		}
	}
	
	 /** 
     * 当前是否存在事务 
     */  
	public static boolean existsTransaction(){
		return existsTransaction.get();
	}
	
	// 设置当前事务环境  
    private static void setExistsTransaction(boolean flag) {  
        existsTransaction.set(flag);  
    }  
    
    //设置当前事物是否需要回滚
    public static void setRollBack(boolean flag){
    	rollBack.set(flag);
    }
    
    public static boolean needRollBack(){
    	return rollBack.get();
    }
	//开始一个事物
	public static void beginTransaction(){
		Connection conn = getNotAutoCommitConnection();
		setExistsTransaction(Boolean.TRUE); 
		connection_holder.set(conn);
	}
	
	//获取当前数据库的连接
	private static Connection getCurrConnection(){
		Connection conn = connection_holder.get();
		if(null==conn){
			conn = getNotAutoCommitConnection();
		}
		return conn;
	}
	
	//执行sql
	public static int executeSql(String sql){
		try{
			Connection conn = getCurrConnection();
			count += conn.createStatement().executeUpdate(sql);
			logger.info("sql执行成功，等待事物提交");
		}catch(SQLException e){
			logger.info("sql执行失败");
			logger.error(e.getMessage(), e);
		}
		
		return count;
	}
	//事物提交
	public static void commit(){
		Connection conn = getCurrConnection();
		try{
			if(null!=conn&&!conn.isClosed()){
				conn.commit();
				conn.close();
				
				logger.info("事物已提交，已改变：" + count +"条记录。");
				existsTransaction.set(Boolean.FALSE);
			}
		}catch(SQLException e){
			logger.info("事物提交失败");
			logger.error(e.getMessage(), e);
		}
	}
	
	//事物回滚
	public static void rollBack(){
		Connection conn = getCurrConnection();
		try{
			if(null!=conn&&!conn.isClosed()){
				conn.rollback();;
				conn.close();
				logger.info("事物已回滚，回滚改变：" + count +"条记录。");
				existsTransaction.set(Boolean.FALSE);
			}
		}catch(SQLException e){
			logger.info("事物提交失败");
			logger.error(e.getMessage(), e);
		}
	}
	//创建一个不自动提交的连接
	private static Connection getNotAutoCommitConnection(){
		Connection conn =null;
		try{
			conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
			conn.setAutoCommit(false);
		}catch(SQLException e){
			logger.info("创建数据库连接失败");
			logger.error(e.getMessage(), e);
		}
		
		return conn;
	}
}
