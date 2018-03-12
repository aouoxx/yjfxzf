package com.supconit.kqfx.web.analysis.services.impl;

import java.util.List;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicOrmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.analysis.dao.DayReportDao;
import com.supconit.kqfx.web.analysis.entities.DayReport;
import com.supconit.kqfx.web.analysis.services.DayReportService;

@Service("analysis_dayreport_service")
public class DayReportServiceImpl extends
		AbstractBasicOrmService<DayReport, String> implements DayReportService {
	
	@Autowired
	private DayReportDao  dayReportDao;
	
	@Override
	public DayReport getById(String id) {
		return this.dayReportDao.getById(id);
	}


	@Override
	public void insert(DayReport entity) {
		this.dayReportDao.insert(entity);
	}

	@Override
	public void update(DayReport entity) {
		this.dayReportDao.update(entity);
	}

	@Override
	public void delete(DayReport entity) {
		this.dayReportDao.delete(entity);
	}


	@Override
	public List<DayReport> CountByCondition(DayReport condition) {
		
		return this.dayReportDao.CountByCondition(condition);
	}


	@Override
	public List<DayReport> AnalysisCarFluxByCondition(DayReport condition) {
		
		return this.dayReportDao.AnalysisCarFluxByCondition(condition);
	}


	@Override
	public Pageable<DayReport> findZczByPager(Pageable<DayReport> pager,
			DayReport condition) {
		// TODO Auto-generated method stub
		return dayReportDao.findZczByPager(pager, condition);
	}


	@Override
	public List<DayReport> AnalysisZczByCondition(DayReport condition) {
		// TODO Auto-generated method stub
		return dayReportDao.AnalysisZczByCondition(condition);
	}


	@Override
	public Pageable<DayReport> findOverloadPunishByPager(
			Pageable<DayReport> pager, DayReport condition) {
		// TODO Auto-generated method stub
		return dayReportDao.findOverloadPunishByPager(pager, condition);
	}


	@Override
	public List<DayReport> AnalysisOverLoadByCondition(DayReport condition) {
		// TODO Auto-generated method stub
		return dayReportDao.AnalysisOverLoadByCondition(condition);
	}


	@Override
	public List<DayReport> CountDetectByCondition(DayReport condition) {
		
		return this.dayReportDao.CountDetectByCondition(condition);
	}

}
