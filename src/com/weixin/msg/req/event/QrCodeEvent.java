package com.weixin.msg.req.event;

import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.TextResMessage;

public class QrCodeEvent extends MenuEvent{
	private String ticket;
	
	public QrCodeEvent(String type,String key,String ticket) {
		super(type,key);
		this.ticket = ticket;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	@Override
	public BaseResMessage exec(String openId){
		if(type.equals("subscribe")){
			return new TextResMessage("您好，欢迎二维码订阅！");
		}else if(type.equals("SCAN")){
			return new TextResMessage("您好，欢迎二维码扫描！");
		}
		return null;
	}
	
}
