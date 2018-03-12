package com.supconit.kqfx.web.fxzf.warn.services;

import java.util.List;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.orm.BasicOrmService;

import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnHistory;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnInfo;

public interface WarnHistoryService extends BasicOrmService<WarnHistory, String>{
	
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
	 * 判断是否告警时间间隔内已发布告警信息
	 * @param license 车牌
	 * @param plateColor 车牌颜色
	 * @return
	 */
	Boolean IsPublishWarnMessage(String license, String plateColor);
	
	/**
	 * 自动发送告警信息至情报板
	 * @param fxzf
	 */
	AjaxMessage publishToQbb(Fxzf fxzf);
	
	/**
	 * 自动发送告警信息至WEB端
	 * @param fxzf
	 */
	AjaxMessage publishToWeb(Fxzf fxzf);
	
	/**
	 * 统计治超站每日告警信息
	 */
	List<WarnHistory> AnalysisDayWarnByCondition(WarnHistory condition);
	
	/**
	 * 通过正则将表达式中字段匹配成对象属性
	 * @param fxzf
	 * @param warnInfo
	 * @return
	 */
	String getWarnInfo(Fxzf fxzf, WarnInfo warnInfo);
	
	
	/**
	 * 根据关联的T_FXZF表的ID找到对应的
	 * @param fxzfid
	 * @return
	 */
	List<WarnHistory> getWarnByFxzfId(String fxzfid);
	
	/**
	 * 通过车牌号,车牌颜色和过车时间来获取告警的信息内容
	 * @param warn
	 * @return
	 */
	WarnHistory getWarnHistoryQbb(Fxzf warn);
}
