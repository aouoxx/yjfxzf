package com.supconit.kqfx.web.fxzf.warn.daos;

import hc.base.domains.Pageable;
import hc.orm.BasicDao;

import java.util.List;

import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnHistory;

public interface WarnHistoryDao extends BasicDao<WarnHistory, String>{

	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<WarnHistory> findByPager(Pageable<WarnHistory> pager, WarnHistory condition);
	
	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<WarnHistory> findByPagerExport(Pageable<WarnHistory> pager, WarnHistory condition);
	
	/**
	 * 根据车牌号、车牌颜色、间隔时间查询已发布的告警信息
	 * @param license
	 * @param plateColor
	 * @param againTime
	 * @return
	 */
	List<WarnHistory> findAgainTimeData(String license, String plateColor,
                                        Double againTime);

	List<WarnHistory> AnalysisDayWarnByCondition(WarnHistory condition);
	
	List<WarnHistory> getWarnByFxzfId(String fxzfid);
	
	WarnHistory getWarnHistoryQbb(Fxzf warn);
	
}
