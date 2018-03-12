package com.supconit.kqfx.web.fxzf.avro.redis;

public interface ChargeRedisService {
	
	//判断是否存在该key的值
	boolean chargeKey(String key);

	/**
	 * 判断设备列表是否为空
	 * @param key
	 * @return
	 */
	public boolean chargeDeviceKey(String key);
	
	public String readSynKey(String key);
}
