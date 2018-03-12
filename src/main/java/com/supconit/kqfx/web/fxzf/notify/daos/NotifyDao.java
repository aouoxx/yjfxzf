package com.supconit.kqfx.web.fxzf.notify.daos;

import hc.base.domains.Pageable;
import hc.orm.BasicDao;

import com.supconit.kqfx.web.fxzf.notify.entities.Notify;


public interface NotifyDao extends BasicDao<Notify,String> {
	
	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<Notify> findByPager(Pageable<Notify> pager, Notify condition);
	
	int insert(Notify notify);

}
