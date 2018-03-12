package com.supconit.kqfx.web.util;

/**
 * 编码转译工具类
 * @author cjm
 *
 */
public class EncodeTransforUtil {
	
	/**
	 * 获取GBK编码字节数
	 * @param xxnr
	 * @return
	 */
	public static Integer getGbkCodeingLength(String str) throws Exception{
		
		return str.getBytes("GBK").length;
	}
	
	
	/**
	 * 获取GBK编码截取字符串(根据配置文件中的情报板信息)
	 * @param xxnr
	 * @return
	 */
	public static String getXxnrResult(String xxnr,Integer bytes){
		String result = "";
		//默认情报板字符长度
		if(null==bytes||0==bytes){
			bytes = Integer.valueOf(PropertiesUtil.getByCode("qbb.bytes"));
		}
		try {
			int a = 0;
			for (int i = 0; i < xxnr.length(); i++) {
				String m = xxnr.substring(i,i+1);
				a += m.getBytes("GBK").length;
				if(a<bytes)
					result = result +m;
				else
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
