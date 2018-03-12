package com.supconit.kqfx.web.analysis.services.impl;

import java.util.List;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicOrmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.analysis.dao.TimeZoneDao;
import com.supconit.kqfx.web.analysis.entities.TimeZone;
import com.supconit.kqfx.web.analysis.services.TimeZoneService;

@Service("analysis_timezone_service")
public class TimeZoneServiceImpl extends AbstractBasicOrmService<TimeZone, String> implements TimeZoneService {

	@Autowired
	private TimeZoneDao timeZoneDao;
	
	@Override
	public TimeZone getById(String id) {
		return this.timeZoneDao.getById(id);
	}

	@Override
	public void insert(TimeZone entity) {
		this.timeZoneDao.insert(entity);
	}

	@Override
	public void update(TimeZone entity) {
		this.timeZoneDao.update(entity);
	}

	@Override
	public void delete(TimeZone entity) {
		this.timeZoneDao.delete(entity);
	}

	@Override
	public List<TimeZone> getByPartTime(TimeZone condition) {
		return timeZoneDao.getByPartTime(condition);
	}

	@Override
	public List<TimeZone> getByDateTime(TimeZone condition) {
		return timeZoneDao.getByDateTime(condition);
	}

	@Override
	public Pageable<TimeZone> findByHourPager(Pageable<TimeZone> pager, TimeZone condition) {
		return timeZoneDao.findByHourPager(pager, condition);
	}

	@Override
	public Pageable<TimeZone> findByDayPager(Pageable<TimeZone> pager, TimeZone condition) {
		return timeZoneDao.findByDayPager(pager, condition);
	}

	@Override
	public TimeZone getDateTimeCount(TimeZone condition) {
		return timeZoneDao.getDateTimeCount(condition);
	}

	@Override
	public TimeZone getPartTimeCount(TimeZone condition) {
		return timeZoneDao.getPartTimeCount(condition);
	}

}
