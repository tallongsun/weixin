package com.weixin.msg.req;

import com.weixin.msg.BaseReqMessage;
import com.weixin.msg.req.event.Event;

public class EventReqMessage extends BaseReqMessage{

	private Event event;
	
	public EventReqMessage(String msgType, String toUserName,
			String fromUserName, long createTime, long msgId,Event event) {
		super(msgType, toUserName, fromUserName, createTime, msgId);
		this.event = event;
	}


	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	
}
