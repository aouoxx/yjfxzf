package com.supconit.kqfx.web.service.data;

import hc.business.dic.services.DataDictionaryService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;




import org.tempuri.dag_xsd.Service_wsdl.ServiceLocator;

import org.tempuri.dag_xsd.Service_wsdl.ServiceStub;

import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.fxzf.search.services.FxzfSearchService;
import com.supconit.kqfx.web.fxzf.warn.entities.Config;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.SysConstants;

@Component("KQFX_DATA_TRASPORT")
public class DataTransportThread implements Runnable {
 
	private static final Logger logger = LoggerFactory.getLogger(DataTransportThread.class);
	
	@Autowired
	private FxzfSearchService fxzfSearchService;
	@Autowired
	private DataWebServiceImpl dataWebServiceImpl;
	@Autowired
	private DataDictionaryService dataDictionaryService;
	@Autowired
	private ConfigService configService;
	
	@Value("${WebService.data}")
	private String url;
	@Override
	public void run() {
		while (true){
			try{
				List<Fxzf> Fxzfs = fxzfSearchService.getFxzfToTransport();
				HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME",dataDictionaryService);
				if(Fxzfs.size()>0){
					/**
					 * 获取长宽高
					 */
					Double length = configService.getByCode(SysConstants.LENGTH).getValue();
					Double width = configService.getByCode(SysConstants.WIDTH).getValue();
					Double height = configService.getByCode(SysConstants.HEIGHT).getValue();
					
					ServiceLocator serviceLocator = new ServiceLocator();
					java.net.URL wsdl= new java.net.URL(url);
					ServiceStub serviceStub = new ServiceStub(wsdl,serviceLocator);
					
					for(Fxzf fxzf : Fxzfs){
						logger.info("================有数据需要被传送================");
						if(dataWebServiceImpl.tranferData(fxzf,serviceStub,stationMap,length,width,height)){
							fxzf.setTransport(1);
							fxzf.setTransportTime(new Date());
							fxzfSearchService.upDateFxzftransport(fxzf);
						}
					}
				}else{
					Thread.sleep(5000);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
}
