package com.supconit.kqfx.web.device.services;

import java.util.List;

import com.supconit.kqfx.web.device.entities.DeviceInfo;

public interface DeviceInfoService {

	/**
	 * 获取单个设备的信息
	 * @param device
	 * @return
	 */
	DeviceInfo getDeviceInfo(DeviceInfo device);
	
	/**
	 * 获取某个治超站的所有设备的ID
	 */
	List<DeviceInfo> getStationAllDeviceInfo(DeviceInfo condition);
}
