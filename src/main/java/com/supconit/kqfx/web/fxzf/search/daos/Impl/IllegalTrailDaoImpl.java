package com.supconit.kqfx.web.fxzf.search.daos.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.fxzf.search.daos.IllegalTrailDao;
import com.supconit.kqfx.web.fxzf.search.entities.IllegalTrail;

@Repository("taizhou_offsite_enforcement_searchIllegalTrail_list_dao")
public class IllegalTrailDaoImpl extends AbstractBasicDaoImpl<IllegalTrail, String>
	implements IllegalTrailDao
{
	
	private final static String NAMESPACE=IllegalTrail.class.getName();
	
	
	@Override
	protected String getNamespace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}


	@Override
	public Pageable<IllegalTrail> findByPager(Pageable<IllegalTrail> pager,
			IllegalTrail condition) {
		return findByPager(pager, "selectPager", "countPager", condition);
	}


	@Override
	public IllegalTrail findByLicenseAndColor(String license, String plateColor) {
		IllegalTrail trail = new IllegalTrail();
		trail.setLicense(license);
		trail.setPlateColor(plateColor);
		return selectOne("findByLicenseAndColor", trail);
	}


	@Override
	public List<IllegalTrail> findByOverLoadTimes(long counts) {
		// TODO Auto-generated method stub
		return selectList("findByOverLoadTimes",counts);
	}


	@Override
	public void deleteById(String id) {
		delete("deleteById", id);
	}

}
