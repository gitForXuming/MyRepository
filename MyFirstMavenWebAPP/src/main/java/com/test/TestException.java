package com.test;

public class TestException {
	public static void main(String[] args) throws ServiceException {
		try{
			if(true){
				throw new ServiceException();
			}
			
		}catch(ServiceException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println();
		}
	}
	
	public static class ServiceException extends Exception{
		public ServiceException(){
			super("123");
			try{
				String s =null;
				s.split("1");
				
			}catch(Exception e){
				throw e;
			}
			
		}
		
	}
}
