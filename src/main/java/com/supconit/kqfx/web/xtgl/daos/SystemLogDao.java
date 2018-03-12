/**
 * 
 */
package com.supconit.kqfx.web.xtgl.daos;

import hc.base.domains.Pageable;

import com.supconit.kqfx.web.xtgl.entities.SystemLog;

/**
 * @author 
 * @create 2014-04-09 15:46:42
 * @since 
 * 
 */
 
public interface SystemLogDao extends hc.orm.BasicDao<SystemLog, String> {

	/**
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<SystemLog> findByPager(Pageable<SystemLog> pager, SystemLog condition);

}
