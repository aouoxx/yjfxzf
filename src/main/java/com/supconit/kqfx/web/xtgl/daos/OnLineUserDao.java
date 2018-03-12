package com.supconit.kqfx.web.xtgl.daos;

import hc.base.domains.Pageable;

import com.supconit.kqfx.web.xtgl.entities.OnLineUser;



/**
 * @author 
 * @create 2014-05-06 15:21:36
 * @since 
 * 
 */
 
public interface OnLineUserDao extends hc.orm.BasicDao<OnLineUser, String> {

	/**
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<OnLineUser> findByPager(Pageable<OnLineUser> pager, OnLineUser condition);

}
