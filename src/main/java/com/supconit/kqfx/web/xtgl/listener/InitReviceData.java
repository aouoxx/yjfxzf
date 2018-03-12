package com.supconit.kqfx.web.xtgl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.supconit.honeycomb.base.context.SpringContextHolder;
import com.supconit.kqfx.web.fxzf.avro.redis.ReadRedisService;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.qbb.services.QbbBfxxService;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;

public class InitReviceData implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(InitReviceData.class);
	
	@Override
	public void run() {
		try {
			ReadRedisService readRedisService = ((ReadRedisService)SpringContextHolder.getBean(ReadRedisService.class));
			WriteRedisService writeRedisService = ((WriteRedisService)SpringContextHolder.getBean(WriteRedisService.class));
			QbbBfxxService qbbBfxxService = ((QbbBfxxService)SpringContextHolder.getBean(QbbBfxxService.class));
			ConfigService configService = (ConfigService)SpringContextHolder.getBean(ConfigService.class);
			writeRedisService.RedisInitList(0);//初始化黑名单
			writeRedisService.RedisInitList(1);//初始化白名单
			qbbBfxxService.initLastQbb();//初始化情报板内容
			readRedisService.ReceiveRealTimeData();//接收实时数据
			configService.initParameter();//初始化写入Reids中需要记录的变量
			logger.info("读取redis队列");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
