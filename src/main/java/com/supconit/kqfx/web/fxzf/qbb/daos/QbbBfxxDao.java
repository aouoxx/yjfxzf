package com.supconit.kqfx.web.fxzf.qbb.daos;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.orm.BasicDao;

import java.util.List;

import com.supconit.kqfx.web.fxzf.qbb.entities.QbbBfxx;

public interface QbbBfxxDao extends BasicDao<QbbBfxx, String> {

	Pageable<QbbBfxx> find(Pagination<QbbBfxx> pager, QbbBfxx condition);

	List<QbbBfxx> getByCondition(QbbBfxx condition);

	List<QbbBfxx> IsWarnTypeHistrory(QbbBfxx qbbbfxx);

	List<QbbBfxx> IsPublishTypeHistrory(QbbBfxx qbbbfxx);

	QbbBfxx getByBfxxId(String id);
}
