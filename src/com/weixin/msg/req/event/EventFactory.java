package com.weixin.msg.req.event;

import java.util.Map;

public class EventFactory {
	public static Event getEvent(Map<String, String> requestMap){
		String type = requestMap.get("Event");
		if(type.equals("subscribe") || type.equals("unsubscribe")){
			return new Event(type);
		}else if(type.equals("CLICK") || type.equals("VIEW")){
			String key = requestMap.get("EventKey");
			return new MenuEvent(type,key);
		}else if(type.equals("SCAN")){//TODO:现在这种方式二维码的subscribe事件会被普通的吃掉
			String key = requestMap.get("EventKey");
			String ticket = requestMap.get("Ticket");
			return new QrCodeEvent(type, key, ticket);
		}else if(type.equals("LOCATION")){
			String latitude = requestMap.get("Latitude");
			String longitude = requestMap.get("Longitude");
			String precision = requestMap.get("Precision");
			return new LocationEvent(type, latitude, longitude, precision);
		}
		return null;
		
	}
}
