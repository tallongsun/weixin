package com.weixin.music;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weixin.msg.res.MusicResMessage.Music;
import com.weixin.util.HttpUtil;

public class QQMusicService {
	public static final Logger log = LoggerFactory.getLogger(MusicLogic.class);
	
	public static Music searchMusic(String musicTitle, String musicAuthor) {  
        // 百度音乐搜索地址  
        String requestUrl = "http://shopcgi.qqmusic.qq.com/fcgi-bin/shopsearch.fcg?value={TITLE}&artist={AUTHOR}&page_record_num=1";  
        // 对音乐名称、作者进URL编码  
        requestUrl = requestUrl.replace("{TITLE}", HttpUtil.urlEncode(musicTitle,"GB2312"));  
        requestUrl = requestUrl.replace("{AUTHOR}", HttpUtil.urlEncode(musicAuthor,"GB2312"));  
        // 处理名称、作者中间的空格  
        requestUrl = requestUrl.replaceAll("\\+", "%20");  
        
        log.debug("music url:"+requestUrl);
  
        // 查询并获取返回结果  
        InputStream inputStream = HttpUtil.doGet(requestUrl);  
        // 从返回结果中解析出Music  
        Music music = parseMusic(inputStream);  
  
        // 如果music不为null，设置标题和描述  
        if (null != music) {  
            music.setTitle(musicTitle);  
            // 如果作者不为""，将描述设置为作者  
            if (!"".equals(musicAuthor))  
                music.setDescription(musicAuthor);  
            else  
                music.setDescription("来自QQ音乐");  
        }  
        return music;  
    } 
	

	private static Music parseMusic(InputStream inputStream) {
		Music music = null;
		try {
			String jsonStr = HttpUtil.readStream(inputStream,"GB2312");
			jsonStr = jsonStr.substring(jsonStr.indexOf('(')+1,jsonStr.lastIndexOf(')'));
			log.debug("encode:"+System.getProperty("file.encoding")+",jsonStr:"+jsonStr);
			
			JSONObject jso = JSONObject.parseObject(jsonStr);
			JSONArray jsoAry = jso.getJSONArray("songlist");
			if(jsoAry.size()>0){
				JSONObject subJso = jsoAry.getJSONObject(0);
				String loc = subJso.getString("location");
				String songId = subJso.getString("song_id");
				String url = String.format("http://stream1%s.qqmusic.qq.com/3%s.mp3", loc,songId);
				log.debug("musicUrl:"+url);
				String durl = url;
				music = new Music();
				// 设置普通品质音乐链接
				music.setMusicUrl(url);
				// 设置高品质音乐链接
				music.setHQMusicUrl(durl);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return music;
	}
	
    public static void main(String[] args) {
    	System.out.println(QQMusicService.searchMusic("大约在冬季",""));
	}
}
