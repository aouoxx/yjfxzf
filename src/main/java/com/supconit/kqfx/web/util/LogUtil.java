package com.supconit.kqfx.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.apache.log4j.Logger;

/**
 * 日志输出用共通方法
 */
public class LogUtil {

	/**
	 * 输出用logger
	 */
	// static Logger logger = Logger.getLogger(LogUtil.class.getName());
	private static Logger logger = LoggerFactory.getLogger(LogUtil.class);

	/**
	 * 输出Debug日志
	 * 
	 * @param message
	 *            信息
	 */
	public static void debug(String message) {
		try {
			logger.debug(getThrowableLineString(new LogThrowableInfo())
					+ "  - " + message);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	/**
	 * 输出信息日志
	 * 
	 * @param message
	 *            信息
	 */
	public static void info(String message) {
		try {
			logger.info(getThrowableLineString(new LogThrowableInfo()) + "  - "
					+ message);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	/**
	 * 输出trace日志
	 * 
	 * @param message
	 *            信息
	 */
	public static void trace(String message) {
		try {
			logger.trace(getThrowableLineString(new LogThrowableInfo())
					+ "  - " + message);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	/**
	 * 输出错误日志
	 * 
	 * @param message
	 *            信息
	 */
	public static void error(String message) {
		try {
			logger.error(getThrowableLineString(new LogThrowableInfo())
					+ "  - " + message);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	/**
	 * 输出错误日志
	 * 
	 * @param throwable
	 *            异常
	 */
	public static void error(Throwable throwable) {
		logger.error(throwable.getMessage());
	}

	/**
	 * 取得异常信息的文本内容
	 * 
	 * @return 异常信息
	 */
	public static String getThrowableLineString(LogThrowableInfo t) {
		String[] lines = t.getThrowableStrRep();
		String traceLine = "";

		if (lines != null && lines.length > 3) {
			traceLine = lines[3];
		}

		if (traceLine.startsWith("\tat ")) {
			traceLine = traceLine.substring(4);
		}

		return traceLine;
	}
}
