package com.supconit.kqfx.web.util;


public class UtilTool {
	/**
	 * * 判断输入的字符串是否包含GBK编码格式的字符。* 在此方法中，首先将字符串以ISO-8859-1编码格式提取并转化为byte[]数组；*
	 * 然后判断该数组中是否包含'?'（ASCII码值为63），如果有，则该字符串中包含GBK编码格式的字符。*
	 * 当然，为了排除原字符串中的'?'的干扰，应首先将'?'替换掉。*
	 * 
	 * @param string
	 *            String 字符串。*
	 * @throws UnsupportedEncodingException
	 *             如果系统不支持ISO-8859-1编码格式，将抛出此异常。*
	 * @return boolean 如果输入的字符串包含GBK编码格式的字符，则返回true；否则返回false。*
	 * @see Transform#toGBK*
	 * @see Transform#toISO_8859_1
	 */
	public static boolean isGBK(String string)
			throws java.io.UnsupportedEncodingException {
		byte[] bytes = string.replace('?', 'a').getBytes("ISO-8859-1");
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] == 63) {
				return true;
			}
		}
		return false;
	}

	/**
	 * * 实现将字符串转换为GBK编码格式的字符串，并返回转换结果。*
	 * 如果原始字符串为ISO-8859-1编码格式的字符串，则返回转化为GBK编码格式的字符串；*
	 * 如果原始字符串本身即为GBK编码格式的字符串，则返回原字符串，即不进行编码格式的转换。*
	 * 判断字符串是否为GBK编码格式的标准是判断该字符串中是否包含GBK编码格式的字符。*
	 * 
	 * @param string
	 *            String 欲转换的字符串。*
	 * @throws UnsupportedEncodingException
	 *             如果系统不支持ISO-8859-1或GBK编码格式，将抛出此异常。*
	 * @return String 转换后的字符串。*
	 * @see Transform#isGBK*
	 * @see Transform#toISO_8859_1
	 */
	public static String toGBK(String string)
			throws java.io.UnsupportedEncodingException {
		if (string == null)
			return "";
		if (!isGBK(string)) {
			return new String(string.getBytes("ISO-8859-1"), "GBK");
		}
		return string;
	}

	/**
	 * * 实现将字符串转换为ISO-8859-1编码格式的字符串，并返回转换结果。*
	 * 如果原始字符串为GBK编码格式的字符串，则返回转化为ISO-8859-1编码格式的字符串；*
	 * 如果原始字符串本身即为ISO-8859-1编码格式的字符串，则返回原字符串，即不进行编码格式的转换。*
	 * 判断字符串是否为ISO-8859-1编码格式的标准是判断该字符串中是否未包含GBK编码格式的字符。*
	 * 
	 * @param string
	 *            String 欲转换的字符串。*
	 * @throws UnsupportedEncodingException
	 *             如果系统不支持ISO-8859-1或GBK编码格式，将抛出此异常。*
	 * @return String 转换后的字符串。*
	 * @see Transform#isGBK*
	 * @see Transform#toGBK
	 */
	public static String toISO_8859_1(String string)
			throws java.io.UnsupportedEncodingException {
		if (string == null)
			return "";
		if (isGBK(string)) {
			return new String(string.getBytes("GBK"), "ISO-8859-1");
		}
		return string;
	}
	
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

}