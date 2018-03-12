package com.supconit.kqfx.web.util;

import java.util.UUID;

/**
 * ID生成器
 * @author hebingting
 *
 */
public class IDGenerator {
	/**
	 * 
	 * @return
	 */
	public static String idGenerator(){
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

}
