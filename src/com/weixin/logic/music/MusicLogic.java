package com.weixin.logic.music;

import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.MusicResMessage;
import com.weixin.msg.res.TextResMessage;
import com.weixin.msg.res.MusicResMessage.Music;

public class MusicLogic {
	
	
	public static BaseResMessage process(String content){
        // 将歌曲2个字及歌曲后面的+、空格、-等特殊符号去掉  
        String keyWord = content.replaceAll("^歌曲[\\+ ~!@#%^-_=]?", "");  
        // 如果歌曲名称为空  
        if ("".equals(keyWord)) {  
            return new TextResMessage(getUsage());
        } 
        String[] kwArr = keyWord.split("@");  
        // 歌曲名称  
        String musicTitle = kwArr[0];  
        // 演唱者默认为空  
        String musicAuthor = "";  
        if (2 == kwArr.length)  
            musicAuthor = kwArr[1];  

        // 搜索音乐  
        Music music = QQMusicService.searchMusic(musicTitle, musicAuthor);  
        // 未搜索到音乐  
        if (null == music) {  
        	return new TextResMessage(
        			"对不起，没有找到你想听的歌曲<" + musicTitle + ">。");
        } 
        // 音乐消息  
        return new MusicResMessage(music);  
        
	}
	

    private  static String getUsage() {  
        StringBuffer buffer = new StringBuffer();  
        buffer.append("歌曲点播操作指南").append("\n\n");  
        buffer.append("回复：歌曲+歌名").append("\n");  
        buffer.append("例如：歌曲存在").append("\n");  
        buffer.append("或者：歌曲存在@汪峰").append("\n\n");  
        buffer.append("回复“?”显示主菜单");  
        return buffer.toString();  
    } 
    
}
