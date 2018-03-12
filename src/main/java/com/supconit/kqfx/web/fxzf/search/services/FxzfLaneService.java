package com.supconit.kqfx.web.fxzf.search.services;

import java.util.List;

import hc.orm.BasicOrmService;

import com.supconit.kqfx.web.fxzf.search.entities.FxzfLane;

public interface FxzfLaneService extends BasicOrmService<FxzfLane,String>{

	
	/**
	 * 获取该志超站下对应的车道的违法记录，
	 * @param monitor
	 * @return
	 */
	int getStationLaneCount(FxzfLane lane);
	
	List<FxzfLane> getByStation(String station);
	
	

}
