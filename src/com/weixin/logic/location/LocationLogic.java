package com.weixin.logic.location;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.db.dao.LocationDao;
import com.weixin.msg.BaseResMessage;
import com.weixin.msg.req.LocationReqMessage;
import com.weixin.msg.res.NewsResMessage;
import com.weixin.msg.res.TextResMessage;
import com.weixin.msg.res.NewsResMessage.Article;
import com.weixin.util.MapUtil;

public class LocationLogic {
	public static final Logger log = LoggerFactory.getLogger(LocationLogic.class);
	
	public static BaseResMessage process(LocationReqMessage reqMsg){
		// 用户发送的经纬度
		String lng = reqMsg.getLocation_Y();
		String lat = reqMsg.getLocation_X();
		log.debug("lng:"+lng+",lat:"+lat);
		
		// 坐标转换后的经纬度
		String bd09Lng = null;
		String bd09Lat = null;
		// 调用接口转换坐标
		UserLocation userLocation = MapUtil.convertCoord(lng, lat);
		if (null != userLocation) {
			bd09Lng = userLocation.getBd09Lng();
			bd09Lat = userLocation.getBd09Lat();
			log.debug("bdlng:"+bd09Lng+",bdlat:"+bd09Lat);
		}
		// 保存用户地理位置
		LocationDao.saveUserLocation(reqMsg.getFromUserName(), lng, lat, bd09Lng, bd09Lat);

		StringBuffer buffer = new StringBuffer();
		buffer.append("[愉快]").append("成功接收您的位置！").append("\n\n");
		buffer.append("您可以输入搜索关键词获取周边信息了，例如：").append("\n");
		buffer.append("        附近ATM").append("\n");
		buffer.append("        附近KTV").append("\n");
		buffer.append("        附近厕所").append("\n");
		buffer.append("必须以“附近”两个字开头！");
		return new TextResMessage(buffer.toString());
	}
	
	public static BaseResMessage process(String content,String openId){
        String keyWord = content.replaceAll("^附近[\\+ ~!@#%^-_=]?", "");  
        if ("".equals(keyWord)) {  
            return new TextResMessage(getUsage());
        } 
        UserLocation location = LocationDao.getLastLocation(openId);
        if(location == null){
        	return new TextResMessage(getUsage());
        }
        
        List<Place> placeList = MapUtil.searchPlace(keyWord, location.getBd09Lng(), location.getBd09Lat());
		if (null == placeList || 0 == placeList.size()) {
			return new TextResMessage(String.format("/难过，您发送的位置附近未搜索到“%s”信息！", keyWord));
		} 
		
		List<Article> articleList = makeArticleList(placeList, location.getBd09Lng(), location.getBd09Lat());
		return new NewsResMessage(articleList.size(), articleList);
	}
	
	private static List<Article> makeArticleList(List<Place> placeList, String bd09Lng, String bd09Lat) {
		// 项目的根路径
		String basePath = "http://www.itallong.com/youyou/";
		List<Article> list = new ArrayList<Article>();
		Place place = null;
		for (int i = 0; i < placeList.size(); i++) {
			place = placeList.get(i);
			Article article = new Article();
			article.setTitle(place.getName() + "\n距离约" + place.getDistance() + "米");
			// P1表示用户发送的位置（坐标转换后），p2表示当前POI所在位置
			article.setUrl(String.format(basePath + "route.jsp?p1=%s,%s&p2=%s,%s", bd09Lng, bd09Lat, place.getLng(), place.getLat()));
			// 将首条图文的图片设置为大图
			if (i == 0)
				article.setPicUrl(basePath + "images/poisearch.png");
			else
				article.setPicUrl(basePath + "images/navi.png");
			list.add(article);
		}
		return list;
	}
	
	public static String getUsage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("周边搜索使用说明").append("\n\n");
		buffer.append("1）发送地理位置").append("\n");
		buffer.append("点击窗口底部的“+”按钮，选择“位置”，点“发送”").append("\n\n");
		buffer.append("2）指定关键词搜索").append("\n");
		buffer.append("格式：附近+关键词\n例如：附近ATM、附近KTV、附近厕所");
		return buffer.toString();
	}
}
