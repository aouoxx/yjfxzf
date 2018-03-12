package com.supconit.kqfx.web.fxzf.search.services.Impl;

import java.util.List;

import hc.orm.AbstractBasicOrmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.fxzf.search.daos.FxzfLaneDao;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfLane;
import com.supconit.kqfx.web.fxzf.search.services.FxzfLaneService;
@Service("kqfxzf_kqfx_laneFxzf_service")
public class FxzfLaneServiceImpl extends AbstractBasicOrmService<FxzfLane, String>
implements FxzfLaneService {

	@Autowired
	private FxzfLaneDao fxzfLaneDao;

	@Override
	public FxzfLane getById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(FxzfLane entity) {
		fxzfLaneDao.insert(entity);
	}

	@Override
	public void update(FxzfLane entity) {
		fxzfLaneDao.update(entity);
	}

	@Override
	public void delete(FxzfLane entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getStationLaneCount(FxzfLane lane) {
		// TODO Auto-generated method stub
		return fxzfLaneDao.getStationLaneCount(lane);
	}

	@Override
	public List<FxzfLane> getByStation(String station) {
		// TODO Auto-generated method stub
		return fxzfLaneDao.getByStation(station);
	}
	


}
