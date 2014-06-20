package com.weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpUtil {
	public static String urlEncode(String source,String enc) {  
        String result = source;  
        try {  
            result = java.net.URLEncoder.encode(source, enc);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        return result;  
    } 
	
	public static String doGet(String requestUrl,String enc) {  
        StringBuffer buffer = new StringBuffer();  
        BufferedReader bufferedReader = null;  
        HttpURLConnection httpUrlConn = null;
        try {  
            URL url = new URL(requestUrl);  
            httpUrlConn = (HttpURLConnection) url.openConnection();  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.connect();  
            // 将返回的输入流转换成字符串  
            bufferedReader = new BufferedReader(new InputStreamReader(
            		httpUrlConn.getInputStream(), enc));  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally{
            try {
            	if(bufferedReader!=null){
            		bufferedReader.close();
            	}
			} catch (IOException e) {
			}  
            httpUrlConn.disconnect();  
        }
        return buffer.toString();  
    } 
	
}
