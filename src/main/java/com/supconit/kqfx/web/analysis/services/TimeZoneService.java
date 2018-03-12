package com.supconit.kqfx.web.analysis.services;

import hc.base.domains.Pageable;
import hc.orm.BasicOrmService;

import java.util.List;

import com.supconit.kqfx.web.analysis.entities.TimeZone;

public interface TimeZoneService extends BasicOrmService<TimeZone, String> {
	
	
	/**
	 * 查询获取小时统计列表数据
	 * @return
	 */
	List<TimeZone> getByPartTime(TimeZone condition);
	
	/**
	 * 查询获取日期统计列表数据
	 * @return
	 */
	List<TimeZone> getByDateTime(TimeZone condition);
	
	/**
	 * 按小时分页获取表格列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<TimeZone> findByHourPager(Pageable<TimeZone> pager, TimeZone condition);
	
	/**
	 * 按日期分页获取表格列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<TimeZone> findByDayPager(Pageable<TimeZone> pager, TimeZone condition);
	
	/**
	 * 根据日期就平均值，获取每个日期对应的天数
	 * @param condition
	 * @return
	 */
	TimeZone getDateTimeCount(TimeZone condition);
	
	/**
	 * 根据小时求平均值，获取每个小时对应的数据
	 * @param condition
	 * @return
	 */
	TimeZone getPartTimeCount(TimeZone condition);

}
