package com.weixin.msg.req.event;

import java.util.Map;

public class EventFactory {
	public static Event getEvent(Map<String, String> requestMap){
		String type = requestMap.get("Event");
		if(type.equals("subscribe") || type.equals("unsubscribe")){
			return new Event(type);
		}else if(type.equals("CLICK")){
			String key = requestMap.get("EventKey");
			return new MenuEvent(type,key);
		}
		return null;
		
	}
}
