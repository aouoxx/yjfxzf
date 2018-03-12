package com.supconit.kqfx.web.analysis.dao;

import hc.base.domains.Pageable;
import hc.orm.BasicDao;

import java.util.List;

import com.supconit.kqfx.web.analysis.entities.OverLoad;

public interface OverLoadDao extends BasicDao<OverLoad, String> {
	
	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<OverLoad> findByPager(Pageable<OverLoad> pager, OverLoad condition);
	
	
	List<OverLoad> getByDetectionStation(OverLoad condition);
	
	

}
