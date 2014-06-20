package com.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.weixin.msg.BaseResMessage;
import com.weixin.msg.res.TextResMessage;
import com.weixin.msg.res.NewsResMessage.Article;

public class MessageUtil {
	
    private static XStream xstream = new XStream(new XppDriver() {  
        public HierarchicalStreamWriter createWriter(Writer out) {  
            return new PrettyPrintWriter(out) {  
                // 对所有xml节点的转换都增加CDATA标记  
                boolean cdata = true;  
  
                @SuppressWarnings("unchecked")  
                public void startNode(String name, Class clazz) {  
                    super.startNode(name, clazz);  
                }  
  
                protected void writeText(QuickWriter writer, String text) {  
                    if (cdata) {  
                        writer.write("<![CDATA[");  
                        writer.write(text);  
                        writer.write("]]>");  
                    } else {  
                        writer.write(text);  
                    }  
                }  
            };  
        }  
    });
	
    @SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest req){  
        Map<String, String> map = new HashMap<String, String>(); 
        
        InputStream inputStream = null;
		try {
			inputStream = req.getInputStream();
			SAXReader reader = new SAXReader();  
			Document document;
			document = reader.read(inputStream);
 
	        Element root = document.getRootElement();  
	        List<Element> elementList = root.elements();  
	        for (Element e : elementList)  
	            map.put(e.getName(), e.getText());  
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
				}  
			}
		}

        return map;
    }  
    
    public static String messageToXml(BaseResMessage msg) {  
        xstream.alias("xml", msg.getClass());  
        if(msg.getMsgType().equals("news")){
        	xstream.alias("item", new Article().getClass());
        }
        return xstream.toXML(msg);  
    } 
    
    public static void main(String[] args) {
		System.out.println(messageToXml(new TextResMessage("")));
	}
    
}
