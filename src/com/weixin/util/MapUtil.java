package com.weixin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.Base64;
import com.weixin.logic.location.Place;
import com.weixin.logic.location.UserLocation;

public class MapUtil {
	public static UserLocation convertCoord(String lng, String lat) {
		// 百度坐标转换接口
		String convertUrl = "http://api.map.baidu.com/ag/coord/convert?from=2&to=4&x={x}&y={y}";
		convertUrl = convertUrl.replace("{x}", lng);
		convertUrl = convertUrl.replace("{y}", lat);

		UserLocation location = new UserLocation();
		try {
			String jsonCoord = HttpUtil.doGet(convertUrl,"UTF-8");
			JSONObject jsonObject = JSONObject.parseObject(jsonCoord);
			// 对转换后的坐标进行Base64解码
			location.setBd09Lng(new String(Base64.decodeFast(jsonObject.getString("x")),"UTF-8"));
			location.setBd09Lat(new String(Base64.decodeFast(jsonObject.getString("y")),"UTF-8"));
		} catch (Exception e) {
			location = null;
			e.printStackTrace();
		}
		return location;
	}
	
	public static List<Place> searchPlace(String query, String lng, String lat) {
		// 拼装请求地址
		String requestUrl = "http://api.map.baidu.com/place/v2/search?&query=QUERY&location=LAT,LNG&radius=2000&output=xml&scope=2&page_size=10&page_num=0&ak=CA21bdecc75efc1664af5a195c30bb4e";
		requestUrl = requestUrl.replace("QUERY", HttpUtil.urlEncode(query, "UTF-8"));
		requestUrl = requestUrl.replace("LAT", lat);
		requestUrl = requestUrl.replace("LNG", lng);
		// 调用Place API圆形区域检索
		String respXml = HttpUtil.doGet(requestUrl,"UTF-8");
		// 解析返回的xml
		List<Place> placeList = parsePlaceXml(respXml);
		return placeList;
	}
	
	@SuppressWarnings("unchecked")
	private static List<Place> parsePlaceXml(String xml) {
		List<Place> placeList = null;
		try {
			Document document = DocumentHelper.parseText(xml);
			// 得到xml根元素
			Element root = document.getRootElement();
			// 从根元素获取<results>
			Element resultsElement = root.element("results");
			// 从<results>中获取<result>集合
			List<Element> resultElementList = resultsElement.elements("result");
			// 判断<result>集合的大小
			if (resultElementList.size() > 0) {
				placeList = new ArrayList<Place>();
				// POI名称
				Element nameElement = null;
				// POI地址信息
				Element addressElement = null;
				// POI经纬度坐标
				Element locationElement = null;
				// POI电话信息
				Element telephoneElement = null;
				// POI扩展信息
				Element detailInfoElement = null;
				// 距离中心点的距离
				Element distanceElement = null;
				// 遍历<result>集合
				for (Element resultElement : resultElementList) {
					nameElement = resultElement.element("name");
					addressElement = resultElement.element("address");
					locationElement = resultElement.element("location");
					telephoneElement = resultElement.element("telephone");
					detailInfoElement = resultElement.element("detail_info");

					Place place = new Place();
					place.setName(nameElement.getText());
					place.setAddress(addressElement.getText());
					place.setLng(locationElement.element("lng").getText());
					place.setLat(locationElement.element("lat").getText());
					// 当<telephone>元素存在时获取电话号码
					if (null != telephoneElement)
						place.setTelephone(telephoneElement.getText());
					// 当<detail_info>元素存在时获取<distance>
					if (null != detailInfoElement) {
						distanceElement = detailInfoElement.element("distance");
						if (null != distanceElement)
							place.setDistance(Integer.parseInt(distanceElement.getText()));
					}
					placeList.add(place);
				}
				// 按距离由近及远排序
				Collections.sort(placeList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return placeList;
	}
	
	public static void main(String[] args) {
		System.out.println(convertCoord("", ""));
	}
}
