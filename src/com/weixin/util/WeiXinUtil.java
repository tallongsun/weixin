package com.weixin.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.weixin.auth.AccessToken;
import com.weixin.auth.AuthToken;
import com.weixin.auth.UserInfo;
import com.weixin.manager.MenuManager;

public class WeiXinUtil {
	private static Logger log = LoggerFactory.getLogger(WeiXinUtil.class);  
	
	// 获取access_token的接口地址（GET） 限200（次/天）  
	public final static String access_token_url = 
		"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET"; 
	// 菜单创建（POST） 限100（次/天） 
	public static String menu_create_url = 
		"https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	// 创建带参数的二维码
	private static final String QRCOD_CREATE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
	// 获取带参数的二维码
	private static final String QRCOD_SHOW = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
	
	// 发送客服消息(限用户发送消息给公众号48小时内)
	private static final String MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
	
	// 群发
    private static final String UPLOADNEWS_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=";
    private static final String MASS_SENDALL_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=";
    private static final String MASS_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";
    private static final String MASS_DELETE_URL = "https://api.weixin.qq.com//cgi-bin/message/mass/delete?access_token=";
	
	// 获取用户基本信息
	private static final String USER_INFO_URI = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	
	// 获取关注者列表
	private static final String USER_GET_URI = "https://api.weixin.qq.com/cgi-bin/user/get";
	
	// 分组管理
	private static final String GROUP_CREATE_URI = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=";
	private static final String GROUP_GET_URI = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=";
	private static final String GROUP_GETID_URI = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=";
	private static final String GROUP_UPDATE_URI = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=";
	private static final String GROUP_MEMBERS_UPDATE_URI = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=";
	
	// 上传下载多媒体
	private static final String UPLOAD_MEDIA_URL= "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=";
	private static final String GET_MEDIA_URL= "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=";
	
	
	// OAuth网页授权
	private static final String CODE_URI = "http://open.weixin.qq.com/connect/oauth2/authorize";//授权接口，用户授权后，回调我们的接口，给我们一个Code
	private static final String TOKEN_URI = "https://api.weixin.qq.com/sns/oauth2/access_token";//通过Code换取AUTH_ACCESS_TOKEN接口
	private static final String REFRESH_TOKEN_URI = "https://api.weixin.qq.com/sns/oauth2/refresh_token";//AUTH_ACCESS_TOKEN失效后，通过AUTH_ACCESS_TOKEN换取AUTH_REFRESH_TOKEN接口
	private static final String SNS_USER_INFO_URI = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";//使用AUTH_TOKEN获取用户信息接口
	
	
	
	public static AccessToken getAccessToken(String appid, String appsecret) {  
	    AccessToken accessToken = null;  
	    
	    String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
	    JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
	    // 如果请求成功  
	    if (null != jsonObject) {  
	        try {  
	            accessToken = new AccessToken();  
	            accessToken.setToken(jsonObject.getString("access_token"));  
	            accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));  
	        } catch (JSONException e) {  
	            // 获取token失败  
	            log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));  
	        }  
	    }  
	    return accessToken;  
	} 
	
	public static int createMenu(String jsonMenu, String accessToken) {  
	    int result = 0;  
	  
	    // 拼装创建菜单的url  
	    String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);  
	    // 调用接口创建菜单  
	    JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);  
	  
	    if (null != jsonObject) {  
	        if (0 != jsonObject.getIntValue("errcode")) {  
	            result = jsonObject.getIntValue("errcode");  
	            log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));  
	        }  
	    }  
	  
	    return result;  
	}  
	
	//临时二维码
	public static AccessToken createScene(String accessToken, int expireSeconds,int sceneId) {
		AccessToken ticket = null;  
		Map<String, Object> scene = new HashMap<String, Object>();
		scene.put("scene_id", sceneId);
		
		Map<String, Object> actionInfo = new HashMap<String, Object>();
		actionInfo.put("scene", scene);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("expire_seconds", expireSeconds);
		params.put("action_name", "QR_SCENE");
		params.put("action_info", actionInfo);
		
		String post = JSONObject.toJSONString(params);
		JSONObject jsonObject = httpRequest(QRCOD_CREATE.concat(accessToken),"POST", post);
		
	    if (null != jsonObject) {  
	        try {  
	        	ticket = new AccessToken();  
	        	ticket.setToken(jsonObject.getString("ticket"));  
	        	ticket.setExpiresIn(jsonObject.getIntValue("expire_seconds"));  
	        } catch (JSONException e) {  
	            // 获取token失败  
	            log.error("获取ticket失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));  
	        }  
	    }  
		return ticket;
	}
	
	//永久二维码
	public static AccessToken createLimitScene(String accessToken, int sceneId){
		AccessToken ticket = null;  
		Map<String, Object> scene = new HashMap<String, Object>();
		scene.put("scene_id", sceneId);
		
		Map<String, Object> actionInfo = new HashMap<String, Object>();
		actionInfo.put("scene", scene);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("action_name", "QR_LIMIT_SCENE");
		params.put("action_info", actionInfo);
		String post = JSONObject.toJSONString(params);
		JSONObject jsonObject = httpRequest(QRCOD_CREATE.concat(accessToken),"POST", post);
		
	    if (null != jsonObject) {  
	        try {  
	        	ticket = new AccessToken();  
	        	ticket.setToken(jsonObject.getString("ticket"));
	        	ticket.setExpiresIn(jsonObject.getIntValue("expire_seconds"));
	        } catch (JSONException e) {  
	            // 获取token失败  
	            log.error("获取ticket失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));  
	        }  
	    }
		return ticket;
	}
	
	//查看二维码链接
	public static String showQrcodeUrl(String ticket) {
		return QRCOD_SHOW.concat(ticket);
	}
	
	//推送文本消息
    public static void pushTextMsg(String accessToken,String openId, String text){
        Map<String,Object> json = new HashMap<String,Object>();
        Map<String,Object> textObj = new HashMap<String,Object>();
        textObj.put("content", text);
        json.put("touser", openId);
        json.put("msgtype", "text");
        json.put("text", textObj);
		httpRequest(MESSAGE_URL.concat(accessToken), "POST", JSONObject.toJSONString(json));
	}
    
	public static UserInfo getUserInfo(String accessToken, String openid){
		String reqUrl = USER_INFO_URI.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
		JSONObject obj = httpRequest(reqUrl, "GET", null);  
		log.debug("userBasicInfo:"+obj);
	    if (null != obj) {  
			if(obj.get("errcode") != null){
				log.error(obj.getString("errmsg"));
				return null;
			}
			UserInfo user = JSONObject.toJavaObject(obj, UserInfo.class);
			return user;
	    }
	    return null;
	}
	
	public static JSONObject getFollwersList(String accessToken, String next_openid){
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("next_openid", next_openid);
		JSONObject obj =  httpRequest(initParams(USER_GET_URI,params),"GET", null);
		return obj;
	}
	
	public static JSONObject createGroup(String accessToken, String name){
		Map<String,Object> group = new HashMap<String,Object>();
        Map<String,Object> nameObj = new HashMap<String,Object>();
        nameObj.put("name", name);
        group.put("group", nameObj);
        String post = JSONObject.toJSONString(group);
        JSONObject reslut = httpRequest(GROUP_CREATE_URI.concat(accessToken), "POST",post);
        return reslut;
	}
	
	public static JSONObject getAllGroup(String accessToken){
		JSONObject reslut = httpRequest(GROUP_GET_URI.concat(accessToken),"POST","");
		return reslut;
	}
	
	public static JSONObject getGroup(String accessToken,String openid) {
		JSONObject reslut = httpRequest(GROUP_GETID_URI.concat(accessToken),"POST","{\"openid\":\""+openid+"\"}");
    	return reslut;
	}
	
	public static JSONObject updateGroupName(String accessToken,String id,String name) {
		Map<String,Object> group = new HashMap<String,Object>();
        Map<String,Object> nameObj = new HashMap<String,Object>();
        nameObj.put("name", name);
        nameObj.put("id", id);
        group.put("group", nameObj);
        String post = JSONObject.toJSONString(group);
        JSONObject reslut = httpRequest(GROUP_UPDATE_URI.concat(accessToken), "POST",post);
        return reslut;
	}
	
	public static JSONObject moveGroup(String accessToken,String openid,String to_groupid){
		JSONObject reslut = httpRequest(GROUP_MEMBERS_UPDATE_URI.concat(accessToken), "POST","{\"openid\":\""+openid+"\",\"to_groupid\":"+to_groupid+"}");
        return reslut;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> uploadMedia(String accessToken,String type,File file) throws Exception {
        String url = UPLOAD_MEDIA_URL + accessToken +"&type="+type;
        String jsonStr = upload(url,file);
        return JSON.parseObject(jsonStr, Map.class);
    }
	
    public static Attachment getMedia(String accessToken,String mediaId) throws Exception{
    	String url = GET_MEDIA_URL + accessToken + "&media_id=" + mediaId;
        return download(url);
    }
    
    
    public static JSONObject uploadNews(String accessToken,List<Article> articles) {
    	Map<String,Object> json = new HashMap<String,Object>();
    	json.put("articles", articles);
    	JSONObject jso = httpRequest(UPLOADNEWS_URL.concat(accessToken),"POST", JSONObject.toJSONString(json));
		return jso;
    }
    
    public static JSONObject massSendToGroup(String accessToken,String groupId,String mpNewsMediaId) {
    	Map<String,Object> filter = new HashMap<String,Object>();
    	filter.put("group_id", groupId);
    	
    	Map<String,Object> mpnews = new HashMap<String,Object>();
    	mpnews.put("media_id", mpNewsMediaId);
    	
    	Map<String,Object> json = new HashMap<String,Object>();
    	json.put("mpnews", mpnews);
    	json.put("filter", filter);
    	json.put("msgtype", "mpnews");
    	JSONObject jso = httpRequest(MASS_SENDALL_URL.concat(accessToken),"POST", JSONObject.toJSONString(json));
		return jso;
    }
    
    public static JSONObject massSendToUsers(String accessToken,String[] openids,String mpNewsMediaId) {
    	Map<String,Object> json = new HashMap<String,Object>();
    	Map<String,Object> mpnews = new HashMap<String,Object>();
    	mpnews.put("media_id", mpNewsMediaId);
    	json.put("touser", openids);
    	json.put("mpnews", mpnews);
    	json.put("msgtype", "mpnews");
    	JSONObject jso = httpRequest(MASS_SEND_URL.concat(accessToken), "POST",JSONObject.toJSONString(json));
		return jso;
    }
    
    public static JSONObject cancelMassSend(String accessToken,String msgid) {
    	Map<String,Object> json = new HashMap<String,Object>();
    	json.put("msgid", msgid);
    	JSONObject jso = httpRequest(MASS_DELETE_URL.concat(accessToken), "POST",JSONObject.toJSONString(json));
		return jso;
    }
    
    public static String showOauthUrl() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", "wxf62c8db6d6fc4cd0");
        params.put("response_type", "code");
        params.put("redirect_uri", HttpUtil.urlEncode("http://www.itallong.com/youyou/oauth.jsp","UTF-8"));
        // snsapi_base（不弹出授权页面，只能拿到用户openid）
        // snsapi_userinfo（弹出授权页面，这个可以通过 openid 拿到昵称、性别、所在地）
        params.put("scope", "snsapi_base");
        params.put("state", "wx#wechat_redirect");
        String para = createSign(params, false);
        return CODE_URI + "?" + para;
    }
    
    public static AuthToken getAuthToken(String code) {
    	AuthToken snsToken = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", "wxf62c8db6d6fc4cd0");
        params.put("secret", "813e8db02fdfebbb3b15e808eef922b2");
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        JSONObject jsonObject = httpRequest(initParams(TOKEN_URI, params), "GET",null);
        
	    if (null != jsonObject) {  
	        try {  
	        	snsToken = new AuthToken();  
	        	snsToken.setToken(jsonObject.getString("access_token"));
	        	snsToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
	        	snsToken.setRefreshToken(jsonObject.getString("refresh_token"));
	        	snsToken.setOpenId(jsonObject.getString("openid"));
	        	snsToken.setScope(jsonObject.getString("scope"));
	        } catch (JSONException e) {  
	            // 获取token失败  
	            log.error("获取snsToken失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));  
	        }  
	    }
		return snsToken;
    }
    
    public static AuthToken refreshAuthToken(String refreshToken) {
    	AuthToken snsToken = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", "wxf62c8db6d6fc4cd0");
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);
        JSONObject jsonObject =  httpRequest(initParams(REFRESH_TOKEN_URI, params),"GET",null);
	    if (null != jsonObject) {  
	        try {  
	        	snsToken = new AuthToken();  
	        	snsToken.setToken(jsonObject.getString("access_token"));
	        	snsToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
	        	snsToken.setRefreshToken(jsonObject.getString("refresh_token"));
	        	snsToken.setOpenId(jsonObject.getString("openid"));
	        	snsToken.setScope(jsonObject.getString("scope"));
	        } catch (JSONException e) {  
	            // 获取token失败  
	            log.error("刷新snsToken失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"), jsonObject.getString("errmsg"));  
	        }  
	    }
		return snsToken;
    }
    
    public static UserInfo getAuthUserInfo(String accessToken, String openid){
    	String reqUrl = SNS_USER_INFO_URI.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
	    JSONObject obj = httpRequest(reqUrl, "GET", null);  
	    log.debug("userInfo:"+obj);
	    // 如果请求成功  
	    if (null != obj) {  
			if(obj.get("errcode") != null){
				log.error(obj.getString("errmsg"));
				return null;
			}
			UserInfo user = JSONObject.toJavaObject(obj, UserInfo.class);
			return user;
	    }  
	    return null;  
    }
    
	
	/**
	 * 发送https请求，兼容get和set方法
	 * 
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @return
	 */
	private static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {  
        JSONObject jsonObject = null;  
        StringBuffer buffer = new StringBuffer();  
        try {  
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
            TrustManager[] tm = { new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
            }};  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
  
            URL url = new URL(requestUrl);  
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf);  
  
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod(requestMethod);  
  
            if ("GET".equalsIgnoreCase(requestMethod))  
                httpUrlConn.connect();  
  
            // 当有数据需要提交时  
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
  
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
            jsonObject = JSONObject.parseObject(buffer.toString());  
        } catch (ConnectException ce) {  
            log.error("Weixin server connection timed out.");  
        } catch (Exception e) {  
            log.error("https request error:{}", e);  
        }  
        return jsonObject;  
    }  
	
    private static String createSign(Map<String, String> params, boolean encode) {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = value.toString();
            }
            if (encode) {
                temp.append(HttpUtil.urlEncode(valueString, "UTF-8"));
            } else {
                temp.append(valueString);
            }
        }
        return temp.toString();
    }
    
    private static String initParams(String url, Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.indexOf("?") == -1) {
            sb.append("?");
        }
        boolean isfist = true;
        for (Entry<String, String> entry : params.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
            	sb.append("&");
            }
            sb.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(value)) {
            	sb.append(HttpUtil.urlEncode(value, "UTF-8"));
            }
        }
        return sb.toString();
    }
    
    private static String upload(String url,File file) throws Exception {
        String BOUNDARY = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL"; // multipart需要定义数据分隔线  
        StringBuffer bufferRes = null;
        URL urlGet = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlGet.openConnection();
        conn.setDoOutput(true);  
        conn.setDoInput(true);  
        conn.setUseCaches(false);  
        conn.setRequestMethod("POST");  
        conn.setRequestProperty("connection", "Keep-Alive");  
        conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36");  
        conn.setRequestProperty("Charsert", "UTF-8");   
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  
          
        OutputStream out = new DataOutputStream(conn.getOutputStream());  
        StringBuilder sb = new StringBuilder();    
        //--分割线开始 
        sb.append("--"+BOUNDARY+"\r\n");   
        sb.append("Content-Disposition: form-data;name=\"media\";filename=\""+ file.getName() + "\"\r\n");    
        sb.append("Content-Type:application/octet-stream\r\n\r\n");    
        byte[] data = sb.toString().getBytes();  
        out.write(data);  
        //文件流输出到网络流
        DataInputStream fs = new DataInputStream(new FileInputStream(file));  
        int bytes = 0;  
        byte[] bufferOut = new byte[1024];  
        while ((bytes = fs.read(bufferOut)) != -1) {  
            out.write(bufferOut, 0, bytes);  
        }  
        out.write("\r\n".getBytes()); //多个文件时，二个文件之间加入这个  
        fs.close();  
        out.write(("\r\n--" + BOUNDARY + "--\r\n").getBytes());  
        //--分割线结束
        out.flush();    
        out.close();   

        // 定义BufferedReader输入流来读取URL的响应  
        InputStream in = conn.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String valueString = null;
        bufferRes = new StringBuffer();
        while ((valueString = read.readLine()) != null){
            bufferRes.append(valueString);
        }
        in.close();
        if (conn != null) {
            // 关闭连接
            conn.disconnect();
        }
        return bufferRes.toString();
    }
    
    private static Attachment download(String url) throws IOException{
    	 Attachment att = new Attachment();
         URL _url = new URL(url);
         HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
         // 连接超时
         conn.setConnectTimeout(25000);
         // 读取超时 --服务器响应比较慢，增大时间
         conn.setReadTimeout(25000);
         conn.setRequestMethod("GET");
         conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
         conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
         conn.setDoOutput(true);
         conn.setDoInput(true);
         conn.connect();
         
         if(conn.getContentType().equalsIgnoreCase("text/plain")){
            // 定义BufferedReader输入流来读取URL的响应  
             InputStream in = conn.getInputStream();
             BufferedReader read = new BufferedReader(new InputStreamReader(in, "UTF-8"));
             String valueString = null;
             StringBuffer bufferRes = new StringBuffer();
             while ((valueString = read.readLine()) != null){
                 bufferRes.append(valueString);
             }
             in.close();
             att.setError(bufferRes.toString());
         }else{
             BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());  
             String ds = conn.getHeaderField("Content-disposition");
             String fullName = ds.substring(ds.indexOf("filename=\"")+10,ds.length()-1);
             String relName = fullName.substring(0, fullName.lastIndexOf("."));
             String suffix = fullName.substring(relName.length()+1);

             att.setFullName(fullName);
             att.setFileName(relName);
             att.setSuffix(suffix);
             att.setContentLength(conn.getHeaderField("Content-Length"));
             att.setContentType(conn.getHeaderField("Content-Type"));

             att.setFileStream(bis);
         }
         return att;
    }

	public static void main(String[] args) throws Exception{
		AccessToken token = getAccessToken("wxf62c8db6d6fc4cd0", "813e8db02fdfebbb3b15e808eef922b2");
		if(token!=null){
			createMenu(MenuManager.getTestMenu(), token.getToken());
			
//			AccessToken ticket = createScene(token.getToken(),1800,10000);
//			if(ticket!=null){
//				//https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGl8ToAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL29FejVkTDNsak1MMS1iaFNjMktzAAIEvJ2qUwMECAcAAA==
//				System.out.println(showQrcodeUrl(ticket.getToken()));
//			}
//			ticket = createLimitScene(token.getToken(),1);
//			if(ticket!=null){
//				//https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGY8ToAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL0hrejFQVERsZzhMNmhRUVlmMkNzAAIE06GqUwMEAAAAAA==
//				System.out.println(showQrcodeUrl(ticket.getToken()));
//			}
			
//			pushTextMsg(token.getToken(),"oaKFFtz39jZYQQKvKxtiUX-C9Py8","just for test..");
		
//			String openId = "oaKFFtz39jZYQQKvKxtiUX-C9Py8";//用户ID
//			UserInfo ui = getUserInfo(token.getToken(), openId);
//			System.out.println(ui);
//		
//			openId = "";//第一个拉取的OPENID，不填默认从头开始拉取
//			JSONObject jso = getFollwersList(token.getToken(), openId);
//			System.out.println(jso);
			
//			JSONObject jso = createGroup(token.getToken(), "test");
//			System.out.println(jso);
//			String groupId = jso.getJSONObject("group").getString("id");
//			
//			jso = getGroup(token.getToken(), openId);
//			System.out.println(jso);
//			
//			jso = updateGroupName(token.getToken(), groupId, "星标组");
//			System.out.println(jso);
//			
//			jso = moveGroup(token.getToken(), openId, groupId);
//			System.out.println(jso);
//			
//			jso = getAllGroup(token.getToken());
//			System.out.println(jso);
			
//			String mediaId = uploadMedia(token.getToken(),"image", new File("WebRoot/images/navi.png")).get("media_id").toString();
//			System.out.println(mediaId);
//			Attachment atta = getMedia(token.getToken(),mediaId);
//			byte[] result = new byte[Integer.parseInt(atta.getContentLength())];
//			atta.getFileStream().read(result,0,result.length);
//			FileOutputStream fos = new FileOutputStream( new File(atta.getFullName()));
//			fos.write(result);
			
			//除了上传图文信息素材外，其他接口返回api功能未授权
//			Article article= new Article("mxRN4M1TJwCxYPteWjvbuzT6sk6HNbnHENtkNuA-vV0ENXoFhix-COY8ge2aj0YT","xxx","title","www.qq.com","content","digest");
//			String mediaId = uploadNews(token.getToken(),Arrays.asList(article)).get("media_id").toString();
//			System.out.println(mediaId);
//			JSONObject jso = massSendToGroup(token.getToken(), 100+"", mediaId);
//			System.out.println(jso.getString("errcode")+":"+jso.getString("msg_id"));
//			jso = massSendToUsers(token.getToken(), new String[]{"oaKFFtz39jZYQQKvKxtiUX-C9Py8"}, mediaId);
//			System.out.println(jso.getString("errcode")+":"+jso.getString("msg_id"));
//			jso = cancelMassSend(token.getToken(), jso.getString("msg_id"));
//			System.out.println(jso.getString("errcode"));
			
//			System.out.println(showOauthUrl());
			
//			SnsToken snsToken = getAuthToken("027192b730ffec7b82c244a248407544");
//			System.out.println(snsToken);
//			
//			String snsTokenStr = "OezXcEiiBSKSxW0eoylIeP_fr36CPQ1Moji9_g6H7fiGCxoTD9xr7ksLRzK0zQiaOEwAmzlBmtLibrFXOSV4BdUyvF1oiwA5DFyJC0_mSxdEuDU4IPEpYo6gN4kE6E-KloPRHPo_zeQFW8lOpS_MaQ";
//			snsTokenStr = snsToken.getToken();
//			openId = snsToken.getOpenId();
//			
//			UserInfo sui = getAuthUserInfo(snsTokenStr, openId);
//			System.out.println(sui);
			
//			SnsToken snsToken = refreshAuthToken(snsTokenStr);
//			System.out.println(snsToken);
			
			

		}
	}
	
	public static class Attachment{
		private String fileName;
		private String fullName;
		private String suffix;
		private String contentLength;
		private String contentType;
		private BufferedInputStream fileStream;
		private String error;
		
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getFullName() {
			return fullName;
		}
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
		public String getSuffix() {
			return suffix;
		}
		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}
		public String getContentLength() {
			return contentLength;
		}
		public void setContentLength(String contentLength) {
			this.contentLength = contentLength;
		}
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		public BufferedInputStream getFileStream() {
			return fileStream;
		}
		public void setFileStream(BufferedInputStream fileStream) {
			this.fileStream = fileStream;
		}
		public String getError() {
			return error;
		}
		public void setError(String error) {
			this.error = error;
		}
		@Override
		public String toString() {
			return "Attachment [contentLength=" + contentLength
					+ ", contentType=" + contentType + ", error=" + error
					+ ", fileName=" + fileName + ", fileStream=" + fileStream
					+ ", fullName=" + fullName + ", suffix=" + suffix + "]";
		}
		
	}
	
	public static class Article{
		private String thumb_media_id;
		private String author;
		private String title;
		private String content_source_url;
		private String content;
		private String digest;
		
		public Article(){
			
		}
		
		public Article(String thumbMediaId, String author, String title,
				String contentSourceUrl, String content, String digest) {
			thumb_media_id = thumbMediaId;
			this.author = author;
			this.title = title;
			content_source_url = contentSourceUrl;
			this.content = content;
			this.digest = digest;
		}

		public String getThumb_media_id() {
			return thumb_media_id;
		}
		public void setThumb_media_id(String thumbMediaId) {
			thumb_media_id = thumbMediaId;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent_source_url() {
			return content_source_url;
		}
		public void setContent_source_url(String contentSourceUrl) {
			content_source_url = contentSourceUrl;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getDigest() {
			return digest;
		}
		public void setDigest(String digest) {
			this.digest = digest;
		}
		
	}
}


