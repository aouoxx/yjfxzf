package com.supconit.kqfx.web.util;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * @author zongkai
 * 字符串共通函数
 * 
 */
public class StringUtil {

	/**
	 * 判断是否为空或者为""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null) {
			return true;
		}
		if (str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判定是否为NULL或者空串
	 * 
	 * @param sIn
	 * @return
	 */
	public static String nvl(String sIn) {
		return (sIn == null) ? "" : sIn;
	}

	/** 从左边开始添加空格 */
	public static final int DIRECT_LEFT = 0;
	/** 从右边边开始添加空格 */
	public static final int DIRECT_RIGHT = 1;

	public static boolean isNullOrEmpty(CharSequence argCharSeq) {

		if ((argCharSeq == null) || (argCharSeq.toString().trim().length() < 1)) {
			return true;
		}

		return false;
	}

	public static boolean isNullOrEmpty(Timestamp timestamp) {

		if ((timestamp == null) || (timestamp.toString().trim().length() < 1)) {
			return true;
		}

		return false;
	}

	public static boolean isNullOrEmpty(List<?> list) {

		if ((list == null) || (list.size() == 0)) {
			return true;
		}

		return false;
	}

	public static boolean equalsString(String argStr1, String argStr2) {

		if ((argStr1 == null) && (argStr2 == null)) {
			return true;
		}
		if ((argStr1 == null) || (argStr2 == null)) {
			return false;
		}
		return argStr1.equals(argStr2);
	}

	public static String getFirstLowerCase(String argString) {

		if (isNullOrEmpty(argString)) {
			return "";
		}
		if (argString.length() < 2) {
			return argString.toLowerCase();
		}

		char ch = argString.charAt(0);
		ch = Character.toLowerCase(ch);

		return ch + argString.substring(1);
	}

	public static String getFirstUpperCase(String argString) {

		if (isNullOrEmpty(argString)) {
			return "";
		}
		if (argString.length() < 2) {
			return argString.toUpperCase();
		}

		char ch = argString.charAt(0);
		ch = Character.toUpperCase(ch);

		return ch + argString.substring(1);
	}

	public static void appendLine(StringBuffer argSbf) {
		argSbf.append(System.getProperty("line.separator"));
	}

	/**
	 * 格式化字符串
	 * 
	 * @param src
	 * @param argParams
	 * @return
	 */
	public static String formatMsg(String src, Object... argParams) {
		return String.format(src, argParams);
	}

	public static int getCount(String src, String strChar) {
		int result = 0;

		int beginIndex = 0;
		int endIndex = src.indexOf(strChar, beginIndex);

		while (endIndex >= 0) {
			result++;
			beginIndex = endIndex + strChar.length();
			endIndex = src.indexOf(strChar, beginIndex);
		}

		return result;
	}

	/**
	 * 字符串替换
	 * 
	 * @param src
	 * @param sFnd
	 * @param sRep
	 * @return
	 */
	public static String replaceStr(String src, String sFnd, String sRep) {
		if (src == null || "".equals(nvl(sFnd))) {
			return src;
		}

		String sTemp = "";
		int endIndex = 0;
		int beginIndex = 0;
		do {
			endIndex = src.indexOf(sFnd, beginIndex);
			if (endIndex >= 0) { // mean found it.
				sTemp = sTemp + src.substring(beginIndex, endIndex) + nvl(sRep);
				beginIndex = endIndex + sFnd.length();
			} else if (beginIndex >= 0) {
				sTemp = sTemp + src.substring(beginIndex);
				break;
			}
		} while (endIndex >= 0);

		return sTemp;
	}

	/**
	 * 得到下一个序列字符串
	 * 
	 * @param argStr
	 * @param argNumLen
	 * @return
	 */
	public static String getNextString(String argStr, int argNumLen) {
		String strResult = argStr;
		String strTempNum = "0";
		if (argStr.length() > argNumLen) {
			int index = argStr.length() - argNumLen;
			strResult = argStr.substring(0, index);

			strTempNum = argStr.substring(index + 1);
		}
		StringBuilder sb = new StringBuilder("0000000000");
		long lngNo = Long.parseLong(strTempNum);
		sb.append(++lngNo);

		strResult += sb.toString().substring(sb.length() - argNumLen);
		return strResult;
	}

	/**
	 * SQL文替换
	 * 
	 * @param argSql
	 * @param argParams
	 * @return
	 */
	public static String replaceSqlParams(String argSql, Object[] argParams) {
		StringBuilder sb = new StringBuilder();

		final String REPLACE_CHAR = "?";
		int fromIndex = 0;
		int pos = -1;
		int index = 0;
		while ((pos = argSql.indexOf(REPLACE_CHAR, fromIndex)) >= 0) {
			sb.append(argSql.substring(fromIndex, pos));
			String strParam = getParamValue(argParams, index++);
			sb.append(strParam);
			fromIndex = pos + REPLACE_CHAR.length();
		}
		sb.append(argSql.substring(fromIndex));

		return sb.toString().replaceAll("(=)(\\s*+NULL\\b)", "IS$2");
	}

	/**
	 * SQL文字符串的设定
	 * 
	 * @param params
	 * @param index
	 * @return
	 */
	private static String getParamValue(Object[] params, int index) {
		Object obj = params[index];

		String result = "";
		if (obj == null) {
			result = "NULL";
		} else if (obj instanceof String) {
			result = "'" + obj + "'";
		} else if (obj instanceof Date) {
			result = "TO_DATE('"
					+ DateUtil.formatDate((Date) obj,
							DateUtil.DATE_FORMAT_TIME_T)
					+ "', 'yyyy/mm/dd HH24:MI:SS')";
		} else {
			result = "'" + obj + "'";
		}

		return result;
	}

	/**
	 * 字符串比较
	 * 
	 * @param argStr1
	 *            字符串1
	 * @param argStr2
	 *            字符串2
	 * @return boolean
	 */
	public static int compare(String argStr1, String argStr2) {
		if (argStr1 == null && argStr2 == null) {
			return 0;
		}
		if (argStr1 == null) {
			return -1;
		}
		if (argStr2 == null) {
			return 1;
		}

		return argStr1.compareTo(argStr2);
	}

	/**
	 * 给指定的str添加到指定的长度(全角算2,一律在字符串右边添加空格),超过长度的截取
	 * 
	 * @param strIn
	 *            要添加的字符串
	 * @param len
	 *            添加到的长度
	 * @param direct
	 *            从左边添加还是右边添加]
	 * 
	 * @return 添加到的指定长度
	 */
	public static String paddingSpaceForChar(String strIn, int len, int direct) {
		if (strIn == null) {
			return null;
		}
		int strInLen = getStrLength(strIn);
		if (strInLen == len) {
			return strIn;

		} else if (strInLen > len) {
			return chopStr(strIn, len);

		} else {
			StringBuffer sb = new StringBuffer((strIn == null ? "" : strIn));
			for (int i = 0; i < (len - strInLen); i++) {
				sb.append(" ");
			}
			return sb.toString();
		}
	}

	/**
	 * 判定是不是全角
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isHalf(char c) {
		if (!(('\uFF61' <= c) && (c <= '\uFF9F'))
				&& !(('\u0020' <= c) && (c <= '\u007E'))) {
			return false;
		} else
			return true;
	}

	/**
	 * 获取字符串长度(全角算2)
	 * 
	 * @param s
	 * @return
	 */
	public static int getStrLength(String s) {
		if (s == null) {
			return 0;
		}

		int len = 0;
		for (int i = 0; i < s.length(); i++) {
			if (isHalf(s.charAt(i))) {
				len += 1;
			} else {
				len += 2;
			}
		}
		return len;
	}

	/**
	 * 截取字符串
	 * 
	 * @param s
	 * @param byteLen
	 * @return
	 */
	public static String chopStr(String s, int byteLen) {
		if (byteLen < 0) {
			return "";
		}
		if (s == null) {
			return null;
		}

		int len = 0;
		for (int i = 0; i < s.length(); i++) {
			if (isHalf(s.charAt(i))) {
				len += 1;
			} else {
				len += 2;
			}
			if (len > byteLen) {
				return s.substring(0, i);
			}
		}
		return s;
	}

	/**
	 * 动态列的JSON数据绑定
	 * 
	 * @param str
	 * @param dyns
	 * @return
	 */
	public static String processJSON(String str, Map<String, String[]> dyns) {
		String[] keys = dyns.keySet().toArray(new String[0]);
		String[] toks = str.split("}");
		if (toks.length < 2 || keys.length == 0 || dyns.get(keys[0]) == null
				|| (toks.length - 1 != dyns.get(keys[0]).length)) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0, len = toks.length - 1; i < len; i++) {
			StringBuilder innerSB = new StringBuilder(toks[i]);
			for (int j = 0, size = keys.length; j < size; j++) {
				innerSB.append(",\"" + keys[j] + "\":");
				String tep = dyns.get(keys[j])[i];
				if (tep == null) {
					innerSB.append("null");
				} else {
					innerSB.append("\"" + tep + "\"");
				}
			}
			sb.append(innerSB.toString());
			sb.append("}");
		}
		sb.append(toks[toks.length - 1]);
		return sb.toString();
	}

	public static String nulConvToStr(Object obj) {

		if (obj == null) {
			return "";
		} else if (obj instanceof String) {
			return String.valueOf(obj);
		}

		return obj.toString();
	}



	public static double strConvertToDouble(String str) {
		if (str == null || str.trim().length() == 0) {
			return Double.valueOf(0);
		}
		return Double.valueOf(str);
	}
	
	
	
	public static BigDecimal dblConvDecimal(Double d) {
		if (d == null)
			return BigDecimal.valueOf(0);
		
		return BigDecimal.valueOf(d);
	}
	
	public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	 } 
	
	public static String getNextString(String value){
		String next = "";
		if(!isNullOrEmpty(value) && isNumeric(value)){
			int len = value.length();
			int tmp = Integer.parseInt(value);
			next = String.valueOf(++tmp);
			int nextLen = next.length();
			if(nextLen <= len){
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < len-nextLen; i++){
					sb.append("0");
				}
				sb.append(next);
				next = sb.toString();
			}
		}
		return next;
	}
	/**
	 * 左边填充字符串
	 * @param str 要填充的字符串
	 * @param fixedLength 字符串要返回的长度
	 * @param fillingChar  字符串在长度不够时，右边填充的字符
	 * @return
	 */
	public static String padLeft(String str, int fixedLength, char fillingChar) {  
        if (str == null) {  
            str = "";  
            int space = fixedLength;  
            while (space-- != 0) {  
                str = fillingChar + str;  
            }  
            return str;  
        }  
        if (str.length() < fixedLength) {  
            int space = fixedLength - str.length();  
            while (space-- != 0) {  
                str = fillingChar + str;  
            }  
        }  
        return str;  
    }
	/**
	 * 右边填充字符串
	 * @param str 要填充的字符串
	 * @param fixedLength 字符串要返回的长度
	 * @param fillingChar  字符串在长度不够时，右边填充的字符
	 * @return
	 */
	public static String padRight(String str, int fixedLength, char fillingChar) {  
        if (str == null) {  
            str = "";  
            int space = fixedLength;  
            while (space-- != 0) {  
                str = str + fillingChar;  
            }  
            return str;  
        }  
        if (str.length() < fixedLength) {  
            int space = fixedLength - str.length();  
            while (space-- != 0) {  
                str = str + fillingChar;  
            }  
        }  
        return str;  
    }
	
	/**
	 * 生成UUID
	 * @return uuid
	 */
	public static String getUUID(){
	    return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public void main(String[] args){
		System.out.println("uuid:"+getUUID());
	}
		
	
    
	/**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
	    	if(input == null){
	    		return null;
	    	}
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == ' ') {
                 c[i] = '\u3000';
               } else if (c[i] < '\177') {
                 c[i] = (char) (c[i] + 65248);

               }
             }
             return new String(c);
    }


    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {
        
	    	if(input == null){
	    		return null;
	    	}
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == '\u3000') {
                 c[i] = ' ';
               } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                 c[i] = (char) (c[i] - 65248);

               }
             }
        String returnString = new String(c);
        
             return returnString;
    }
    
   
}
