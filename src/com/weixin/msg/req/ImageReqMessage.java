package com.weixin.msg.req;

import com.weixin.msg.BaseReqMessage;

public class ImageReqMessage  extends BaseReqMessage{
	public ImageReqMessage(String msgType, String toUserName,
			String fromUserName, long createTime, long msgId,String picUrl) {
		super(msgType, toUserName, fromUserName, createTime, msgId);
		PicUrl = picUrl;
	}

	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	@Override
	public String toString() {
		return super.toString()+"ImageReqMessage [PicUrl=" + PicUrl + "]";
	}
	
	
}
