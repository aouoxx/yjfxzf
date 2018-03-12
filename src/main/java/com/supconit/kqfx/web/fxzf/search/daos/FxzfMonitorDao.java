package com.supconit.kqfx.web.fxzf.search.daos;

import java.util.Date;
import java.util.List;

import hc.orm.BasicDao;

import com.supconit.kqfx.web.fxzf.search.entities.FxzfMonitor;

public interface FxzfMonitorDao extends BasicDao<FxzfMonitor, String>{
	
	List<FxzfMonitor> getByStation(String staion);
	
	List<FxzfMonitor> getByTotalStation(FxzfMonitor monitor);
	
	int getStationCount(FxzfMonitor monitor);
	
	Date getMinTime(FxzfMonitor monitor);
	
	FxzfMonitor getByTimeAndStation(FxzfMonitor monitor);
	
	void deleteByTime(FxzfMonitor monitor);
	
	void deleteByMonitorId(String id);

}
