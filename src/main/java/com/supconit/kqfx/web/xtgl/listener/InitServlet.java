package com.supconit.kqfx.web.xtgl.listener;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.supconit.honeycomb.base.context.SpringContextHolder;
import com.supconit.kqfx.web.service.data.DataTransportThread;
import com.supconit.kqfx.web.service.device.DeviceDataThread;
import com.supconit.kqfx.web.util.PropertiesUtil;

/**
 * 初始化各种数据接收和传输，以及设备状态上传线程
 * @author gs
 * @CreateTime 上午8:25:14
 */
@Controller("initservlet")
public class InitServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	@Autowired
	private DataTransportThread dataTransportThread;
	@Autowired
	private DeviceDataThread deviceDataThread;
	
	private String dataReceive;
	private String dataTransport;
	private String deviceFlag;

	private static final Logger logger = LoggerFactory.getLogger(InitServlet.class);
	
	
	public void init(){
		dataReceive=PropertiesUtil.getByCode("data.receive");
		dataTransport=PropertiesUtil.getByCode("data.transport");
		deviceFlag=PropertiesUtil.getByCode("device.status");
		logger.info("=========启动应用的各种线程============");
		/**
		 * 数据接收线程
		 */
		if(dataReceive.equals("Y")){
			new Thread(new InitReviceData()).start(); 
		}
		 
		 /**
		  * 数据上传线程
		  */
		if(dataTransport.equals("Y")){
			dataTransportThread = SpringContextHolder.getBean(DataTransportThread.class);
			new Thread(dataTransportThread).start();
		}
		 /**
		  * 读取设备状态线程
		  */
		if(deviceFlag.equals("Y")){
			deviceDataThread=SpringContextHolder.getBean(DeviceDataThread.class);
			 new Thread(deviceDataThread).start();
		 }
		 logger.info("=========应用的各种线程启动完成============");
	}
}

