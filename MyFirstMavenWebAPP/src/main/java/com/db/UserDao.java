package com.db;

import java.util.List;

import com.model.VO.BeanVO;


public interface UserDao{
	public List<BeanVO> queryUsers(BeanVO user);
	
	public int addUser(BeanVO user);
}
