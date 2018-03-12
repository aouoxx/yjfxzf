package com.supconit.kqfx.web.device.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.device.daos.DeviceDataDao;
import com.supconit.kqfx.web.device.entities.DeviceData;
import com.supconit.kqfx.web.device.services.DeviceDataService;

@Service("KQFX_DEVICEDATA_SERVICE")
public class DeviceDataServiceImpl  implements DeviceDataService{

	@Autowired
	private DeviceDataDao deviceDataDao;
	
	@Override
	public int countNomarlStatues(DeviceData data) {
		return deviceDataDao.countNomarlStatues(data);
	}

	@Override
	public String getDeviceKey(String deviceId) {
		return deviceDataDao.getDeviceKey(deviceId);
	}

	@Override
	public void insert(DeviceData deviceData) {
		deviceDataDao.insert(deviceData);
		
	}

	
}
