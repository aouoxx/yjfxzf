package com.supconit.kqfx.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class PropertiesUtil {
	
	private static  Properties properties;
	
	static{
		try {
			properties = new Properties();
			InputStream is = PropertiesUtil.class
					.getResourceAsStream("/config-dev.properties");
			properties.load(is);
			if (properties.isEmpty())
				throw new IOException("找不到config-dev.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getByCode(String code){
		String result = "";
		if(properties.containsKey(code)){
			result = properties.get(code).toString();
		}
		return result;
	}
}
