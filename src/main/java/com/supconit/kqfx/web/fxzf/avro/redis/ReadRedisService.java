package com.supconit.kqfx.web.fxzf.avro.redis;

import java.util.List;

import com.supconit.kqfx.web.device.entities.Redis_DeviceData;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardData;
import com.supconit.kqfx.web.fxzf.avro.redis_FxzfInfo;

/**
 * 读取redis的接口
 * @author gs
 *
 */
public interface ReadRedisService {
	
	/**
	 * 根据治超点获取实时情报板内容
	 * @param detectStation 治超点数据字典
	 * @return
	 */
	redis_BoardData ReadBoardData(String detectStation);
	
	
	/**
	 * 获取实时非现执法过车数据
	 * @return
	 */
	List<redis_FxzfInfo> ReadFxzfInfo();
	
	/**
	 *获取实时设备状态信息
	 */
	List<Redis_DeviceData> ReadDeviceData(String key);
	
	/**
	 * 根据车牌、车牌颜色、标志位获取黑白名单ID
	 * @param License 车牌
	 * @param LicenseColor 车牌颜色
	 * @param flag 0为黑名单 1为白名单 默认为黑名单
	 * @return redis中value值
	 */
	String RedisValueList(String License, String LicenseColor, Integer flag);
	
	/**
	 * 实时数据接入
	 */
	void ReceiveRealTimeData();
	
}
