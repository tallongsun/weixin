package com.weixin.logic.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.weixin.util.HttpUtil;

public class ChickenChatService {
	public static final Logger log = LoggerFactory.getLogger(ChatLogic.class);
	
	public static String chat(String req,String openId){
        String url = String.format(
        		"http://www.simsimi.com/func/reqN?lc=ch&ft=0.0&req=%s",
        		HttpUtil.urlEncode(req,"UTF-8"));
        log.debug(url);
        
        String jsonStr = HttpUtil.doGet(url,"UTF-8");
        log.debug(jsonStr);
        
		JSONObject jso = JSONObject.parseObject(jsonStr);
		String response = jso.getString("sentence_resp");
		log.debug(response);
		
		return response;
	}
}
