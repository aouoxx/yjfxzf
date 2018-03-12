/**
 * 
 */
package com.supconit.kqfx.web.util;

/**
 * 常量类
 * 
 * @author zongkai
 * 
 */
public class Constants {

	/**
	 * 删除标志：未删除
	 */
	public final static String DELFLG_NORMAL = "0";

	/**
	 * 删除标志：已删除
	 */
	public final static String DELFLG_DELETED = "1";

	/**
	 * 性别：男
	 */
	public final static String MALE = "0";

	/**
	 * 性别：女
	 */
	public final static String FEMALE = "1";

	/**
	 * 登录用户信息保存在session中的Key
	 */
	public final static String SESSIONKEY_USER = "LOGIN_USER";

	/**
	 * 当前Session
	 */
	public final static String CURRENT_SESSION = "CURRENT_SESSION";

	/**
	 * 当前DN
	 */
	public final static String CURRENT_USER = "CURRENT_USER";

	/**
	 * 下拉框用（全部）
	 */
	public final static String ALL = "--全部--";

	/**
	 * 文件上传路径
	 */
	public final static String FILE_BASE_PATH = "upload";

	/**
	 * 路径分隔符
	 */
	public final static String PATH_SEPARATOR = System
			.getProperty("file.separator");

	/**
	 * 换行符
	 */
	public final static String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * 文件的类型：图片
	 */
	public final static String FILE_TYPE_PIC = "FxzfImage/x-png,FxzfImage/gif,FxzfImage/jpeg,FxzfImage/bmp,FxzfImage/png,FxzfImage/pjpeg";

	/**
	 * 文件的类型：text
	 */
	public final static String FILE_TYPE_TXT = "text/plain";

	/**
	 * CHECKBOX用 全部
	 */
	public final static String CHECKBOX_ALL = "全部";

	/**
	 * 超级角色（系统唯一）
	 */
	public final static String SUPER_ROLE = "001";

	/**
	 * 超级用户（系统唯一）
	 */
	public final static String SUPER__USER = "admin";
	/**
	 * excel单页最大记录条数
	 */
	public final static int MAX_SHEET_COUNT = 65530;

	/**
	 * 空字符串
	 */
	public final static String EMPTY_STRING = "";
	//DWR推送数据的时间间隔,单位（MS）
	public final static int PUSH_DATA_TIME_INTERVAL=10000;
	

	
	/**
	 * 上传声音文件路径
	 */
	public final static String SOUND = "sound";
	
	/**
	 * 机构分类(当值为-1包含全部分类)
	 */
	public final static String JGDJ_ALL = "-1";
	
	/**
	 * 机构分类:1:局级
	 */
	public final static String JGDJ_JJ = "1";
	
}
