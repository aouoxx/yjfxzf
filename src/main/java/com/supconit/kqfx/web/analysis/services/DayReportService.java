package com.supconit.kqfx.web.analysis.services;

import hc.base.domains.Pageable;
import hc.orm.BasicOrmService;

import java.util.List;

import com.supconit.kqfx.web.analysis.entities.DayReport;

public interface DayReportService extends BasicOrmService<DayReport, String> {
	
	/**
	 * 根据查询条件获取统计结果
	 * @return
	 */
	List<DayReport> CountByCondition(DayReport condition);
	
	/**
	 * 统计车流量信息
	 * @param condition
	 * @return
	 */
	List<DayReport> AnalysisCarFluxByCondition(DayReport condition);
	
	/**
	 * 分页查询ZCZ按日期统计信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<DayReport> findZczByPager(Pageable<DayReport> pager, DayReport condition);
	
	/**
	 * 统计治超站信息
	 */
	List<DayReport> AnalysisZczByCondition(DayReport condition);
	
	/**
	 * 分页按违法程度进行统计
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<DayReport> findOverloadPunishByPager(Pageable<DayReport> pager, DayReport condition);
	
	/**
	 * 统计违法程度
	 * @return
	 */
	List<DayReport> AnalysisOverLoadByCondition(DayReport condition);
	
	/**
	 * 治超站日期统计
	 * @param condition
	 * @return
	 */
	List<DayReport> CountDetectByCondition(DayReport condition);

}
