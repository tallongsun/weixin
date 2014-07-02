package com.weixin;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebSocketChatServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
	
	public static final Logger log = LoggerFactory.getLogger(RequestServlet.class);

	@Override
	public void configure(WebSocketServletFactory factory) {
		 factory.register(ChatWebSocket.class);

	}

	public static class ChatWebSocket implements WebSocketListener {
		private Session  connection;
		private static Set<ChatWebSocket> users = new CopyOnWriteArraySet<ChatWebSocket>();

		public void onWebSocketClose(int closeCode, String message) {
			log.debug("websocket close...");
			
			users.remove(this);
		}

		public void onWebSocketConnect(Session connection) {
			log.debug("websocket open...");
			
			this.connection = connection;
			users.add(this);
		}


		public void onWebSocketText(String data) {
			log.debug("websocket receive text...");
			
			for (ChatWebSocket user : users) {
				try {
					user.connection.getRemote().sendString(data);
				} catch (Exception e) {
				}
			}
		}

		public void onWebSocketBinary(byte[] arg0, int arg1, int arg2) {
			log.debug("websocket receive binary...");
		}

		public void onWebSocketError(Throwable arg0) {
			log.debug("websocket error...");
		}

	}
}
