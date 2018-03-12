package com.supconit.kqfx.web.fxzf.qbb.services;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.orm.BasicOrmService;

import com.supconit.kqfx.web.fxzf.qbb.entities.QbbMbxx;

public interface QbbMbxxService extends BasicOrmService<QbbMbxx, String> {

	/**
	 * 分页按条件查询
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<QbbMbxx> find(Pagination<QbbMbxx> pager, QbbMbxx condition);

}
