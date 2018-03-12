package com.supconit.kqfx.web.xtgl.daos.impl;

import hc.base.domains.Pageable;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.xtgl.daos.OnLineUserDao;
import com.supconit.kqfx.web.xtgl.entities.OnLineUser;

/**
 * @author 
 * @create 2014-05-06 15:21:36
 * @since 
 * 
 */
 
@Repository("wzzhgl_xtgl_onLineUser_dao")
public class OnLineUserDaoImpl extends hc.orm.AbstractBasicDaoImpl<OnLineUser, String> implements OnLineUserDao {

	private static final String	NAMESPACE	= OnLineUser.class.getName();

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}
	
	@Override
	public Pageable<OnLineUser> findByPager(Pageable<OnLineUser> pager, OnLineUser condition) {
		return findByPager(pager, "selectPager", "countPager", condition);
	}
}
