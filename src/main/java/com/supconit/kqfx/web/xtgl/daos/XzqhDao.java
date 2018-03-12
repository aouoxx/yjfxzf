/**
 * 
 */
package com.supconit.kqfx.web.xtgl.daos;

import hc.base.domains.Pageable;

import java.util.List;

import com.supconit.kqfx.web.xtgl.entities.Xzqh;

/**
 * @author 
 * @create 2014-03-17 16:03:40
 * @since 
 * 
 */
 
public interface XzqhDao extends hc.orm.BasicDao<Xzqh, String> {

	/**
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<Xzqh> findByPager(Pageable<Xzqh> pager, Xzqh condition);
	
	List<Xzqh> findList(Xzqh condition);

}
