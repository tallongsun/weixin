package com.weixin.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoHelper {
	public static final Logger log = LoggerFactory.getLogger(DaoHelper.class);
	
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
	public Connection getConn() {
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
	public void releaseResources(Connection conn, PreparedStatement ps, ResultSet rs) {
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


}
