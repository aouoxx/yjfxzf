package com.supconit.kqfx.web.fxzf.qbb.daos.impl;

import org.springframework.stereotype.Repository;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.fxzf.qbb.daos.QbbMbxxDao;
import com.supconit.kqfx.web.fxzf.qbb.entities.QbbMbxx;

@Repository("fxzf_qbb_qbbmbxx_dao")
public class QbbMbxxDaoImpl extends AbstractBasicDaoImpl<QbbMbxx, String> implements
		QbbMbxxDao {
	
	private static final String NAMESPACE = QbbMbxx.class.getName();
	
	
	@Override
	protected String getNamespace() {
		
		return NAMESPACE;
	}


	@Override
	public Pageable<QbbMbxx> find(Pagination<QbbMbxx> pager, QbbMbxx condition) {
		
		return findByPager(pager, "selectPager", "countPager", condition);
	}

	

}
