package com.supconit.kqfx.web.analysis.services.impl;

import java.util.List;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicOrmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.analysis.dao.OverLoadDao;
import com.supconit.kqfx.web.analysis.entities.OverLoad;
import com.supconit.kqfx.web.analysis.services.OverLoadService;

@Service("analysis_overload_service")
public class OverLoadServiceImpl extends
		AbstractBasicOrmService<OverLoad, String> implements OverLoadService {
	
	@Autowired
	private OverLoadDao  overLoadDao; 
	
	@Override
	public OverLoad getById(String id) {
		return this.overLoadDao.getById(id);
	}

	@Override
	public void insert(OverLoad entity) {
		this.overLoadDao.insert(entity);
	}

	@Override
	public void update(OverLoad entity) {
		this.overLoadDao.update(entity);
	}

	@Override
	public void delete(OverLoad entity) {
		this.overLoadDao.delete(entity);
	}

	@Override
	public Pageable<OverLoad> findByPager(Pageable<OverLoad> pager,
			OverLoad condition) {
		// TODO Auto-generated method stub
		return overLoadDao.findByPager(pager, condition);
	}

	@Override
	public List<OverLoad> getByDetectionStation(OverLoad condition) {
		// TODO Auto-generated method stub
		return overLoadDao.getByDetectionStation(condition);
	}

}
