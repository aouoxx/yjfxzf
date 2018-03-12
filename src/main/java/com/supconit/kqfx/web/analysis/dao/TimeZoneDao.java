package com.supconit.kqfx.web.analysis.dao;

import java.util.List;

import com.supconit.kqfx.web.analysis.entities.TimeZone;

import hc.base.domains.Pageable;
import hc.orm.BasicDao;

public interface TimeZoneDao extends BasicDao<TimeZone, String> {
	
	/**
	 * 查询获取统计列表数据
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

	
	TimeZone getDateTimeCount(TimeZone condition);
	
	TimeZone getPartTimeCount(TimeZone condition);
}
