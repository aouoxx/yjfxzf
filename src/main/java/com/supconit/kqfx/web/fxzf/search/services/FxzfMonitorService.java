package com.supconit.kqfx.web.fxzf.search.services;

import hc.orm.BasicOrmService;

import java.util.Date;
import java.util.List;

import com.supconit.kqfx.web.fxzf.search.entities.FxzfMonitor;

public interface FxzfMonitorService extends BasicOrmService<FxzfMonitor,String>{

	/**
	 * 获取该用户权限关联的违法记录
	 * @param monitor
	 * @return
	 */
	List<FxzfMonitor> getByTotalStation(FxzfMonitor monitor);
	
	/**
	 * 获取该治超站最近的5条记录
	 * @param staion
	 * @return
	 */
	List<FxzfMonitor> getByStation(String staion);
	/**
	 * 获取该志超站的违法条数，最大是5条，
	 * @param monitor
	 * @return
	 */
	int getStationCount(FxzfMonitor monitor);
	
	/**
	 * 获取治超站中对应记录中最小时间
	 * @param monitor
	 * @return
	 */
	Date getMinTime(FxzfMonitor monitor);
	
	/**
	 * 通过治超站的最小时间和治超站编号，获取该记录
	 * @param monitor
	 * @return
	 */
	FxzfMonitor getByTimeAndStation(FxzfMonitor monitor);
	
	/**
	 * 通过时间删除和治超站编码删除对应的记录
	 * @param monitor
	 */
	void deleteByTime(FxzfMonitor monitor);
	
	/**
	 * 通过记录ID删除该记录
	 * @param id
	 */
	void deleteByMonitorId(String id);

}
