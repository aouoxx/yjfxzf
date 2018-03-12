package com.supconit.kqfx.web.device.daos;

import java.util.List;

import com.supconit.kqfx.web.device.entities.DeviceData;


public interface DeviceDataDao {
	
	/**
	 * 获取列表数据
	 * @param condition
	 * @return
	 */
	public List<DeviceData> getList(DeviceData condition);

	/**
	 * 插入列表数据
	 * @param deviceDatas
	 */
	public void insert(List<DeviceData> deviceDatas);
	
	
	/**
	 * 插入单个设备的状态信息
	 * @param deviceData
	 */
	public void insert(DeviceData deviceData);
	
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
}
