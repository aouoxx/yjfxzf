package com.supconit.kqfx.web.service.device;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tempuri.dag_xsd.Service_wsdl.ServiceLocator;

import org.tempuri.dag_xsd.Service_wsdl.ServiceStub;

import com.supconit.kqfx.web.device.entities.DeviceData;
import com.supconit.kqfx.web.device.entities.DeviceInfo;
import com.supconit.kqfx.web.device.services.DeviceDataService;
import com.supconit.kqfx.web.device.services.DeviceInfoService;
import com.supconit.kqfx.web.util.XmlAnalysisUtil;

@Component("DEVICE_WEB_SERVICE")
public class DeviceWebServiceImpl {
	
	private static final Logger logger =  LoggerFactory.getLogger(DeviceWebServiceImpl.class);
	
	@Value("${WebService.device}")
	private String url;
	
	@Autowired
	private DeviceInfoService deviceInfoService;
	
	@Autowired 
	private DeviceDataService deviceDataService;
	
	public void tranferDeviceState(){
		try {
		/**
		 * 获取所有的设备ID
		 * 遍历其中的一个设备，通过其对应的ID(编号信息)
		 * 从DeviceData表中获取对应的数据信息
		 */
		DeviceInfo condition = new DeviceInfo();
		List<DeviceInfo> devices = deviceInfoService.getStationAllDeviceInfo(condition);
		Date nowTime = new Date();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		
		ServiceLocator serviceLocator = new ServiceLocator();
		java.net.URL wsdl= new java.net.URL(url);
		ServiceStub serviceStub = new ServiceStub(wsdl,serviceLocator);
		
		for(DeviceInfo device : devices){
			DeviceData deviceData = new DeviceData();
			int secound = device.getChargeCycle()*-1;
			int thresHold=device.getAlarmThreshold();
			//设备编号
			deviceData.setIndexCode(device.getId());
			//传送给上级单位的时间
			deviceData.setTransferTime(format.format(nowTime));
			
			deviceData.setNowTime(nowTime);
			calendar.setTime(nowTime);
			calendar.add(Calendar.SECOND, secound);
			deviceData.setBeforeTime(calendar.getTime());
			/**
			 * 正常的次数大于异常的次数说明是正常的
			 * 正常的次数小于于异常的次数说明发生异常的
			 * 设备状态
			 * 0：异常
			 * 1：正常
			 * 2：未使用
			 * 3：未知
			 */
			if(deviceDataService.countNomarlStatues(deviceData)>thresHold){
				deviceData.setDeviceKey(deviceDataService.getDeviceKey(deviceData.getIndexCode()));
				deviceData.setDeviceState(1+"");
				deviceData.setExceptionReason("无");
			}else{
				/**
				 * 该时间段内没有数据传送说明连接丢失
				 */
				if(deviceDataService.countNomarlStatues(deviceData)==0){
					deviceData.setDeviceKey(deviceDataService.getDeviceKey(deviceData.getIndexCode()));
					deviceData.setDeviceState(3+"");
					deviceData.setExceptionReason("连接丢失");
				}else{
					/**
					 * 发生异常
					 */
					deviceData.setDeviceKey(deviceDataService.getDeviceKey(deviceData.getIndexCode()));
					deviceData.setDeviceState(0+"");
					/**
					 * 获取最近的异常信息
					 */
				}
			}
			/**
			 * 打印被传送的设备状态信息
			 */
			logger.info("============将要传送的设备信息============");
			logger.info("设备编号："+deviceData.getIndexCode());
			logger.info("设备秘钥："+deviceData.getDeviceKey());
			logger.info("设备状态："+deviceData.getDeviceState());
			logger.info("异常信息："+deviceData.getExceptionReason());
			logger.info("报送时间："+deviceData.getTransferTime());
			
			/**
			 * 调用web数据接口
			 */
			String deviceState = deviceDataToString(deviceData);
			
			String xmlResult=null;
			//xmlResult = serviceStub.sendDeviceState(deviceState);
			String flag = XmlAnalysisUtil.getCode(xmlResult);
			if(flag.equals("0")){
				logger.info("============设备状态传输成功==========");
			}
		}
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("============设备状态传输失败=========");
		}
	}
	
	public String deviceDataToString(DeviceData deviceData){
		String parameter = "";
		parameter = "<?xml version=\"1.0\" encoding=\" UTF-8 \" standalone=\"yes\" ?>"
				   +"<ROOT>"
				   +"<DeviceKey>"+deviceData.getDeviceKey()+"</DeviceKey>"
				   +"<IndexCode>"+deviceData.getIndexCode()+"</IndexCode>"
				   +"<DeviceState>"+deviceData.getDeviceState()+"<DeviceState>"
				   +"<ExceptionReason>"+deviceData.getExceptionReason()+"<ExceptionReason>"
				   +"<CheckTime>"+deviceData.getTransferTime()+"</CheckTime>"
				   +"</ROOT> ";
		return parameter;
	}
}
