/**
 * 
 */
package com.supconit.kqfx.web.xtgl.daos.impl;

import hc.base.domains.Pageable;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.xtgl.daos.SystemLogDao;
import com.supconit.kqfx.web.xtgl.entities.SystemLog;

/**
 * @author 
 * @create 2014-04-09 15:46:42
 * @since 
 * 
 */
 
@Repository("xtgl_systemLog_dao")
public class SystemLogDaoImpl extends hc.orm.AbstractBasicDaoImpl<SystemLog, String> implements SystemLogDao {

	private static final String	NAMESPACE	= SystemLog.class.getName();

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}
	
	
	@Override
	public Pageable<SystemLog> findByPager(Pageable<SystemLog> pager, SystemLog condition) {
		return findByPager(pager, "selectPager", "countPager", condition);
	}
}
