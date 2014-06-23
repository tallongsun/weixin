package com.weixin.logic.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.weixin.cache.cao.WeatherCao;
import com.weixin.msg.BaseResMessage;
import com.weixin.msg.req.VoiceReqMessage;
import com.weixin.msg.res.TextResMessage;
import com.weixin.util.HttpUtil;

public class WeatherLogic {
	public static final Logger log = LoggerFactory.getLogger(WeatherLogic.class);

	public static BaseResMessage process(VoiceReqMessage voiceReqMsg) {
		String weather = WeatherCao.getWeather();
		if(weather!=null){
			return new TextResMessage(weather);
		}
		
		String jsoStr = weather("101070201");
		
		JSONObject jso = JSONObject.parseObject(jsoStr);
		
		JSONObject weatherInfo = jso.getJSONObject("weatherinfo");
		if(weatherInfo==null){
			return new TextResMessage("抱歉，没有查到天气信息！");
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("【"+weatherInfo.getString("city")+"天气预报】\n")
			.append(weatherInfo.getString("date_y")+" "+weatherInfo.getString("fchh")+"时发布\n\n")
			.append("实时天气\n")
			.append(weatherInfo.getString("weather1")+" "+weatherInfo.getString("temp1")+" "+weatherInfo.getString("wind1")+"\n\n")
			.append("温馨提示："+weatherInfo.getString("index_d")+"\n\n")
			.append("明天\n")
			.append(weatherInfo.getString("weather2")+" "+weatherInfo.getString("temp2")+" "+weatherInfo.getString("wind2")+"\n\n")
			.append("后天\n")
			.append(weatherInfo.getString("weather3")+" "+weatherInfo.getString("temp3")+" "+weatherInfo.getString("wind3")+"\n\n");
		WeatherCao.setWeather(sb.toString());
		log.debug(sb.toString());	
		
		return new TextResMessage(sb.toString());
	}

	private static String weather(String code){
		String jsonStr = HttpUtil.doGet("http://m.weather.com.cn/data/"+code+".html", "UTF-8");
		return jsonStr;
	}
	
	public static void main(String[] args) {
		process(null);
	}
	
}
