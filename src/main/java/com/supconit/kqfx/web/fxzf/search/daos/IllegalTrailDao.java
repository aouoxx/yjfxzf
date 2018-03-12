package com.supconit.kqfx.web.fxzf.search.daos;

import java.util.List;

import hc.base.domains.Pageable;
import hc.orm.BasicDao;

import com.supconit.kqfx.web.fxzf.search.entities.IllegalTrail;

public interface IllegalTrailDao extends BasicDao<IllegalTrail, String> {
	
	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<IllegalTrail> findByPager(Pageable<IllegalTrail> pager, IllegalTrail condition);

	IllegalTrail findByLicenseAndColor(String license, String plateColor);
	
	/**
	 * 查询次数大于count的违章车记录
	 */
	List<IllegalTrail> findByOverLoadTimes(long counts);
	
	void deleteById(String id);

}
