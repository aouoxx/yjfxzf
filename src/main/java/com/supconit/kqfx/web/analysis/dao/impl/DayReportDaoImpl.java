package com.supconit.kqfx.web.analysis.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.analysis.dao.DayReportDao;
import com.supconit.kqfx.web.analysis.entities.DayReport;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

@Repository("analysis_dayreport_dao")
public class DayReportDaoImpl extends AbstractBasicDaoImpl<DayReport, String>
		implements DayReportDao {
	
	private static final String NAMESPACE = DayReport.class.getName();
	
	@Override
	protected String getNamespace() {
		
		return NAMESPACE;
	}

	@Override
	public List<DayReport> CountByCondition(DayReport condition) {
		
		return selectList("CountByCondition", condition);
	}

	@Override
	public List<DayReport> AnalysisCarFluxByCondition(DayReport condition) {
		
		return selectList("AnalysisCarFluxByCondition", condition);
	}

	@Override
	public Pageable<DayReport> findZczByPager(Pageable<DayReport> pager,
			DayReport condition) {
		// TODO Auto-generated method stub
		return findByPager(pager, "selectZczPager", "countZczPager", condition);
	}

	@Override
	public List<DayReport> AnalysisZczByCondition(DayReport condition) {
		// TODO Auto-generated method stub
		return selectList("AnalysisZczByCondition", condition);
	}

	@Override
	public Pageable<DayReport> findOverloadPunishByPager(
			Pageable<DayReport> pager, DayReport condition) {
		// TODO Auto-generated method stub
		return findByPager(pager, "selectOverLoadPager", "countOverLoadPager", condition);
	}

	@Override
	public List<DayReport> AnalysisOverLoadByCondition(DayReport condition) {
		// TODO Auto-generated method stub
		return selectList("AnalysisOverLoadByCondition", condition);
	}

	@Override
	public List<DayReport> CountDetectByCondition(DayReport condition) {
		
		return selectList("CountDetectByCondition", condition);
	}

	

}
