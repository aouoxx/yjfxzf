/**
 * 
 */
package com.supconit.kqfx.web.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理类
 * 
 * @author zongkai
 * 
 */
public class DateUtil {

	/** yyyy-MM-dd格式 */
	public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	/** yyyy-MM-dd HH:mm格式 */
	public static final String DATE_FORMAT_TIME_R = "yyyy-MM-dd HH:mm";
	/** yyyy-MM-dd HH:mm:ss格式 */
	public static final String DATE_FORMAT_TIME_T = "yyyy-MM-dd HH:mm:ss";
	/** yyyyMMddHHmmss格式 */
	public static final String DB_TIME_PATTERN = "yyyyMMddHHmmss";
	public static final String DATE_FORMAT_YYYYMMDD_TWO = "yyyyMMdd";
	/**HHmmss格式**/
	public static final String DATE_FORMAT_HMS = "HHmmss";
	public static final String DATE_FORMAT_YYYYMM = "yyyymm";
	
	public static final String DATE_FORMAT_TIME_AVRO = "yyyy/MM/dd HH:mm:ss";

	/**
	 * 格式化日期
	 * 
	 * @param argDate
	 *            需要格式化的日期
	 * @param argFormat
	 *            格式
	 * @return
	 */
	public static String formatDate(Date argDate, String argFormat) {
		if (argDate == null) {
			return "";
		}

		SimpleDateFormat sdfFrom = null;
		// 结果
		String strResult = null;

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			strResult = sdfFrom.format(argDate).toString();
		} catch (Exception e) {
			strResult = "";
		} finally {
			sdfFrom = null;
		}

		// 返回格式化后的结果
		return strResult;
	}

	/**
	 * 解析日期格式的字符串
	 * 
	 * @param argDateStr
	 *            日期格式的字符串
	 * @return 解析后的日期
	 */
	public static Date formatStringToDate(String argDateStr) {
		return formatStringToDate(argDateStr, null);
	}

	/**
	 * 解析日期格式的字符串
	 * 
	 * @param argDateStr
	 *            日期格式的字符串
	 * @param argFormat
	 *            格式
	 * @return 解析后的日期
	 */
	public static Date formatStringToDate(String argDateStr, String argFormat) {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}

		// format
		SimpleDateFormat sdfFormat = null;
		// 结果日期
		Date result = null;

		try {
			String strFormat = argFormat;
			if (StringUtil.isNullOrEmpty(strFormat)) {
				strFormat = DATE_FORMAT_YYYYMMDD;
				if (argDateStr.length() > 16) {
					strFormat = DATE_FORMAT_TIME_T;
				} else if (argDateStr.length() > 10) {
					strFormat = DATE_FORMAT_TIME_R;
				}
			}
			sdfFormat = new SimpleDateFormat(strFormat);
			result = sdfFormat.parse(argDateStr);
		} catch (Exception e) {
			result = null;
		} finally {
			sdfFormat = null;
		}

		// 返回解析后的日期
		return result;
	}

	/**
	 * 比较日期
	 * 
	 * @param argDate1
	 * @param argDate2
	 * @param argFormat
	 * @return
	 */
	public static int compare(Date argDate1, Date argDate2, String argFormat) {
		if (argDate1 == null && argDate2 == null) {
			return 0;
		}
		if (argDate1 == null) {
			return -1;
		}
		if (argDate2 == null) {
			return 1;
		}

		String strDate1 = formatDate(argDate1, argFormat);
		String strDate2 = formatDate(argDate2, argFormat);

		return strDate1.compareTo(strDate2);
	}

	/**
	 * timeStamp转换yyyy-MM-dd HH:SS
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static String convert(java.sql.Timestamp timeStamp) {
		if (timeStamp == null) {
			return "";
		}
		String ymd = String.valueOf(timeStamp.getYear() + 1900);

		String mm = String.valueOf(timeStamp.getMonth() + 1);
		if (mm.length() < 2) {
			mm = "0" + mm;
		}

		String dd = String.valueOf(timeStamp.getDate());
		if (dd.length() < 2) {
			dd = "0" + dd;
		}

		ymd = ymd + "-" + mm + "-" + dd;

		String hour = String.valueOf(timeStamp.getHours());
		if (hour.length() < 2) {
			hour = "0" + hour;
		}

		String minute = String.valueOf(timeStamp.getMinutes());
		if (minute.length() < 2) {
			minute = "0" + minute;
		}

		return ymd + " " + hour + ":" + minute;
	}

	/**
	 * 取得前一天 getPreDate
	 * 
	 * @param now
	 *            当前日期 yyyy-MM-dd
	 * @return yyyy-MM-dd String
	 * @exception
	 * @since 1.0.0
	 */
	public static String getPreDate(String now) {
		String preDate = "";
		Date tmp = formatStringToDate(now, DATE_FORMAT_YYYYMMDD);
		Calendar cal = Calendar.getInstance();
		cal.setTime(tmp);
		// 取得前一天
		cal.roll(Calendar.DAY_OF_YEAR, -1);
		preDate = formatDate(cal.getTime(), DATE_FORMAT_YYYYMMDD);
		return preDate;
	}

	/**
	 * 获取当天0点0分0秒的毫秒数
	 * 
	 * @return
	 */
	public static long getBeginMillisecond() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return (calendar.getTimeInMillis());
	}

	/**
	 * 获取当天23点59分59秒的毫秒数
	 * 
	 * @return
	 */
	public static long getEndMillisecond() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, day);
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return (calendar.getTimeInMillis());
	}

	/**
	 * 将一个时间格式字符串转换为另一个时间格式字符串
	 * 
	 * @param argFormat1
	 *            日期格式1
	 * @param argDateStr
	 *            转换的字符串
	 * @param argFormat2
	 *            目标格式字符串
	 * @return
	 */
	public static String strFormatAnother(String argFormat1, String argDateStr,
			String argFormat2) {

		return DateUtil
				.formatDate(
						DateUtil.formatStringToDate(argDateStr, argFormat1),
						argFormat2);
	}
	
	/**
	 * 根据当前时间获取本周一日期
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, 2-weekday);
		return cal.getTime();
	}
	
	/**
	 * 根据当前时间获取本周日日期
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, 8-weekday);
		return cal.getTime();
	}
	
	/**
	 * 比较两个日期之间相差天数 
	 * @param fDate 第一个日期
	 * @param oDate 第二个日期
	 * @return
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {

	   if (null == fDate || null == oDate) {

           return -1;

       }

       long intervalMilli = oDate.getTime() - fDate.getTime();

       return (int) (intervalMilli / (24 * 60 * 60 * 1000));

	}
	
	/**
	 * 日期相加
	 * @param date 日期
	 * @param mins 分钟
	 * @return
	 */
	public static Date addMins(Date date,int mins){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, mins);
		return cal.getTime();
	}
	
	
	/**
	 * 比较两个日期之间相差分钟数 
	 * @param firstDate 第一个开始日期
	 * @param secondDate 第二个结束日期
	 * @return
	 */
	public static int betweenMins(Date firstDate,int betweenMins,Date secondDate){
		
		if(null == firstDate || null ==secondDate){return -1;}
		
		Date endDate = addMins(firstDate,betweenMins);
		long intervalMilli = endDate.getTime() - secondDate.getTime();

	    return (int) (intervalMilli / (60 * 1000));
	}
	
}
