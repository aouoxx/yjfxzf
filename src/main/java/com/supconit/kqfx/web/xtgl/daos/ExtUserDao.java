package com.supconit.kqfx.web.xtgl.daos;

import hc.base.domains.Pageable;

import java.util.List;

import com.supconit.kqfx.web.xtgl.entities.ExtUser;
import com.supconit.kqfx.web.xtgl.entities.ExtUserCondition;

public interface ExtUserDao extends hc.orm.BasicDao<ExtUser, Long> {
	
	ExtUser getByCondition(ExtUser condition);

	/**
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<ExtUser> findByPager(Pageable<ExtUser> pager, ExtUserCondition condition);

	/**
	 * 
	 * @param condition
	 * @return
	 */
	List<ExtUser> findList(ExtUserCondition condition);

	List<ExtUser> findRoleByUserId(ExtUser condition);
	
	List<ExtUser> getUserIdByJgbh(long jgbh);
	
	List<ExtUser> getUserIdByRoleId(long roleid);

}
