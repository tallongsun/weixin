package com.weixin;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.weixin.chat.ChatService;
import com.weixin.util.DBUtil;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = -6014103275870506517L;

	public static String WEB_ROOT_PATH = "";
	
	
	@Override
	public void init() throws ServletException{
		WEB_ROOT_PATH = getServletContext().getRealPath("/");
		
		String webInfPath = WEB_ROOT_PATH + "WEB-INF"+File.separator;
		DBUtil.init(webInfPath);
		ChatService.init(webInfPath);
	}
	
}
