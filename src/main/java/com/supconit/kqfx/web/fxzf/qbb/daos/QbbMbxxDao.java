package com.supconit.kqfx.web.fxzf.qbb.daos;

import com.supconit.kqfx.web.fxzf.qbb.entities.QbbMbxx;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.orm.BasicDao;

public interface QbbMbxxDao extends BasicDao<QbbMbxx, String> {

	Pageable<QbbMbxx> find(Pagination<QbbMbxx> pager, QbbMbxx condition);

}
