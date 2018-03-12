package com.supconit.kqfx.web.util;



/**
 * 系统常量类
 * @author cjm
 *
 */
public class SysConstants {
	
	/**
	 * 超限率大于(编码) 单位%
	 */
	public  final static String  OVERLOAD_PERCENT ="OVERLOAD_PERCENT";
	
	/**
	 * 总重大于(编码) 单位t
	 */
	public  final static String OVERLOAD_WEIGHT ="OVERLOAD_WEIGHT"; 
	
	/**
	 * 长度大于编码 单位m
	 */
	public  final static String  LENGTH ="LENGTH";
	
	/**
	 * 宽度大于编码 单位m
	 */
	public  final static String WIDTH ="WIDTH"; 
	
	/**
	 * 高度大于编码 单位m
	 */
	public  final static String HEIGHT ="HEIGHT";
	/**
	 * 再次告警时间间隔
	 */
	public final static String WARN_AGAIN_TIME = "WARN_AGAIN_TIME";
	/**
	 * 情报板停留时间  单位S
	 */
	public  final static String QBB_STAY_TIME ="QBB_STAY_TIME";
	
	/**
	 * 加入黑名单超限次数  单位次
	 */
	public  final static String BLACK_LIST_OVERLOADS ="BLACK_LIST_OVERLOADS";
	
	/**
	 * 兰亭到绍兴默认情报板信息
	 */
	public final static String LT_SX_QBB_XXNR = "LT_SX_QBB_XXNR";
	public final static String LT_SX_QBB_XXNR2 = "LT_SX_QBB_XXNR2";

	/**
	 * 兰亭往诸暨默认情报板信息
	 */
	public final static String LT_ZJ_QBB_XXNR = "LT_ZJ_QBB_XXNR";
	public final static String LT_ZJ_QBB_XXNR2 = "LT_ZJ_QBB_XXNR2";

	/**
	 * 最后一次手动发布的兰亭到绍兴情报板信息
	 */
	public final static String LT_SX_QBB_XXNR_LAST = "LT_SX_QBB_XXNR_LAST";
	public final static String LT_SX_QBB_XXNR_LAST2 = "LT_SX_QBB_XXNR_LAST2";

	/**
	 * 最后一次手动发布的兰亭往诸暨情报板信息
	 */
	public final static String LT_ZJ_QBB_XXNR_LAST = "LT_ZJ_QBB_XXNR_LAST";
	public final static String LT_ZJ_QBB_XXNR_LAST2 = "LT_ZJ_QBB_XXNR_LAST2";

	/**
	 * 网页告警模板
	 */
	public final static String WEB_WARN_TEMPLET = "WEB_WARN_TEMPLET";
	

	/**
	 * Redis中保存系统之间的交互的变量
	 */
	public final static String SYSTEM_PARAMETER="SYSTEM_PARAMETER"; 
	
	/**
	 * 称重下浮百分比
	 */
	public final static String DOWN_FLOW="DOWN_FLOW";
	
	public final static String STATIONDEVICE_01="STATIONDEVICE_01";
	public final static String STATIONDEVICE_02="STATIONDEVICE_02";
	
	/**
	 * Reids中保存的字段
	 */
	/**
	 * 用于设置redis中的时间
	 */
	public final static String PARAMETER1="sSystemTime";
	public final static String PARAMETER2="parameter2";
	public final static String PARAMETER3="parameter3";
	public final static String PARAMETER4="parameter4";
	public final static String PARAMETER5="parameter5";
	
	
}
