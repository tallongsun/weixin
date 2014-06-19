package com.weixin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.util.SignUtil;

public class RequestServlet extends HttpServlet {
	private static final long serialVersionUID = -1553690800373388100L;
	
	public static final Logger log = LoggerFactory.getLogger(RequestServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String echoStr = req.getParameter("echostr");
		log.debug("echoStr:"+echoStr);
		if(echoStr == null || echoStr.isEmpty()){
			log.error("echoStr is null");
			return;
		}
		if(SignUtil.checkSignature(req)){
			resp.getWriter().print(echoStr);
			resp.getWriter().flush();
			resp.getWriter().close();
		}else{
			log.error("check signature fail");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		String respMsg = RequestHandler.processRequest(req);
		if(respMsg !=null){
	        resp.getWriter().print(respMsg);  
			resp.getWriter().flush();
			resp.getWriter().close();
		}
	}


}
