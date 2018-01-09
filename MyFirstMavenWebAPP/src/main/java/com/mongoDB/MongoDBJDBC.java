package com.mongoDB;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDBJDBC {
	public static void main(String[] args) {
		try{   
	       // 连接到 mongodb 服务
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	       
	         // 连接到数据库
			MongoDatabase mongoDatabase = mongoClient.getDatabase("runoob");  
	        System.out.println("Connect to database successfully");
	        
	        
	        MongoCollection<Document> collection = mongoDatabase.getCollection("runoob");
	        FindIterable<Document> findIterable = collection.find(); 
	        MongoCursor<Document> mongoCursor = findIterable.iterator();  
	        while(mongoCursor.hasNext()){  
	            System.out.println(mongoCursor.next());  
	        }  
	        
	     }catch(Exception e){
	    	 System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	     }
	}
}
