package com.supconit.kqfx.web.service.device;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supconit.kqfx.web.device.entities.DeviceInfo;
import com.supconit.kqfx.web.device.services.DeviceInfoService;
import com.supconit.kqfx.web.fxzf.avro.redis.ChargeRedisService;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.util.SysConstants;

@Component("DEVICE_KEY_SET")
public class DeviceKeySetServiceImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceKeySetServiceImpl.class);
	private final static String direction01="K110+150";
	private final static String direction02="K109+800";
	
	@Autowired
	private DeviceInfoService deviceInfoService;
	@Autowired
	private ChargeRedisService chargeRedisService;
	@Autowired
	private WriteRedisService writeRedisService;
	
	
	public void setStationKey(){
		/**
		 * 首先判断治超站设备对应的KEY是否存在
		 * 如果不存在写入对应的KEY信息
		 * 如果存在进行下面的判断
		 * 先判断原来的key中的信息是否被读取
		 * 如果被读取重新设置信息
		 * 否则不在设置信息
		 */
		String stationDevice_01=SysConstants.STATIONDEVICE_01;
		String stationDevice_02=SysConstants.STATIONDEVICE_02;
		if(chargeRedisService.chargeKey(stationDevice_01)){
			if(chargeRedisService.chargeDeviceKey(stationDevice_01)){
				//获取所有station1中的key信息
				DeviceInfo deviceInfo = new DeviceInfo();
				deviceInfo.setDeviceDirection(direction01);
				List<DeviceInfo> devices = deviceInfoService.getStationAllDeviceInfo(deviceInfo);
				for(DeviceInfo device:devices){
					writeRedisService.writeDeviceIdByStation(stationDevice_01, device.getNo());
				}
			}else{
				logger.info("============治超站1设备编号未被读取==============");
			}
		}else{
			//设置所有station1中的key信息
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setDeviceDirection(direction01);
			List<DeviceInfo> devices = deviceInfoService.getStationAllDeviceInfo(deviceInfo);
			for(DeviceInfo device:devices){
				writeRedisService.writeDeviceIdByStation(stationDevice_01, device.getNo());
			}
		}
		if(chargeRedisService.chargeKey(stationDevice_02)){
			if(chargeRedisService.chargeDeviceKey(stationDevice_02)){
				//获取所有station2中的key信息
				DeviceInfo deviceInfo = new DeviceInfo();
				deviceInfo.setDeviceDirection(direction02);
				List<DeviceInfo> devices = deviceInfoService.getStationAllDeviceInfo(deviceInfo);
				for(DeviceInfo device:devices){
					writeRedisService.writeDeviceIdByStation(stationDevice_02, device.getNo());
				}
			}else{
				logger.info("============治超站2设备编号未被读取==============");
			}
		}else{
			//设置所有station2中的key信息
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setDeviceDirection(direction02);
			List<DeviceInfo> devices = deviceInfoService.getStationAllDeviceInfo(deviceInfo);
			for(DeviceInfo device:devices){
				writeRedisService.writeDeviceIdByStation(stationDevice_02, device.getNo());
			}
		}
	}
}
