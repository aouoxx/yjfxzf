package com.supconit.kqfx.web.device.daos.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.device.daos.DeviceDataDao;
import com.supconit.kqfx.web.device.entities.DeviceData;

import hc.orm.AbstractDaoSupport;

@Repository("KQFX_DEVICEDATA_DAO")
public class DeviceDataDaoImpl extends AbstractDaoSupport implements DeviceDataDao{

	private final static String NAMESPACE=DeviceData.class.getName();
	
	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public List<DeviceData> getList(DeviceData condition) {
//		List<DeviceData> deviceDatas = selectList("getList",condition);
//		return deviceDatas;
		return null;
	}

	@Override
	public void insert(List<DeviceData> deviceDatas){
		insert("batchInsert", deviceDatas);
	}

	@Override
	public int countNomarlStatues(DeviceData data) {
		return selectOne("countNomarlStatues", data);
	}

	@Override
	public String getDeviceKey(String deviceId) {
		return selectOne("getDeviceKey", deviceId);
	}

	@Override
	public void insert(DeviceData deviceData) {
		insert("insert", deviceData);
		
	}

}
