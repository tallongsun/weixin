package com.weixin.msg.req;

import com.weixin.msg.BaseReqMessage;

public class TextReqMessage extends BaseReqMessage{

	// 消息内容  
    private String Content;  
  
    public TextReqMessage(String msgType, String toUserName,
			String fromUserName, long createTime, long msgId, String content) {
		super(msgType, toUserName, fromUserName, createTime, msgId);
		Content = content;
	}


	public String getContent() {  
        return Content;  
    }  
  
    public void setContent(String content) {  
        Content = content;  
    }


	@Override
	public String toString() {
		return super.toString()+"TextReqMessage [Content=" + Content + "]";
	}


    
    
}
