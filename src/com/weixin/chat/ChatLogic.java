package com.weixin.chat;

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

	public static void main(String[] args) {
		process("滚蛋","");
	}
}
