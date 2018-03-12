package com.supconit.kqfx.web.fxzf.qbb.services.impl;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.orm.AbstractBasicOrmService;

import com.supconit.kqfx.web.fxzf.qbb.daos.QbbMbxxDao;
import com.supconit.kqfx.web.fxzf.qbb.entities.QbbMbxx;
import com.supconit.kqfx.web.fxzf.qbb.services.QbbMbxxService;

@Service("fxzf_qbb_qbbmbxx_service")
public class QbbMbxxServiceImpl extends AbstractBasicOrmService<QbbMbxx, String>
		implements QbbMbxxService {

	@Autowired
	private QbbMbxxDao qbbMbxxDao;
	
	@Override
	public QbbMbxx getById(String id) {
		
		return this.qbbMbxxDao.getById(id);
	}

	@Override
	public void save(QbbMbxx entity) {
		
		if(StringUtil.isEmpty(entity.getId()))
		{
			this.qbbMbxxDao.insert(entity);
		}else{
			this.qbbMbxxDao.update(entity);
		}
	}

	@Override
	public void insert(QbbMbxx entity) {
		
		this.qbbMbxxDao.insert(entity);
	}

	@Override
	public void update(QbbMbxx entity) {
		this.qbbMbxxDao.update(entity);
	}

	@Override
	public void delete(QbbMbxx entity) {
		this.qbbMbxxDao.delete(entity);
	}

	@Override
	public Pageable<QbbMbxx> find(Pagination<QbbMbxx> pager, QbbMbxx condition) {
		
		return this.qbbMbxxDao.find(pager,condition);
	}

}
