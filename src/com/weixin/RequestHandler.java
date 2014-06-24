package com.weixin;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.logic.chat.ChatLogic;
import com.weixin.logic.face.FaceLogic;
import com.weixin.logic.game.GameLogic;
import com.weixin.logic.location.LocationLogic;
import com.weixin.logic.music.MusicLogic;
import com.weixin.logic.weather.WeatherLogic;
import com.weixin.msg.BaseReqMessage;
import com.weixin.msg.BaseResMessage;
import com.weixin.msg.req.EventReqMessage;
import com.weixin.msg.req.ImageReqMessage;
import com.weixin.msg.req.LocationReqMessage;
import com.weixin.msg.req.TextReqMessage;
import com.weixin.msg.req.VoiceReqMessage;
import com.weixin.msg.req.event.Event;
import com.weixin.msg.req.event.EventFactory;
import com.weixin.msg.res.TextResMessage;
import com.weixin.util.MessageUtil;
import com.weixin.util.NumberUtil;

public class RequestHandler {
	public static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	public static String processRequest(HttpServletRequest request) { 
        String resStr = null;  
        try {  
            BaseReqMessage reqMsg = recognizeMsg(request);
            log.debug("reqMsg:"+reqMsg.toString());
            if(reqMsg!=null){
                // 默认回复文本消息  
            	BaseResMessage resMsg = new TextResMessage("请求处理异常，请稍候尝试！");  
                
                if (reqMsg.getMsgType().equals("text")) { // 文本消息  
                	TextReqMessage textReqMsg = (TextReqMessage)reqMsg;
                	String content = textReqMsg.getContent().trim();
                	
                	if(content.startsWith("歌曲")){
                		resMsg = MusicLogic.process(content);
                	}else if(content.startsWith("游戏")){
                		resMsg = GameLogic.process(content);
                	}else if(content.startsWith("附近")){
                		resMsg = LocationLogic.process(content,reqMsg.getFromUserName());
                	}else if(NumberUtil.verifyNumber(content)){
                		resMsg = GameLogic.process(content,reqMsg.getFromUserName());
                	}else{
                		resMsg = ChatLogic.process(content,reqMsg.getFromUserName());
                	}
                } else if (reqMsg.getMsgType().equals("image")){ // 图片消息
                	ImageReqMessage imageReqMsg = (ImageReqMessage)reqMsg;
                	String picUrl = imageReqMsg.getPicUrl();
                	resMsg = FaceLogic.process(picUrl);
                } else if (reqMsg.getMsgType().equals("location")){ // 位置消息
                	LocationReqMessage locReqMsg = (LocationReqMessage)reqMsg;
                	resMsg = LocationLogic.process(locReqMsg);
                } else if (reqMsg.getMsgType().equals("voice")){ // 语音消息
                	VoiceReqMessage voiceReqMsg = (VoiceReqMessage)reqMsg;
                	resMsg = WeatherLogic.process(voiceReqMsg);
                } else if (reqMsg.getMsgType().equals("event")){ //事件
                	EventReqMessage eventReqMsg = (EventReqMessage)reqMsg;
                	Event event = eventReqMsg.getEvent();
                	if(event!=null){
                		resMsg = event.exec(eventReqMsg.getFromUserName());
                	}
                }
                if(resMsg != null){
                    resMsg.setToUserName(reqMsg.getFromUserName());  
                    resMsg.setFromUserName(reqMsg.getToUserName());  
                    resMsg.setCreateTime(new Date().getTime());  
                    resStr = MessageUtil.messageToXml(resMsg); 
                }
                log.debug("resStr:"+resStr);
            }
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return resStr;  
	}
	
	private static BaseReqMessage recognizeMsg(HttpServletRequest request){
		Map<String, String> requestMap = MessageUtil.parseXml(request);  
		String msgType = requestMap.get("MsgType");  
        String toUserName = requestMap.get("ToUserName");  
        String fromUserName = requestMap.get("FromUserName");  
        long createTime = Long.parseLong(requestMap.get("CreateTime"));
        long msgId = 0;//先不排重
		if("text".equals(msgType)){
			String content = requestMap.get("Content");
			return new TextReqMessage(msgType,toUserName,fromUserName,createTime,msgId,content);
		}else if("image".equals(msgType)){
			String picUrl = requestMap.get("PicUrl");
			return new ImageReqMessage(msgType, toUserName, fromUserName, createTime, msgId, picUrl);
		}else if("location".equals(msgType)){
			String x = requestMap.get("Location_X"); 
			String y = requestMap.get("Location_Y"); 
			String scale = requestMap.get("Scale"); 
			String label = requestMap.get("Label"); 
			return new LocationReqMessage(msgType, toUserName, fromUserName, createTime, msgId, x, y, scale, label);
		}else if("voice".equals(msgType)){
			String mediaId = requestMap.get("MediaId");
			String format = requestMap.get("Format");
			return new VoiceReqMessage(msgType, toUserName, fromUserName, createTime, msgId, mediaId, format);
		}else if("event".equals(msgType)){
			Event event = EventFactory.getEvent(requestMap);
			if(event!=null){
				return new EventReqMessage(msgType, toUserName, fromUserName, createTime, msgId, event);
			}
		}
		return null;
	}
	
}
