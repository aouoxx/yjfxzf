package com.supconit.kqfx.web.analysis.dao.impl;

import java.util.List;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.analysis.dao.OverLoadDao;
import com.supconit.kqfx.web.analysis.entities.OverLoad;

@Repository("analysis_overload_dao")
public class OverLoadDaoImpl extends AbstractBasicDaoImpl<OverLoad, String>
		implements OverLoadDao {
	
	private static final String NAMESPACE = OverLoad.class.getName();
	
	@Override
	protected String getNamespace() {
		
		return NAMESPACE;
	}

	@Override
	public Pageable<OverLoad> findByPager(Pageable<OverLoad> pager,
			OverLoad condition) {
		// TODO Auto-generated method stub
		return findByPager(pager, "selectTjPager", "countTjPager", condition);
	}

	@Override
	public List<OverLoad> getByDetectionStation(OverLoad condition) {
		
		return selectList("getByDetectionStation",condition );
	}


}
