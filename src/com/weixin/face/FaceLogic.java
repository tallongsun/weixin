package com.weixin.face;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.TextResMessage;
import com.weixin.util.HttpUtil;

public class FaceLogic {
	public static final Logger log = LoggerFactory.getLogger(FaceLogic.class);
	
	public static BaseResMessage process(String picUrl){
		log.debug("picUrl"+picUrl);
		 // 默认回复信息  
        String result = "未识别到人脸，请换一张清晰的照片再试！";  
        List<Face> faceList = faceDetect(picUrl);  
        if (null != faceList) {  
            result = makeMessage(faceList);  
        }  
        log.debug("result"+result);
        return new TextResMessage(result); 
	}
	
	private static List<Face> faceDetect(String picUrl) {  
        List<Face> faceList = new ArrayList<Face>();  
        try {  
            // 拼接Face++人脸检测的请求地址  
            String queryUrl = "http://apius.faceplusplus.com/detection/detect?url=URL&api_secret=API_SECRET&api_key=API_KEY";  
            // 对URL进行编码  
            queryUrl = queryUrl.replace("URL", HttpUtil.urlEncode(picUrl, "UTF-8"));  
            queryUrl = queryUrl.replace("API_KEY", "c2af6c03d57c0eb3c47a7488adc88c91");  
            queryUrl = queryUrl.replace("API_SECRET", "iO_yhz5COoKYx6j07nXGtMQCwoy4OhjD");  
            // 调用人脸检测接口  
            String json = HttpUtil.doGet(queryUrl,"UTF-8");  
            // 解析返回json中的Face列表  
            JSONArray jsonArray = JSONObject.parseObject(json).getJSONArray("face");  
            // 遍历检测到的人脸  
            for (int i = 0; i < jsonArray.size(); i++) {  
                // face  
                JSONObject faceObject = (JSONObject) jsonArray.get(i);  
                // attribute  
                JSONObject attrObject = faceObject.getJSONObject("attribute");  
                // position  
                JSONObject posObject = faceObject.getJSONObject("position");  
                Face face = new Face();  
                face.setFaceId(faceObject.getString("face_id"));  
                face.setAgeValue(attrObject.getJSONObject("age").getIntValue("value"));  
                face.setAgeRange(attrObject.getJSONObject("age").getIntValue("range"));  
                face.setGenderValue(genderConvert(attrObject.getJSONObject("gender").getString("value")));  
                face.setGenderConfidence(attrObject.getJSONObject("gender").getDouble("confidence"));  
                face.setRaceValue(raceConvert(attrObject.getJSONObject("race").getString("value")));  
                face.setRaceConfidence(attrObject.getJSONObject("race").getDouble("confidence"));  
                face.setSmilingValue(attrObject.getJSONObject("smiling").getDouble("value"));  
                face.setCenterX(posObject.getJSONObject("center").getDouble("x"));  
                face.setCenterY(posObject.getJSONObject("center").getDouble("y"));  
                faceList.add(face);  
            }  
            // 将检测出的Face按从左至右的顺序排序  
            Collections.sort(faceList);  
        } catch (Exception e) {  
            faceList = null;  
            e.printStackTrace();  
        }  
        return faceList;  
    }
	
	private static String makeMessage(List<Face> faceList) {  
        StringBuffer buffer = new StringBuffer();  
        // 检测到1张脸  
        if (1 == faceList.size()) {  
            buffer.append("共检测到 ").append(faceList.size()).append(" 张人脸").append("\n");  
            for (Face face : faceList) {  
                buffer.append(face.getRaceValue()).append("人种,");  
                buffer.append(face.getGenderValue()).append(",");  
                buffer.append(face.getAgeValue()).append("岁左右").append("\n");  
            }  
        }  
        // 检测到2-10张脸  
        else if (faceList.size() > 1 && faceList.size() <= 10) {  
            buffer.append("共检测到 ").append(faceList.size()).append(" 张人脸，按脸部中心位置从左至右依次为：").append("\n");  
            for (Face face : faceList) {  
                buffer.append(face.getRaceValue()).append("人种,");  
                buffer.append(face.getGenderValue()).append(",");  
                buffer.append(face.getAgeValue()).append("岁左右").append("\n");  
            }  
        }  
        // 检测到10张脸以上  
        else if (faceList.size() > 10) {  
            buffer.append("共检测到 ").append(faceList.size()).append(" 张人脸").append("\n");  
            // 统计各人种、性别的人数  
            int asiaMale = 0;  
            int asiaFemale = 0;  
            int whiteMale = 0;  
            int whiteFemale = 0;  
            int blackMale = 0;  
            int blackFemale = 0;  
            for (Face face : faceList) {  
                if ("黄色".equals(face.getRaceValue()))  
                    if ("男性".equals(face.getGenderValue()))  
                        asiaMale++;  
                    else  
                        asiaFemale++;  
                else if ("白色".equals(face.getRaceValue()))  
                    if ("男性".equals(face.getGenderValue()))  
                        whiteMale++;  
                    else  
                        whiteFemale++;  
                else if ("黑色".equals(face.getRaceValue()))  
                    if ("男性".equals(face.getGenderValue()))  
                        blackMale++;  
                    else  
                        blackFemale++;  
            }  
            if (0 != asiaMale || 0 != asiaFemale)  
                buffer.append("黄色人种：").append(asiaMale).append("男").append(asiaFemale).append("女").append("\n");  
            if (0 != whiteMale || 0 != whiteFemale)  
                buffer.append("白色人种：").append(whiteMale).append("男").append(whiteFemale).append("女").append("\n");  
            if (0 != blackMale || 0 != blackFemale)  
                buffer.append("黑色人种：").append(blackMale).append("男").append(blackFemale).append("女").append("\n");  
        }  
        // 移除末尾空格  
        buffer = new StringBuffer(buffer.substring(0, buffer.lastIndexOf("\n")));  
        return buffer.toString();  
    }  
	
	private static String genderConvert(String gender) {  
        String result = "男性";  
        if ("Male".equals(gender))  
            result = "男性";  
        else if ("Female".equals(gender))  
            result = "女性";  
  
        return result;  
    } 
	
	private static String raceConvert(String race) {  
        String result = "黄色";  
        if ("Asian".equals(race))  
            result = "黄色";  
        else if ("White".equals(race))  
            result = "白色";  
        else if ("Black".equals(race))  
            result = "黑色";  
        return result;  
    }
	
	public static void main(String[] args) {
		process("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
	}
}
