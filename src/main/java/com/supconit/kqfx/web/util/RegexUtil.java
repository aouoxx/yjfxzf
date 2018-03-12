package com.supconit.kqfx.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jodd.util.StringUtil;

/**
 * 正则表达式工具类
 * @author hebingting
 *
 */
public class RegexUtil {
	/**
	 * 验证手机号是否正确
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles){
		if(StringUtil.isEmpty(mobiles)){
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}
	public static void main(String[] args) {
		System.out.println(isMobileNO(null));
	}
}
