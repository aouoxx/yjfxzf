package com.supconit.kqfx.web.fxzf.notify.services.Impl;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicOrmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.fxzf.notify.daos.NotifyDao;
import com.supconit.kqfx.web.fxzf.notify.entities.Notify;
import com.supconit.kqfx.web.fxzf.notify.services.NotifyService;



@Service("taizhou_offsite_enforcement_notify_service")
public class NotifyServiceImpl extends AbstractBasicOrmService<Notify, String>  implements NotifyService {
	
	@Autowired
	private NotifyDao notifyDao;

	@Override
	public Notify getById(String arg0) {
		// TODO Auto-generated method stub
		return notifyDao.getById(arg0);
	}

	@Override
	public void save(Notify arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(Notify entity) {
		notifyDao.insert(entity);

	}

	@Override
	public void update(Notify entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Notify entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Pageable<Notify> findByPager(Pageable<Notify> pager, Notify condition) {
		// TODO Auto-generated method stub
		return notifyDao.findByPager(pager, condition);
	}

}
