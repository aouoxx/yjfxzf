package com.supconit.kqfx.web.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadGlobalFile {
	
	private static Properties properties;
	
	public static synchronized Properties getProperties() throws IOException{
		if (properties == null) {
			Properties prop = new Properties(); 
			InputStream in = LoadGlobalFile.class.getResourceAsStream("/global-vars-dev.properties"); 
			prop.load(in); 
			properties = prop;
		} 
	    return properties;
	}

}
