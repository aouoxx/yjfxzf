package com.supconit.kqfx.web.device.services.Impl;

import java.util.List;

import hc.orm.AbstractBasicOrmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.device.daos.DeviceInfoDao;
import com.supconit.kqfx.web.device.entities.DeviceInfo;
import com.supconit.kqfx.web.device.services.DeviceInfoService;

@Service("KQFXZF_DEVICE_SERVICE")
public class DeviceInfoServiceImpl extends AbstractBasicOrmService<DeviceInfo, String>  implements DeviceInfoService{
	
	@Autowired
	private DeviceInfoDao deviceInfoDao;

	@Override
	public DeviceInfo getById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(DeviceInfo entity) {
		
	}

	@Override
	public void update(DeviceInfo entity) {
		
	}

	@Override
	public void delete(DeviceInfo entity) {
		
	}

	@Override
	public DeviceInfo getDeviceInfo(DeviceInfo device) {
		return deviceInfoDao.getDeviceInfo(device);
	}

	@Override
	public List<DeviceInfo> getStationAllDeviceInfo(DeviceInfo condition) {
		
		return deviceInfoDao.getStationAllDeviceInfo(condition);
	}

	

}
