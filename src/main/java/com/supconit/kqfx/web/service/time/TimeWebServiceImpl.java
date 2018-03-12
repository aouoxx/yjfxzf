package com.supconit.kqfx.web.service.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tempuri.dag_xsd.Service_wsdl.ServiceLocator;

import org.tempuri.dag_xsd.Service_wsdl.ServiceStub;

import com.supconit.kqfx.web.fxzf.avro.redis_ConfigInfo;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.warn.daos.ConfigDao;
import com.supconit.kqfx.web.fxzf.warn.entities.Config;
import com.supconit.kqfx.web.util.SysConstants;
import com.supconit.kqfx.web.util.XmlAnalysisUtil;

/**
 * 定时请求webservice来获取对应的时间信息
 * @author dell
 *
 */
@Component("TIME_WEB_SERVICE")
public class TimeWebServiceImpl {
	
	private static final Logger logger = LoggerFactory
			.getLogger(TimeWebServiceImpl.class);
	
	@Autowired
	private WriteRedisService writeRedisService;
	@Autowired
	private ConfigDao configDao;
	@Value("${WebService.time}")
	private String url;
	
	public void synSystemTime(){
	try {
		String key = SysConstants.PARAMETER1;
		String time = url;
		
		ServiceLocator serviceLocator = new ServiceLocator();
		java.net.URL wsdl= new java.net.URL(url);
		ServiceStub serviceStub = new ServiceStub(wsdl,serviceLocator);
		
		/**
		 * 调用webservice的接口
		 */
		String xmlResult = serviceStub.synTime();
		time = XmlAnalysisUtil.getMesssage(xmlResult);
		String flag = XmlAnalysisUtil.getCode(xmlResult);
		//<!—CODE为非0时，MESSAGE 字段为失败的原因，CODE为0时为空-->
		if(flag.equals("0")){
			time = XmlAnalysisUtil.getTime(xmlResult);
			logger.info("===接口获取的时间："+time+"===");
			writeRedisService.writeSynTime(key, time+"000");
			Config config=configDao.getByCode(SysConstants.DOWN_FLOW);
			redis_ConfigInfo configInfo = new redis_ConfigInfo();
			if(config!=null){
				configInfo.setDOWNFLOW(String.valueOf(100-config.getValue()));
			}
			configInfo.setSSystemtime(time+"000");
			configInfo.setParameter2("0");
			configInfo.setParameter3("0");
			configInfo.setParameter4("0");
			configInfo.setParameter5("0");
			writeRedisService.WriteKeyToRedis(SysConstants.SYSTEM_PARAMETER,configInfo );
			logger.info("-----------同步时间接口调用成功--------------");
		}else{
			logger.info("-----------同步时间接口调用失败--------------");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
}
