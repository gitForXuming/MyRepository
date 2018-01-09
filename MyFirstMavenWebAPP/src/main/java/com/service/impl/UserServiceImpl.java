package com.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.db.UserDao;
import com.model.VO.BeanVO;
import com.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService{
	
	
	@Resource
	private EhCacheCacheManager cacheManager;
	
	@Resource
	private UserDao userdao;
	
	
	/**
	 * @description 如果参数user.username相同的话，仅走一次#user.username的意思是使用参数user.username的值作为Element元素的key 
	 * @param id
	 * @return
	 */
	/**
	 * @CachePut(value="myCache",key="#id")
	 * @description 该注解@CachePut与@Cacheable的不同之处在于前者每次都会触发方法体的执行，也就是说
	 * 我每次都会重新执行方法体并将返回的结果放在缓存不管参数是不是与以前的相同。可以使用这种方式更新，
	 * 当然也可以先移除再执行以前的查询就可以了。
	 * 这中方法的实时性很强，我们可以用在比如权限中，当权限重新配置完成后调用一下重新缓存一次
	 * @param user
	 * @return
	 */
	
	@Cacheable(value = "userCache", key="#user.username")   
	public List queryUsers(BeanVO user)  throws Exception{
		
		return userdao.queryUsers(user);
	}
	
	/**
	 * @description 移除缓存userCache中的key为#user.username的元素。当删除数据库中某个记录的时候记得清理缓存
	 * @param user
	 */
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@CacheEvict(value="userCache",key="#user.username")
	public int addUser(BeanVO user) throws Exception{
		int i=0;
		
		userdao.addUser(user);
		
		userdao.addUser(user);
		
		HashMap hm = new HashMap();
		TreeMap tm = new TreeMap();
		LinkedHashMap lhm = new LinkedHashMap();
		return 0;
	}

	public int addUsers(List<BeanVO> users)  throws Exception{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int testProxy(int a, int b ,String c,long sleep){
		
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 100;
		
	}
}
