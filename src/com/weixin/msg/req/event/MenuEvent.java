package com.weixin.msg.req.event;

import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.TextResMessage;

public class MenuEvent extends Event{
	private String key;
	
	public MenuEvent(String type,String key) {
		super(type);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public BaseResMessage exec(){
		if(type.equals("CLICK")){
			return new TextResMessage("您好，欢迎点击！");
		}
		return null;
	}
	
}
