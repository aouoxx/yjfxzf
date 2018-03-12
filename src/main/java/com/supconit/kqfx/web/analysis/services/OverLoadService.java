package com.supconit.kqfx.web.analysis.services;

import hc.base.domains.Pageable;
import hc.orm.BasicOrmService;

import java.util.List;

import com.supconit.kqfx.web.analysis.entities.OverLoad;

public interface OverLoadService extends BasicOrmService<OverLoad, String> {
	
	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<OverLoad> findByPager(Pageable<OverLoad> pager, OverLoad condition);
	
	/**
	 * 查询获取统计列表数据
	 * @return
	 */
	List<OverLoad> getByDetectionStation(OverLoad condition);
}
