package com.weixin.msg.res;

import com.weixin.msg.BaseResMessage;

public class TextResMessage extends BaseResMessage{
	private String Content;

	
	
	public TextResMessage(String content) {
		super();
		this.setMsgType("text");
		Content = content;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		this.Content = content;
	}
	
}
