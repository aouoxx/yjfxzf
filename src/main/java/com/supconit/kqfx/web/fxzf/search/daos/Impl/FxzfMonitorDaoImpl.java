package com.supconit.kqfx.web.fxzf.search.daos.Impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.fxzf.search.daos.FxzfMonitorDao;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfMonitor;
@Repository("taizhou_offsite_enforcement_monitorFxzf_dao")
public class FxzfMonitorDaoImpl extends AbstractBasicDaoImpl<FxzfMonitor, String> implements FxzfMonitorDao {

	private final static String NAMESPACE = FxzfMonitor.class.getName();
	
	@Override
	protected String getNamespace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}

	@Override
	public int getStationCount(FxzfMonitor monitor) {
		// TODO Auto-generated method stub
		return selectOne("getStationCount", monitor);
	}

	@Override
	public Date getMinTime(FxzfMonitor monitor) {
		// TODO Auto-generated method stub
		return selectOne("getMinTime", monitor);
	}

	@Override
	public FxzfMonitor getByTimeAndStation(FxzfMonitor monitor) {
		// TODO Auto-generated method stub
		return selectOne("getByTimeAndStation", monitor);
	}

	@Override
	public void deleteByTime(FxzfMonitor monitor) {
		// TODO Auto-generated method stub
		delete("deleteByTime", monitor);
	}

	@Override
	public void deleteByMonitorId(String id) {
		// TODO Auto-generated method stub
		delete("deleteById", id);
	}

	@Override
	public List<FxzfMonitor> getByStation(String staion) {
		// TODO Auto-generated method stub
		return selectList("getByStation", staion);
	}

	@Override
	public List<FxzfMonitor> getByTotalStation(FxzfMonitor monitor) {
		// TODO Auto-generated method stub
		return selectList("getByTotalStation", monitor);
	}

	
	
}
