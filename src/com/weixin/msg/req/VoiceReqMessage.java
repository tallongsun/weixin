package com.weixin.msg.req;

import com.weixin.msg.BaseReqMessage;

public class VoiceReqMessage extends BaseReqMessage{

	// 媒体ID
	private String MediaId;
	// 语音格式
	private String Format;
	
	public VoiceReqMessage(String msgType, String toUserName,
			String fromUserName, long createTime, long msgId,String mediaId,String format) {
		super(msgType, toUserName, fromUserName, createTime, msgId);
		this.MediaId = mediaId;
		this.Format = format;
	}
	
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public String getFormat() {
		return Format;
	}
	public void setFormat(String format) {
		Format = format;
	}
	
	
}
