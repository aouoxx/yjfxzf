package com.supconit.kqfx.web.device.daos.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.device.daos.DeviceInfoDao;
import com.supconit.kqfx.web.device.entities.DeviceInfo;

@Repository("KQFX_DEVICEINFO_DAO")
public class DeviceInfoDaoImpl extends AbstractBasicDaoImpl<DeviceInfo, String>  implements DeviceInfoDao {

	private final static String NAMESPACE=DeviceInfo.class.getName();
	
	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public DeviceInfo getDeviceInfo(DeviceInfo device) {
		return selectOne("getOneDevice", device);
	}

	@Override
	public List<DeviceInfo> getStationAllDeviceInfo(DeviceInfo condition) {
		return selectList("getStationAllDeviceInfo", condition);
	}

}
