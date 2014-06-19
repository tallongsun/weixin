package com.weixin;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.chat.ChatLogic;
import com.weixin.msg.BaseReqMessage;
import com.weixin.msg.BaseResMessage;
import com.weixin.msg.req.TextReqMessage;
import com.weixin.msg.res.TextResMessage;
import com.weixin.music.MusicLogic;
import com.weixin.util.MessageUtil;

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
                
                // 文本消息  
                if (reqMsg.getMsgType().equals("text")) { 
                	TextReqMessage textReqMsg = (TextReqMessage)reqMsg;
                	String content = textReqMsg.getContent().trim();
                	
                	if(content.startsWith("歌曲")){
                		resMsg = MusicLogic.process(content);
                	}else{
                		resMsg = ChatLogic.process(content,reqMsg.getFromUserName());
                	}
                }  
                if(resMsg != null){
                    resMsg.setToUserName(reqMsg.getFromUserName());  
                    resMsg.setFromUserName(reqMsg.getToUserName());  
                    resMsg.setCreateTime(new Date().getTime());  
                    
                }
                resStr = MessageUtil.messageToXml(resMsg);  
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
        long msgId = Long.parseLong(requestMap.get("MsgId"));
		if("text".equals(msgType)){
			String content = requestMap.get("Content");
			return new TextReqMessage(msgType,toUserName,fromUserName,createTime,msgId,content);
		}
		return null;
	}
}
