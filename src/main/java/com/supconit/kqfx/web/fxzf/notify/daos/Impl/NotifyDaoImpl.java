package com.supconit.kqfx.web.fxzf.notify.daos.Impl;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.fxzf.notify.daos.NotifyDao;
import com.supconit.kqfx.web.fxzf.notify.entities.Notify;



@Repository("taizhou_offsite_enforcement_notify_dao")
public class NotifyDaoImpl extends AbstractBasicDaoImpl<Notify, String> implements NotifyDao {
	
	private static final String NAMESPACE=Notify.class.getName();

	@Override
	public Pageable<Notify> findByPager(Pageable<Notify> pager, Notify condition) {
		// TODO Auto-generated method stub
		return findByPager(pager, "selectPager", "countPager", condition);
	}

	@Override
	protected String getNamespace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}

	@Override
	public int insert(Notify entity) {
		// TODO Auto-generated method stub
		return super.insert(entity);
	}

	@Override
	public Notify getById(String id) {
		// TODO Auto-generated method stub
		return super.getById(id);
	}
	
	
	
	

}
