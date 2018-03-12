package com.supconit.kqfx.web.analysis.dao;

import hc.base.domains.Pageable;
import hc.orm.BasicDao;

import java.util.List;

import com.supconit.kqfx.web.analysis.entities.DayReport;

public interface DayReportDao extends BasicDao<DayReport, String> {

	List<DayReport> CountByCondition(DayReport condition);

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
	
	Pageable<DayReport> findOverloadPunishByPager(Pageable<DayReport> pager, DayReport condition);
	
	List<DayReport> AnalysisOverLoadByCondition(DayReport condition);

	List<DayReport> CountDetectByCondition(DayReport condition);
	
}
