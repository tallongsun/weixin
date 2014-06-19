package com.weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
	
	public static InputStream doGet(String requestUrl) {  
        InputStream inputStream = null;  
        try {  
            URL url = new URL(requestUrl);  
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.connect();  
            // 获得返回的输入流  
            inputStream = httpUrlConn.getInputStream();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return inputStream;  
    }  
	
	public static String readStream(InputStream in,String enc){
        StringBuilder buffer = new StringBuilder();  
        BufferedReader reader=null;  
        try{  
            reader = new BufferedReader(new InputStreamReader(in,enc));  
            String line=null;  
            while((line = reader.readLine())!=null){  
                buffer.append(line);  
            }  
        }catch(Exception e){  
            e.printStackTrace();  
        }finally{  
            if(null!=reader){  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                }  
            }  
        }  
        return buffer.toString();  
	}
}
