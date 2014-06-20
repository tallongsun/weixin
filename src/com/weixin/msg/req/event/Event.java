package com.weixin.msg.req.event;

import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.TextResMessage;

public class Event {
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
	
	public BaseResMessage exec(){
		if(type.equals("subscribe")){
			return new TextResMessage("您好，欢迎关注！");
		}
		return null;
	}
	
}
