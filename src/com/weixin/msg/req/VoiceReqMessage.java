package com.weixin.msg.req;

import com.weixin.msg.BaseReqMessage;

public class VoiceReqMessage extends BaseReqMessage{

	// 媒体ID
	private String MediaId;
	// 语音格式
	private String Format;
	// 语音识别结果，UTF8编码
	private String Recognition;
	
	public VoiceReqMessage(String msgType, String toUserName,
			String fromUserName, long createTime, long msgId,String mediaId,String format,String recognition) {
		super(msgType, toUserName, fromUserName, createTime, msgId);
		this.MediaId = mediaId;
		this.Format = format;
		this.Recognition = recognition;
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

	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(String recognition) {
		Recognition = recognition;
	}

	@Override
	public String toString() {
		return super.toString()+"VoiceReqMessage [Format=" + Format + ", MediaId=" + MediaId
				+ ", Recognition=" + Recognition + "]";
	}
	
	
}
