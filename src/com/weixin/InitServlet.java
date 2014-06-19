package com.weixin;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.weixin.chat.ChatService;
import com.weixin.util.DBUtil;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = -6014103275870506517L;


	@Override
	public void init() throws ServletException{
		DBUtil.init(getWebInfPath());
		ChatService.init(getWebInfPath());
	}
	
	
	private String getWebInfPath(){
		return getServletContext().getRealPath("/")+"WEB-INF"+File.separator;
	}
}
