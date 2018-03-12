package com.supconit.kqfx.web.fxzf.search.daos;

import java.util.List;

import hc.orm.BasicDao;

import com.supconit.kqfx.web.fxzf.search.entities.FxzfLane;

public interface FxzfLaneDao extends BasicDao<FxzfLane, String>{
	
	int getStationLaneCount(FxzfLane lane);
	List<FxzfLane> getByStation(String station);

}
