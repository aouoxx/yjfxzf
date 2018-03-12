package com.supconit.kqfx.web.fxzf.qbb.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.supconit.honeycomb.base.context.SpringContextHolder;
import com.supconit.kqfx.web.fxzf.avro.qbbItemInfo;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardInfo;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.qbb.entities.QbbBfxx;


public class RestoreQbb extends Thread {
	
	private Long time;
	private List<QbbBfxx>  qbbBfxxs;
	
	
	public RestoreQbb(Long time,List<QbbBfxx> qbbBfxxs) {
		super();
		this.time = time;
		this.qbbBfxxs = qbbBfxxs;
	}

	@Override
	public void run() {
		try {
			if (null != time && 0 != time) {
				RestoreQbb.sleep(time * 60 * 1000);
			}
			QbbBfxxService qbbBfxxService = SpringContextHolder.getBean(QbbBfxxService.class);
			WriteRedisService writeRedisService = SpringContextHolder.getBean(WriteRedisService.class);
			Date date = new Date();
			if(!CollectionUtils.isEmpty(qbbBfxxs)){
				for (QbbBfxx qbbBfxx : qbbBfxxs) {
					qbbBfxx.setPublishTime(date);
					qbbBfxxService.insert(qbbBfxx);
					redis_BoardInfo boardInfo = new redis_BoardInfo();
					List<qbbItemInfo> itemList = new ArrayList<qbbItemInfo>();
					itemList.add(QbbBfxx.translateToQbbItem(qbbBfxx));
					boardInfo.setSQbbName(qbbBfxx.getLocation());
					boardInfo.setSQbbItems(itemList);
					writeRedisService.WriteBoardInfo(boardInfo,qbbBfxx.getLocation());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	
}
