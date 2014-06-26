package com.weixin.msg.req.event;

import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.TextResMessage;

public class LocationEvent  extends Event{
	private String Latitude;
	private String Longitude;
	private String Precision;
	
	public LocationEvent(String type,String latitude,String longitude,String precision){
		super(type);
		this.Latitude = latitude;
		this.Longitude = longitude;
		this.Precision = precision;
	}
	
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getPrecision() {
		return Precision;
	}
	public void setPrecision(String precision) {
		Precision = precision;
	}
	
	@Override
	public BaseResMessage exec(String openId){
		if(type.equals("LOCATION")){
			log.debug("{}@{},{},{}",openId,Latitude,Longitude,Precision);
			return new TextResMessage("您好，位置信息已更新！");
		}
		return null;
	}
	
	
}
