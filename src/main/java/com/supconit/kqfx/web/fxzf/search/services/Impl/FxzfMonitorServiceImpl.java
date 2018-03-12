package com.supconit.kqfx.web.fxzf.search.services.Impl;

import java.util.Date;
import java.util.List;

import hc.orm.AbstractBasicOrmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.fxzf.search.daos.FxzfMonitorDao;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfMonitor;
import com.supconit.kqfx.web.fxzf.search.services.FxzfMonitorService;
@Service("taizhou_offsite_enforcement_monitorFxzf_service")
public class FxzfMonitorServiceImpl extends AbstractBasicOrmService<FxzfMonitor, String>
implements FxzfMonitorService {

	@Autowired
	private FxzfMonitorDao fxzfMonitorDao;
	
	@Override
	public FxzfMonitor getById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(FxzfMonitor entity) {
		// TODO Auto-generated method stub
		fxzfMonitorDao.insert(entity);
	}

	@Override
	public void update(FxzfMonitor entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(FxzfMonitor entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStationCount(FxzfMonitor monitor) {
		// TODO Auto-generated method stub
		return fxzfMonitorDao.getStationCount(monitor);
	}

	@Override
	public Date getMinTime(FxzfMonitor monitor) {
		// TODO Auto-generated method stub
		return fxzfMonitorDao.getMinTime(monitor);
	}

	@Override
	public FxzfMonitor getByTimeAndStation(FxzfMonitor monitor) {
		// TODO Auto-generated method stub
		return fxzfMonitorDao.getByTimeAndStation(monitor);
	}

	@Override
	public void deleteByTime(FxzfMonitor monitor) {
		fxzfMonitorDao.deleteByTime(monitor);
	}

	@Override
	public void deleteByMonitorId(String id) {
		// TODO Auto-generated method stub
		fxzfMonitorDao.deleteByMonitorId(id);
	}

	@Override
	public List<FxzfMonitor> getByStation(String staion) {
		// TODO Auto-generated method stub
		return fxzfMonitorDao.getByStation(staion);
	}

	@Override
	public List<FxzfMonitor> getByTotalStation(FxzfMonitor monitor) {
		// TODO Auto-generated method stub
		return fxzfMonitorDao.getByTotalStation(monitor);
	}

}
