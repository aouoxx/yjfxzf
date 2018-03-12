package com.supconit.kqfx.web.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlAnalysisUtil {
	
	private transient static final Logger logger = LoggerFactory
			.getLogger(XmlAnalysisUtil.class);
	
	public static String getCode(String xml){
		   try {  
	            Document doc;   
	            doc = DocumentHelper.parseText(xml);   
	            //Document doc = reader.read(ffile); //读取一个xml的文件  
	            Element root = doc.getRootElement();  
	            
	            //Attribute testCmd= root.attribute("test");  
	            //System.out.println(testCmd.getName()+"-***--"+testCmd.getValue());                         
	            Element code = root.element("CODE");
	            Element message = root.element("MESSAGE");
	            String falg = code.getTextTrim();
	            logger.info("CODE节点内容*--"+code.getTextTrim());
	            logger.info("MESSAGE节点内容*--"+message.getTextTrim());
	            return falg;
	          
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return null;
	        }  
	}
	
	public static String getMesssage(String xml){
		   try {  
	            Document doc;   
	            doc = DocumentHelper.parseText(xml);   
	            //Document doc = reader.read(ffile); //读取一个xml的文件  
	            Element root = doc.getRootElement();  
	            
	            //Attribute testCmd= root.attribute("test");  
	            //System.out.println(testCmd.getName()+"-***--"+testCmd.getValue());                         
	            Element message = root.element("MESSAGE");
	            String falg = message.getTextTrim();
	            return falg;
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return null;
	        }  
	}
	
	public static String getTime(String xml){
		   try {  
	            Document doc;   
	            doc = DocumentHelper.parseText(xml);   
	            Element root = doc.getRootElement();  
	            
	            Element currentTime = root.element("CURRENTTIME");
	            String time = currentTime.getTextTrim();
	            return time;
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return null;
	        }  
	}

}
