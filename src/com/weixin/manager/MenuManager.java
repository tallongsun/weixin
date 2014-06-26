package com.weixin.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weixin.auth.AccessToken;
import com.weixin.auth.TokenThread;
import com.weixin.util.WeiXinUtil;

public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);  
	
	public static String getMenu() {  
		JSONArray btnAry = new JSONArray();
		
		///////////////////////////////////
		JSONArray subBtnAry = new JSONArray();
		JSONObject subBtnJso = new JSONObject();
		subBtnJso.put("name", "天气预报");
		subBtnJso.put("type", "click");
		subBtnJso.put("key", "11");
		subBtnAry.add(subBtnJso);
		
		subBtnJso = new JSONObject();
		subBtnJso.put("name", "周边搜索");
		subBtnJso.put("type", "click");
		subBtnJso.put("key", "12");
		subBtnAry.add(subBtnJso);
		
		JSONObject btnJso = new JSONObject();
		btnJso.put("name", "生活助手");
		btnJso.put("sub_button",subBtnAry);  
		btnAry.add(btnJso);
		
		///////////////////////////////////
		JSONArray subBtnAry2 = new JSONArray();
		JSONObject subBtnJso2 = new JSONObject();
		subBtnJso2.put("name", "歌曲点播");
		subBtnJso2.put("type", "click");
		subBtnJso2.put("key", "21");
		subBtnAry2.add(subBtnJso2);
		
		subBtnJso2 = new JSONObject();
		subBtnJso2.put("name", "经典游戏");
		subBtnJso2.put("type", "click");
		subBtnJso2.put("key", "22");
		subBtnAry2.add(subBtnJso2);
		
		subBtnJso2 = new JSONObject();
		subBtnJso2.put("name", "人脸识别");
		subBtnJso2.put("type", "click");
		subBtnJso2.put("key", "23");
		subBtnAry2.add(subBtnJso2);
		
		subBtnJso2 = new JSONObject();
		subBtnJso2.put("name", "聊天唠嗑");
		subBtnJso2.put("type", "click");
		subBtnJso2.put("key", "24");
		subBtnAry2.add(subBtnJso2);
		
		JSONObject btnJso2 = new JSONObject();
		btnJso2.put("name", "休闲驿站");
		btnJso2.put("sub_button",subBtnAry2);  
		btnAry.add(btnJso2);
  
		///////////////////////////////////
		JSONArray subBtnAry3 = new JSONArray();
		JSONObject subBtnJso3 = new JSONObject();
		subBtnJso3.put("name", "幽默笑话");
		subBtnJso3.put("type", "click");
		subBtnJso3.put("key", "31");
		subBtnAry3.add(subBtnJso3);
		
		subBtnJso3 = new JSONObject();
		subBtnJso3.put("name", "互动社区");
		subBtnJso3.put("type", "view");
		subBtnJso3.put("url", "http://wsq.qq.com/reflow/256311592");
		subBtnAry3.add(subBtnJso3);
		
		JSONObject btnJso3 = new JSONObject();
		btnJso3.put("name", "更多体验");
		btnJso3.put("sub_button",subBtnAry3);  
		btnAry.add(btnJso3);
		
		
		JSONObject menuJso = new JSONObject();
		menuJso.put("button", btnAry);
  
        return menuJso.toString();  
    }
	
	public static String getTestMenu(){
		JSONArray btnAry = new JSONArray();
		
		///////////////////////////////////
		JSONArray subBtnAry = new JSONArray();
		JSONObject subBtnJso = new JSONObject();
		subBtnJso.put("name", "天气预报");
		subBtnJso.put("type", "click");
		subBtnJso.put("key", "11");
		subBtnAry.add(subBtnJso);
		
		subBtnJso = new JSONObject();
		subBtnJso.put("name", "周边搜索");
		subBtnJso.put("type", "click");
		subBtnJso.put("key", "12");
		subBtnAry.add(subBtnJso);
		
		JSONObject btnJso = new JSONObject();
		btnJso.put("name", "生活助手");
		btnJso.put("sub_button",subBtnAry);  
		btnAry.add(btnJso);
		
		///////////////////////////////////
		JSONArray subBtnAry2 = new JSONArray();
		JSONObject subBtnJso2 = new JSONObject();
		subBtnJso2.put("name", "歌曲点播");
		subBtnJso2.put("type", "click");
		subBtnJso2.put("key", "21");
		subBtnAry2.add(subBtnJso2);
		
		subBtnJso2 = new JSONObject();
		subBtnJso2.put("name", "经典游戏");
		subBtnJso2.put("type", "click");
		subBtnJso2.put("key", "22");
		subBtnAry2.add(subBtnJso2);
		
		subBtnJso2 = new JSONObject();
		subBtnJso2.put("name", "人脸识别");
		subBtnJso2.put("type", "click");
		subBtnJso2.put("key", "23");
		subBtnAry2.add(subBtnJso2);
		
		subBtnJso2 = new JSONObject();
		subBtnJso2.put("name", "聊天唠嗑");
		subBtnJso2.put("type", "click");
		subBtnJso2.put("key", "24");
		subBtnAry2.add(subBtnJso2);
		
		JSONObject btnJso2 = new JSONObject();
		btnJso2.put("name", "休闲驿站");
		btnJso2.put("sub_button",subBtnAry2);  
		btnAry.add(btnJso2);
  
		///////////////////////////////////
		JSONArray subBtnAry3 = new JSONArray();
		JSONObject subBtnJso3 = new JSONObject();
		subBtnJso3.put("name", "幽默笑话");
		subBtnJso3.put("type", "click");
		subBtnJso3.put("key", "31");
		subBtnAry3.add(subBtnJso3);
		
		subBtnJso3 = new JSONObject();
		subBtnJso3.put("name", "互动社区");
		subBtnJso3.put("type", "view");
		subBtnJso3.put("url", "http://wsq.qq.com/reflow/256311592");
		subBtnAry3.add(subBtnJso3);
		
		subBtnJso3 = new JSONObject();
		subBtnJso3.put("name", "snsuser");
		subBtnJso3.put("type", "view");
		subBtnJso3.put("url", "http://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf62c8db6d6fc4cd0&redirect_uri=http%3A%2F%2Fwww.itallong.com%2Fyouyou%2Foauth.jsp&response_type=code&scope=snsapi_userinfo&state=wx#wechat_redirect");
		subBtnAry3.add(subBtnJso3);
		
		subBtnJso3 = new JSONObject();
		subBtnJso3.put("name", "snsbase");
		subBtnJso3.put("type", "view");
		subBtnJso3.put("url", "http://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf62c8db6d6fc4cd0&redirect_uri=http%3A%2F%2Fwww.itallong.com%2Fyouyou%2Foauth.jsp&response_type=code&scope=snsapi_base&state=wx#wechat_redirect");
		subBtnAry3.add(subBtnJso3);
		
		subBtnJso3 = new JSONObject();
		subBtnJso3.put("name", "jstest");
		subBtnJso3.put("type", "view");
		subBtnJso3.put("url", "http://www.itallong.com/youyou/index.jsp");
		subBtnAry3.add(subBtnJso3);
		
		JSONObject btnJso3 = new JSONObject();
		btnJso3.put("name", "更多体验");
		btnJso3.put("sub_button",subBtnAry3);  
		btnAry.add(btnJso3);
		
		
		JSONObject menuJso = new JSONObject();
		menuJso.put("button", btnAry);
  
        return menuJso.toString();  
	}
	
	public static void createMenu(){
		if(TokenThread.accessToken!=null){
			int result = WeiXinUtil.createMenu(getMenu(), TokenThread.accessToken.getToken());
			if(result == 0){
				log.info("菜单创建成功!");
			}else{
				log.info("菜单创建失败，错误码："+result);
			}
		}
	}
	
	public static void main(String[] args) {
		AccessToken token = WeiXinUtil.getAccessToken("wx37f976dad1ea9039", "38cf48c15f84ef6352e42a4669bdbdd0");
		if(token!=null){
			int result =WeiXinUtil.createMenu(getMenu(),token.getToken());
			System.out.println(result);
		}
	}
	
}
