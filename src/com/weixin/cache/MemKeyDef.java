package com.weixin.cache;

import static com.weixin.cache.MemKey.*;

/**
 * 键名统一定义，不能重复
 * 
 */
public interface MemKeyDef {
	public static final MemKey WEATHER_KEY = new MemKey("weather",NULL,null);
	
}
