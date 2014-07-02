package com.weixin;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.auth.TokenThread;
import com.weixin.cache.CaoHelper;
import com.weixin.db.DaoHelper;
import com.weixin.logic.chat.ChatService;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = -6014103275870506517L;
	
	public static final Logger log = LoggerFactory.getLogger(RequestServlet.class);

	public static String WEB_ROOT_PATH = "";
	
	
	@Override
	public void init() throws ServletException{
        TokenThread.init(getInitParameter("appid"), getInitParameter("appsecret"));
		
		WEB_ROOT_PATH = getServletContext().getRealPath("/");
		
		log.debug("WEB_ROOT:"+WEB_ROOT_PATH);
		
		String webInfPath = WEB_ROOT_PATH + File.separator+"WEB-INF"+File.separator;
		DaoHelper.init(webInfPath);
		CaoHelper.init(webInfPath);
		ChatService.init(webInfPath);
	}
	
}
