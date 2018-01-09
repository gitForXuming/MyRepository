package com.service;

import java.util.List;

import com.model.VO.BeanVO;

public interface IUserService{
	public static  int a=3;
	/*
	 * 查询用户
	 */
	public List queryUsers(BeanVO user) throws Exception;
	/*
	 * 添加用户
	 */
	public int addUser(BeanVO user) throws Exception;
	
	/*
	 * 添加多个用户
	 */
	public int addUsers(List<BeanVO> users) throws Exception;
	
	public int testProxy(int a ,int b ,String c,long sleep);
}
