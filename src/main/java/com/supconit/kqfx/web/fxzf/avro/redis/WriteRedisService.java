package com.supconit.kqfx.web.fxzf.avro.redis;

import com.supconit.kqfx.web.fxzf.avro.redis_BoardData;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardInfo;
import com.supconit.kqfx.web.fxzf.avro.redis_ConfigInfo;

/**
 * 写Redis的接口
 * @author dell
 *
 */
public interface WriteRedisService {
	
	/**
	 * 根据车牌、车牌颜色、标志位格式化成Rediskey
	 * @param License 车牌
	 * @param LicenseColor 车牌颜色
	 * @param flag 0为黑名单 1为白名单 默认为黑名单
	 * @return String
	 */
	String PatternListToRedis(String License, String LicenseColor, Integer flag);
	
	/**
	 * 初始化名单至redis
	 * @param flag 0为黑名单 1为白名单 默认为黑名单
	 */
	void RedisInitList(Integer flag);
	
	/**
	 * 根据车牌、车牌颜色判断是否为黑白名单
	 * @param License 车牌
	 * @param LicenseColor 车牌颜色
	 * @param flag 0为黑名单 1为白名单 默认为黑名单
	 * @return 存在为true,不存在为false
	 */
	Boolean RedisExistsList(String License, String LicenseColor, Integer flag);
	
	/**
	 * 新增黑、白名单至Redis
	 * @param id 黑白名单ID,存为redis中value值
	 * @param License 车牌
	 * @param LicenseColor 车牌颜色
	 * @param flag 0为黑名单 1为白名单 默认为黑名单
	 */
	void AddListToRedis(String id, String License, String LicenseColor, Integer flag);
	
	/**
	 * 删除黑、白名单至Redis
	 * @param id 黑白名单ID,存为redis中value值
	 * @param License 车牌
	 * @param LicenseColor 车牌颜色
	 * @param flag 0为黑名单 1为白名单 默认为黑名单
	 */
	void DeleteListToRedis(String id, String License, String LicenseColor, Integer flag);
	
	/**
	 * 情报板信息发布接口
	 * @param boardInfo 情报板信息,为播放列表List<qbbItemInfo>
	 * @param detectStation 治超点数据字典
	 */
	void WriteBoardInfo(redis_BoardInfo boardInfo, String detectStation);
	
	/**
	 * 向Reids中写入某个key值
	 * @param key
	 * @param configInfo 向redis中写的变量集合编码
	 */
	void WriteKeyToRedis(String key, redis_ConfigInfo configInfo);
	
	/**
	 * 根据治超点写入实时情报板内容
	 * @param detectStation 治超点数据字典
	 * @return
	 */
	void WriteBoardData(redis_BoardData boardData, String detectStation);
	
	/**
	 * 将时间设置到redis中
	 */
	void writeSynTime(String key, String time);
	
	/**
	 * 将对应治超站的设备信息写入到列表中
	 */
	void writeDeviceIdByStation(String key, String deviceId);
}
