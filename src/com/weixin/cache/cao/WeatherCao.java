package com.weixin.cache.cao;

import com.weixin.cache.CaoHelper;
import com.weixin.cache.MemKeyDef;
import com.weixin.util.TimeUtil;



public class WeatherCao {
	
	public static void setWeather(String weather) {
		MemKeyDef.WEATHER_KEY.setExpiredTime(TimeUtil.getSecondsToNextDay());
		CaoHelper.getRedisClient().set(MemKeyDef.WEATHER_KEY, weather);
	}

	public static String getWeather(){
		return CaoHelper.getRedisClient().get(MemKeyDef.WEATHER_KEY);
	}
	
}
