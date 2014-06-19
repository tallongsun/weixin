package com.weixin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

public class SignUtil {
	private static final String TOKEN = "tallong";
	
	public static boolean checkSignature(HttpServletRequest req){
	    String signature = req.getParameter("signature");
	    String timestamp = req.getParameter("timestamp");
	    String nonce = req.getParameter("nonce");
	        		
	    String[] tempAry = new String[]{TOKEN,timestamp,nonce};
	    Arrays.sort(tempAry);
	    StringBuilder sb = new StringBuilder();
	    for(String str : tempAry){
	    	sb.append(str);
	    }
	    String tempStr = sha1( sb.toString() );
		
		if( tempStr.equals(signature) ){
			return true;
		}else{
			return false;
		}
	}
	
    private static String sha1(String inStr) {
        MessageDigest md = null;
        String outStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");     //选择SHA-1，也可以选择MD5
            byte[] digest = md.digest(inStr.getBytes());       //返回的是byet[]，要转化为String存储比较方便
            outStr = bytetoString(digest);
        }catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return outStr;
    }
    
    private static String bytetoString(byte[] digest) {
        String str = "";
        String tempStr = "";
        
        for (int i = 0; i < digest.length; i++) {
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            }else {
                str = str + tempStr;
            }
        }
        return str.toLowerCase();
    }
    
    public static void main(String[] args) throws Exception{
		System.out.println(bytetoString("nihao你好".getBytes("UTF-8")));
	}
}
