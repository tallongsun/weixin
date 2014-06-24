package com.weixin.logic.chat;

import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.TextResMessage;

public class ChatLogic {
	
	
	public static BaseResMessage process(String content,String openId){
        if ("".equals(content)) {  
            return new TextResMessage("Input something...");
        } 

        String resp = ChatService.chat(content,openId);
        
        return new TextResMessage(resp);  
        
	}
	
	public static String getUsage() {  
        StringBuffer buffer = new StringBuffer();  
        buffer.append("聊天唠嗑使用指南").append("\n\n");  
        buffer.append("闲暇无聊，可以找我聊天啊，有问必答!例如：").append("\n");  
        buffer.append("讲个笑话").append("\n");
        buffer.append("继续");
        return buffer.toString();  
    } 

	public static void main(String[] args) {
		process("滚蛋","");
	}
}
