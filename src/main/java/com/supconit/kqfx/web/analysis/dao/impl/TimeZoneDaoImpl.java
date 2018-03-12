package com.supconit.kqfx.web.analysis.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.analysis.dao.TimeZoneDao;
import com.supconit.kqfx.web.analysis.entities.TimeZone;

@Repository("analysis_timezone_dao")
public class TimeZoneDaoImpl extends AbstractBasicDaoImpl<TimeZone, String> implements TimeZoneDao {
	
	private static final String NAMESPACE = TimeZone.class.getName();
	
	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public List<TimeZone> getByPartTime(TimeZone condition) {
		return selectList("getByPartTime", condition);
	}

	@Override
	public List<TimeZone> getByDateTime(TimeZone condition) {
		return selectList("getByDateTime",condition);
	}

	@Override
	public Pageable<TimeZone> findByHourPager(Pageable<TimeZone> pager, TimeZone condition) {
		return findByPager(pager, "selectPartPager", "countPartPager", condition);
	}

	@Override
	public Pageable<TimeZone> findByDayPager(Pageable<TimeZone> pager, TimeZone condition) {
		return findByPager(pager, "selectDayPager", "countDayPager", condition);
	}

	@Override
	public TimeZone getDateTimeCount(TimeZone condition) {
		return selectOne("getByDateTimesCount", condition);
	}

	@Override
	public TimeZone getPartTimeCount(TimeZone condition) {
		return selectOne("getByPartTimesCnt", condition);
	}


}
