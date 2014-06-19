package com.weixin.msg;

public abstract class BaseReqMessage {
    // 消息类型（text/image/location/link）  
    private String MsgType;  
	
    // 开发者微信号  
    private String ToUserName;  
    // 发送方帐号（一个OpenID）  
    private String FromUserName;  
    // 消息创建时间 （整型）  
    private long CreateTime;  
    // 消息id，64位整型  
    private long MsgId;
    
    
	public BaseReqMessage(String msgType, String toUserName,
			String fromUserName, long createTime, long msgId) {
		MsgType = msgType;
		ToUserName = toUserName;
		FromUserName = fromUserName;
		CreateTime = createTime;
		MsgId = msgId;
	}
	
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	public long getMsgId() {
		return MsgId;
	}
	public void setMsgId(long msgId) {
		MsgId = msgId;
	}

	@Override
	public String toString() {
		return "BaseReqMessage [CreateTime=" + CreateTime + ", FromUserName="
				+ FromUserName + ", MsgId=" + MsgId + ", MsgType=" + MsgType
				+ ", ToUserName=" + ToUserName + "]";
	}
    
    
    
    
}
