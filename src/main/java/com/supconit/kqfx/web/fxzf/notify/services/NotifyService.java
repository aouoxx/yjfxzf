package com.supconit.kqfx.web.fxzf.notify.services;

import hc.base.domains.Pageable;
import hc.orm.BasicOrmService;

import com.supconit.kqfx.web.fxzf.notify.entities.Notify;


public interface NotifyService extends BasicOrmService<Notify,String> {
	
	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<Notify> findByPager(Pageable<Notify> pager, Notify condition);
}
