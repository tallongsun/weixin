package com.weixin.msg.req;

import com.weixin.msg.BaseReqMessage;

public class LocationReqMessage extends BaseReqMessage{

	// 地理位置维度
	private String Location_X;
	// 地理位置经度
	private String Location_Y;
	// 地图缩放大小
	private String Scale;
	// 地理位置信息
	private String Label;
	
	public LocationReqMessage(String msgType, String toUserName,
			String fromUserName, long createTime, long msgId,
			String x,String y,String scale,String label) {
		super(msgType, toUserName, fromUserName, createTime, msgId);
		Location_X = x;
		Location_Y = y;
		Scale = scale;
		Label = label;
	}
	
	public String getLocation_X() {
		return Location_X;
	}
	public void setLocation_X(String locationX) {
		Location_X = locationX;
	}
	public String getLocation_Y() {
		return Location_Y;
	}
	public void setLocation_Y(String locationY) {
		Location_Y = locationY;
	}
	public String getScale() {
		return Scale;
	}
	public void setScale(String scale) {
		Scale = scale;
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
	
	
}
