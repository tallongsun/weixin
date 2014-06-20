package com.weixin.music;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.msg.res.MusicResMessage.Music;
import com.weixin.util.HttpUtil;

public class BaiduMusicService {
	public static final Logger log = LoggerFactory.getLogger(MusicLogic.class);
	
	public static Music searchMusic(String musicTitle, String musicAuthor) {  
        // 百度音乐搜索地址  
        String requestUrl = "http://box.zhangmen.baidu.com/x?op=12&count=1&title={TITLE}$${AUTHOR}$$$$";  
        // 对音乐名称、作者进URL编码  
        requestUrl = requestUrl.replace("{TITLE}", HttpUtil.urlEncode(musicTitle,"UTF-8"));  
        requestUrl = requestUrl.replace("{AUTHOR}", HttpUtil.urlEncode(musicAuthor,"UTF-8"));  
        // 处理名称、作者中间的空格  
        requestUrl = requestUrl.replaceAll("\\+", "%20");  
        
        log.debug("music url:"+requestUrl);
  
        // 解析Music  
        Music music = parseMusic(requestUrl);  
  
        // 如果music不为null，设置标题和描述  
        if (null != music) {  
            music.setTitle(musicTitle);  
            // 如果作者不为""，将描述设置为作者  
            if (!"".equals(musicAuthor))  
                music.setDescription(musicAuthor);  
            else  
                music.setDescription("来自百度音乐");  
        }  
        return music;  
    } 
	

	@SuppressWarnings("unchecked")
	private static Music parseMusic(String reqUrl) {
		Music music = null;
		try {
			// 使用dom4j解析xml字符串
			String xmlStr = HttpUtil.doGet(reqUrl,"UTF-8");
			log.debug("encode:"+System.getProperty("file.encoding")+",xmlStr:"+xmlStr);
			
			Document document = DocumentHelper.parseText(xmlStr);
			// 得到xml根元素
			Element root = document.getRootElement();
			// count表示搜索到的歌曲数
			String count = root.element("count").getText();
			// 当搜索到的歌曲数大于0时
			if (!"0".equals(count)) {
				// 普通品质
				List<Element> urlList = root.elements("url");
				// 高品质
				List<Element> durlList = root.elements("durl");

				// 普通品质的encode、decode
				String urlEncode = urlList.get(0).element("encode").getText();
				String urlDecode = urlList.get(0).element("decode").getText();
				// 普通品质音乐的URL
				String url = urlEncode.substring(0,urlEncode.lastIndexOf("/") + 1) + urlDecode;
				if (-1 != urlDecode.lastIndexOf("&"))
					url = urlEncode.substring(0, urlEncode.lastIndexOf("/") + 1)
							+ urlDecode.substring(0, urlDecode.lastIndexOf("&"));

				// 默认情况下，高音质音乐的URL 等于 普通品质音乐的URL
				String durl = url;

				// 判断高品质节点是否存在
				Element durlElement = durlList.get(0).element("encode");
				if (null != durlElement) {
					// 高品质的encode、decode
					String durlEncode = durlList.get(0).element("encode").getText();
					String durlDecode = durlList.get(0).element("decode").getText();
					// 高品质音乐的URL
					durl = durlEncode.substring(0,durlEncode.lastIndexOf("/") + 1)+ durlDecode;
					if (-1 != durlDecode.lastIndexOf("&"))
						durl = durlEncode.substring(0, durlEncode.lastIndexOf("/") + 1)
								+ durlDecode.substring(0, durlDecode.lastIndexOf("&"));
				}
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
	
}
