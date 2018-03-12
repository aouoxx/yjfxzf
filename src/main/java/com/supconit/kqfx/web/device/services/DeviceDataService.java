package com.supconit.kqfx.web.device.services;

import com.supconit.kqfx.web.device.entities.DeviceData;

/**
 * @author gs
 */
public interface DeviceDataService  {

	
	/**
	 * 获取某个设备在某个时间的正常状态的次数
	 * @param data
	 * @return
	 */
	public int countNomarlStatues(DeviceData data);
	
	/**
	 * 通过设备的ID获取对应的设备秘钥
	 */
	public String getDeviceKey(String deviceId);
	/**
	 * 插入单个设备的状态信息
	 * @param deviceData
	 */
	public void insert(DeviceData deviceData);
}
