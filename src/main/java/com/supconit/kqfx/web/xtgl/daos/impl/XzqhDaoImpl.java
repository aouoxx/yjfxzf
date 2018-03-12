/**
 * 
 */
package com.supconit.kqfx.web.xtgl.daos.impl;

import hc.base.domains.Pageable;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.xtgl.daos.XzqhDao;
import com.supconit.kqfx.web.xtgl.entities.Xzqh;

/**
 * @author 
 * @create 2014-03-17 16:03:40
 * @since 
 * 
 */
 
@Repository("xtgl_xzqh_dao")
public class XzqhDaoImpl extends hc.orm.AbstractBasicDaoImpl<Xzqh, String> implements XzqhDao {

	private static final String	NAMESPACE	= Xzqh.class.getName();

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}
	
	
	@Override
	public Pageable<Xzqh> findByPager(Pageable<Xzqh> pager, Xzqh condition) {
		return findByPager(pager, "selectPager", "countPager", condition);
	}

	@Override
	public List<Xzqh> findList(Xzqh condition) {
		return selectList("findList", condition);
	}
}
