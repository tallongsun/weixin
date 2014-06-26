package com.weixin.msg.req.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.TextResMessage;

public class Event {
	protected Logger log = LoggerFactory.getLogger(Event.class);
	
	protected String type;
	
	public Event(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public BaseResMessage exec(String openId){
		if(type.equals("subscribe")){
			return new TextResMessage("您好，欢迎关注！");
		}
		return null;
	}
	
}
