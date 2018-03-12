package com.supconit.kqfx.web.service.device;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.math.Fraction;
import org.apache.cxf.binding.soap.interceptor.Soap11FaultInInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supconit.kqfx.web.device.entities.DeviceData;
import com.supconit.kqfx.web.device.entities.Redis_DeviceData;
import com.supconit.kqfx.web.device.services.DeviceDataService;
import com.supconit.kqfx.web.fxzf.avro.redis.ReadRedisService;
import com.supconit.kqfx.web.service.data.DataTransportThread;

@Component
public class DeviceDataThread implements Runnable{

	@Autowired
	private DeviceDataService deviceDataService;
	
	@Autowired
	private ReadRedisService readRedisService;
	
	private static final Logger logger = LoggerFactory.getLogger(DeviceDataThread.class);
	
	private final static String DEVICE_DATA="DEVICE_INFO"; 
	SimpleDateFormat format = new SimpleDateFormat("YYYYMMDDHHMMDD");
	
	@Override
	public void run() {
		/**
		 * Get the device states and insert into T_DEVICE_DATA
		 */
		while (true) {
			logger.info("=========获取设备状态信息==========");
			
			List<Redis_DeviceData> deviceDatas = readRedisService.ReadDeviceData(DEVICE_DATA);
			for(Redis_DeviceData redis_DeviceData:deviceDatas){
				DeviceData deviceData = new DeviceData();
				/**
				 * Transfer the Redis_DeviceData into DeviceData
				 */
				try {
					deviceData.setCheckTime(format.parse(String.valueOf(redis_DeviceData.getCheckTime())));
					deviceData.setIndexCode(String.valueOf(redis_DeviceData.getIndexCode()));
					deviceData.setDeviceKey(String.valueOf(redis_DeviceData.getDeviceKey()));
					deviceData.setDeviceState(String.valueOf(redis_DeviceData.getDeviceState()));
					deviceData.setExceptionReason(String.valueOf(redis_DeviceData.getExceptionReason()));
					
					deviceDataService.insert(deviceData);
				} catch (Exception e) {
					logger.error("时间格式转换出错");
				}
				
				
			}
		}
		
	}

	
}
