package com.weixin.msg.req.event;

import com.weixin.logic.chat.ChatLogic;
import com.weixin.logic.face.FaceLogic;
import com.weixin.logic.game.GameLogic;
import com.weixin.logic.location.LocationLogic;
import com.weixin.logic.music.MusicLogic;
import com.weixin.logic.weather.WeatherLogic;
import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.TextResMessage;

public class MenuEvent extends Event{
	private String key;
	
	public MenuEvent(String type,String key) {
		super(type);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public BaseResMessage exec(String openId){
		if(type.equals("CLICK")){
			if(key.equals("11")){
				return WeatherLogic.process(null);
			}else if(key.equals("12")){
				return new TextResMessage(LocationLogic.getUsage());
			}else if(key.equals("21")){
				return MusicLogic.process(MusicLogic.getUsage());
			}else if(key.equals("22")){
				return GameLogic.process("游戏");
			}else if(key.equals("23")){
				return new TextResMessage(FaceLogic.getUsage());
			}else if(key.equals("24")){
				return new TextResMessage(ChatLogic.getUsage());
			}else if(key.equals("31")){
				return ChatLogic.process("笑话", openId);
			}
			return new TextResMessage("您好，欢迎点击！");
		}
		return null;
	}
	
}
