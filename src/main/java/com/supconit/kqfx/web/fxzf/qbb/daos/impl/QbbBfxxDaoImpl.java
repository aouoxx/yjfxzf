package com.supconit.kqfx.web.fxzf.qbb.daos.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.fxzf.qbb.daos.QbbBfxxDao;
import com.supconit.kqfx.web.fxzf.qbb.entities.QbbBfxx;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnHistory;

@Repository("fxzf_qbb_qbbbfxx_dao")
public class QbbBfxxDaoImpl extends AbstractBasicDaoImpl<QbbBfxx, String> implements
		QbbBfxxDao {
	
	private static final String NAMESPACE = QbbBfxx.class.getName();
	
	@Override
	protected String getNamespace() {
		
		return NAMESPACE;
	}

	@Override
	public Pageable<QbbBfxx> find(Pagination<QbbBfxx> pager, QbbBfxx condition) {
		
		return findByPager(pager, "selectPager", "countPager", condition);
	}

	@Override
	public List<QbbBfxx> getByCondition(QbbBfxx condition) {
		
		return this.selectList("getByCondition", condition);
	}

	@Override
	public List<QbbBfxx> IsWarnTypeHistrory(QbbBfxx qbbbfxx) {
		
		return selectList("IsWarnTypeHistrory", qbbbfxx);
	}

	@Override
	public List<QbbBfxx> IsPublishTypeHistrory(QbbBfxx qbbbfxx) {
		
		return selectList("IsPublishTypeHistrory", qbbbfxx);
	}

	@Override
	public QbbBfxx getByBfxxId(String id) {
		// TODO Auto-generated method stub
		return selectOne("getByBfxxId", id);
	}

	

}
