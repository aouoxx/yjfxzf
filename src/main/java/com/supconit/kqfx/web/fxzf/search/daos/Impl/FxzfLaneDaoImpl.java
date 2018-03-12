package com.supconit.kqfx.web.fxzf.search.daos.Impl;

import java.util.List;

import hc.orm.AbstractBasicDaoImpl;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.fxzf.search.daos.FxzfLaneDao;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfLane;
@Repository("kqfxzf_kqfx_laneFxzf_dao")
public class FxzfLaneDaoImpl extends AbstractBasicDaoImpl<FxzfLane, String> implements FxzfLaneDao {

	private final static String NAMESPACE = FxzfLane.class.getName();
	
	@Override
	protected String getNamespace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}

	@Override
	public int getStationLaneCount(FxzfLane lane) {
		// TODO Auto-generated method stub
		return selectOne("getStationLaneCount", lane);
	}

	@Override
	public List<FxzfLane> getByStation(String station) {
		// TODO Auto-generated method stub
		return selectList("getByStation", station);
	}

	

	
	
}
