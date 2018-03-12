package com.supconit.kqfx.web.xtgl.services;

import hc.base.domains.Pageable;
import hc.orm.BasicOrmService;

import java.util.List;

import com.supconit.kqfx.web.xtgl.entities.ExtUser;
import com.supconit.kqfx.web.xtgl.entities.ExtUserCondition;

/**
 * 用户相关服务
 * 
 * @author 
 * 
 */
public interface ExtUserService extends BasicOrmService<ExtUser, Long> {


	Pageable<ExtUser> find(Pageable<ExtUser> pager, ExtUserCondition condition);

	List<ExtUser> findList(ExtUserCondition condition);

	List<ExtUser> findRoleByUserId(ExtUser u);
	
	/**
	 * 通过 JGBH 查找对应的USER ID
	 * @param jgbh
	 * @return
	 */
	List<ExtUser> getUserIdByJgbh(long jgbh);
	
	/**
	 * 通过role 查找对应的USER_ID
	 * @param roleid
	 * @return
	 */
	List<ExtUser> getUserIdByRoleId(long roleid);
	
}