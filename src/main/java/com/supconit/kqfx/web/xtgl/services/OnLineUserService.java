package com.supconit.kqfx.web.xtgl.services;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;

import com.supconit.kqfx.web.xtgl.entities.OnLineUser;



/**
 * @author 
 * @create 2014-04-13 11:02:51 
 * @since 
 * 
 */
public interface OnLineUserService extends hc.orm.BasicOrmService<OnLineUser, String> {

	/**
	 * 分页按条件查询。
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<OnLineUser> find(Pagination<OnLineUser> pager, OnLineUser condition);
}