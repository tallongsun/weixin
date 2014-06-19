package com.weixin.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.chat.Knowledge;

public class DBUtil {
	public static final Logger log = LoggerFactory.getLogger(DBUtil.class);
	
	private static String url = "jdbc:mysql://localhost:3306/chat";
	
	private static String ip = "localhost";
	private static String port = "3306";
	private static String username = "root";
	private static String password = "root@mysql";
	private static String dbname = "chat";
	
	@SuppressWarnings("unchecked")
	public static void init(String rootPath){
		SAXReader saxReader = new SAXReader();
		Document document=null;
		String path = rootPath + "/config/database.xml";
		
		try {
			document = saxReader.read(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		List dbServer = document.selectNodes("/database");
		Iterator iter = dbServer.iterator();
		while(iter.hasNext()){
			Element ele=(Element)iter.next();
			for(Iterator i=ele.attributeIterator();i.hasNext();){
				Attribute attr = (Attribute) i.next();
				String aName = attr.getName();
				String aValue = attr.getValue();
				if (aName.equals("db_ip") && aValue!=null && aValue.trim().length()>0) ip=aValue;
				if (aName.equals("db_port") && aValue!=null && aValue.trim().length()>0) port=aValue;
				if (aName.equals("db_username") && aValue!=null && aValue.trim().length()>0) username=aValue;
				if (aName.equals("db_password") && aValue!=null && aValue.trim().length()>0) password=aValue;
				if (aName.equals("db_name") && aValue!=null && aValue.trim().length()>0) dbname=aValue;
			}
		}
		//获得链接URL
		url = "jdbc:mysql://" + ip + ":" + port + "/"+ dbname + "?useUnicode=true&characterEncoding=utf-8";
		log.debug(url);
	}
	
	/**
	 * 获取Mysql数据库连接
	 * 
	 * @return Connection
	 */
	private Connection getConn() {
		Connection conn = null;
		try {
			// 加载MySQL驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 释放JDBC资源
	 * 
	 * @param conn 数据库连接
	 * @param ps
	 * @param rs 记录集
	 */
	private void releaseResources(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (null != rs)
				rs.close();
			if (null != ps)
				ps.close();
			if (null != conn)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取问答知识表中所有记录
	 * 
	 * @return List<Knowledge>
	 */
	public static List<Knowledge> findAllKnowledge() {
		List<Knowledge> knowledgeList = new ArrayList<Knowledge>();
		String sql = "select * from knowledge";
		DBUtil mysqlUtil = new DBUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = mysqlUtil.getConn();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Knowledge knowledge = new Knowledge();
				knowledge.setId(rs.getInt("id"));
				knowledge.setQuestion(rs.getString("question"));
				knowledge.setAnswer(rs.getString("answer"));
				knowledge.setCategory(rs.getInt("category"));
				knowledgeList.add(knowledge);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			mysqlUtil.releaseResources(conn, ps, rs);
		}
		return knowledgeList;
	}

	/**
	 * 获取上一次的聊天类别
	 * 
	 * @param openId 用户的OpenID
	 * @return chatCategory
	 */
	public static int getLastCategory(String openId) {
		int chatCategory = -1;
		String sql = "select chat_category from chat_log where open_id=? order by id desc limit 0,1";

		DBUtil mysqlUtil = new DBUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = mysqlUtil.getConn();
			ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			rs = ps.executeQuery();
			if (rs.next()) {
				chatCategory = rs.getInt("chat_category");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			mysqlUtil.releaseResources(conn, ps, rs);
		}
		return chatCategory;
	}

	/**
	 * 根据知识id随机获取一个答案
	 * 
	 * @param knowledgeId 问答知识id
	 * @return
	 */
	public static String getKnowledSub(int knowledgeId) {
		String knowledgeAnswer = "";
		String sql = "select answer from knowledge_sub where pid=? order by rand() limit 0,1";

		DBUtil mysqlUtil = new DBUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = mysqlUtil.getConn();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, knowledgeId);
			rs = ps.executeQuery();
			if (rs.next()) {
				knowledgeAnswer = rs.getString("answer");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			mysqlUtil.releaseResources(conn, ps, rs);
		}
		return knowledgeAnswer;
	}

	/**
	 * 随机获取一条笑话
	 * 
	 * @return String
	 */
	public static String getJoke() {
		String jokeContent = "";
		String sql = "select joke_content from joke order by rand() limit 0,1";

		DBUtil mysqlUtil = new DBUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = mysqlUtil.getConn();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				jokeContent = rs.getString("joke_content");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			mysqlUtil.releaseResources(conn, ps, rs);
		}
		return jokeContent;
	}

	/**
	 * 保存聊天记录
	 * 
	 * @param openId 用户的OpenID
	 * @param createTime 消息创建时间
	 * @param reqMsg 用户上行的消息
	 * @param respMsg 公众账号回复的消息
	 * @param chatCategory 聊天类别
	 */
	public static void saveChatLog(String openId, String createTime, String reqMsg, String respMsg, int chatCategory) {
		String sql = "insert into chat_log(open_id, create_time, req_msg, resp_msg, chat_category) values(?, ?, ?, ?, ?)";

		DBUtil mysqlUtil = new DBUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = mysqlUtil.getConn();
			ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			ps.setString(2, createTime);
			ps.setString(3, reqMsg);
			ps.setString(4, respMsg);
			ps.setInt(5, chatCategory);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			mysqlUtil.releaseResources(conn, ps, rs);
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(findAllKnowledge());
	}
}
