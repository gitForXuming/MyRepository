package com.Ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheUtils {
	private static final String ehcacheXml = "";
	
	public static void main(String[] args) {
		CacheManager manager = CacheManager.newInstance("src/config/ehcache.xml");   
		
		Cache cache = new Cache("oneCache", 1, true, false, 5, 2); 
		manager.addCache(cache); 
		
		Element element = new Element("test", "1024");
		cache.put(element); 
		
		System.out.println(manager.getCache("oneCache").get("test").toString());
		
		//manager.shutdown();//关闭缓存
	}
	 
}
