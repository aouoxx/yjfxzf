package com.supconit.kqfx.web.util;


import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUnicodeUtil {
	public static void main(String[] args) {
		// String str = "其它";
		// System.out.println("汉字：" + str);
		// String s = getString(str);
		// System.out.println("区位码：" + s);
		// System.out.println("机内码：" + Hex2HtmlString(s));
		// // 区位码转换为汉字
		// String a = CodeToChinese(s);
		// System.out.println("区位码转换为汉字：" + a);
		String regEx = "[\\u4e00-\\u9fa5]+";
		// System.out.println(regEx);
		String str = "http://www.字符串123/d其他字符串k";
		System.out.println("原字符串：" + str);
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		System.out.print("提取出来的中文有：");

		while (m.find()) {
			System.out.print(m.group(0) + " ");
			str = str.replaceFirst(m.group(0), HtmlUnicodeUtil
					.chinese2HtmlString(m.group(0)));
		}
		System.out.println();
		System.out.println("转换以后的结果：" + str);
	}
	
	public static String chinese2HtmlString(String s) {
		return Hex2HtmlString(getString(s));
	}

	public static String bytes2HexString(byte b) {
		return bytes2HexString(new byte[] { b });
	}

	// 区位码转换为机内码两位十进制数字+160 转换为十六进制
	public static String Hex2HtmlString(String hexString) { 
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hexString.length(); i = i + 2) {
			String hex = hexString.substring(i, i + 2);
			int hexInt = Integer.valueOf(hex);
			hexInt = hexInt + 160;
			String htmlStr = Integer.toHexString(hexInt).toUpperCase();
			sb.append("%").append(htmlStr);
		}
		return sb.toString();
	}
	
	// 汉字转换成区位码
	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	// 汉字转换成区位码
	public static String getString(String chinese) {
		byte[] bs;
		String s = "";
		try {
			bs = chinese.getBytes("GB2312");

			for (int i = 0; i < bs.length; i++) {
				int a = Integer.parseInt(bytes2HexString(bs[i]), 16);
				if(a - 0x80 - 0x20<10){
					s +="0"+(a - 0x80 - 0x20) + "";	
				}else{
					s += (a - 0x80 - 0x20) + "";
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	// 区位码转换成汉字
	public static String CodeToChinese(String code) {
		String Chinese = "";
		for (int i = 0; i < code.length(); i += 4) {
			byte[] bytes = new byte[2];
			String lowCode = code.substring(i, i + 2);
			int tempLow = Integer.parseInt(lowCode);
			tempLow += 160;
			bytes[0] = (byte) tempLow;
			String highCode = code.substring(i + 2, i + 4);
			int tempHigh = Integer.parseInt(highCode);
			tempHigh += 160;
			bytes[1] = (byte) tempHigh;
			String chara = new String(bytes);
			Chinese += chara;
		}
		return Chinese;
	}
	
	public static String getCode(String s){
		String regEx = "[\\u4e00-\\u9fa5]+";
//		System.out.println("原字符串：" + s);
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(s);
//		System.out.print("提取出来的中文有：");

		while (m.find()) {
//			System.out.print(m.group(0) + " ");
			s = s.replace(m.group(0), HtmlUnicodeUtil
					.chinese2HtmlString(m.group(0)));
		}
//		System.out.println();
//		System.out.println("转换以后的结果：" + s);
		return s;
	}
}
