package com.weixin.cache;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class CaoHelper {
	private static RedisClient redisClient;
	
	private static String host = "127.0.0.1";
	private static int port = 6379;
	private static int timeout = 10000;
	
	/** 最大连接数 */
	private static int maxActive = 30;
	/** 最大空闲连接数 */
	private static int maxIdle = 0;
	/** 从连接池取连接前是否校验 */
	private static boolean testOnBorrow = false;
	
	@SuppressWarnings("unchecked")
	public static void init(String rootPath){
		SAXReader saxReader = new SAXReader();
		Document document=null;
		String path = rootPath + "/config/cache.xml";
		
		try {
			document = saxReader.read(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		List cache = document.selectNodes("/cache");
		Iterator iter = cache.iterator();
		while(iter.hasNext()){
			Element ele=(Element)iter.next();
			for(Iterator i=ele.attributeIterator();i.hasNext();){
				Attribute attr = (Attribute) i.next();
				String aName = attr.getName();
				String aValue = attr.getValue();
				if (aName.equals("host") && aValue!=null && aValue.trim().length()>0) 
					host=aValue;
				if (aName.equals("port") && aValue!=null && aValue.trim().length()>0) 
					port=Integer.parseInt(aValue);
				if (aName.equals("timeout") && aValue!=null && aValue.trim().length()>0) 
					timeout=Integer.parseInt(aValue);
				if (aName.equals("active") && aValue!=null && aValue.trim().length()>0) 
					maxActive=Integer.parseInt(aValue);
				if (aName.equals("idle") && aValue!=null && aValue.trim().length()>0) 
					maxIdle=Integer.parseInt(aValue);
			}
		}
		
		JedisPoolConfig jc = new JedisPoolConfig();
		jc.setMaxActive(maxActive);
		jc.setMaxIdle(maxIdle);
		jc.setMaxWait(timeout);
		jc.setTestOnBorrow(testOnBorrow);
		redisClient = new RedisClient(new JedisPool(jc, host, port, timeout));
	}
	

	public static RedisClient getRedisClient() {
		return redisClient;
	}

	
}
