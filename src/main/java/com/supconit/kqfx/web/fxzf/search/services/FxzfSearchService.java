package com.supconit.kqfx.web.fxzf.search.services;

import hc.base.domains.Pageable;
import hc.orm.BasicOrmService;

import java.util.List;

import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;



public interface FxzfSearchService extends BasicOrmService<Fxzf,String> {
	
	/**
	 * 分页查询信息列表,实时过车记录页面(含有车牌号模糊查询)
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<Fxzf> findByPager(Pageable<Fxzf> pager, Fxzf condition);
	
	/**
	 * 分页查询信息列表(违章过车和黑名单过车轨迹,使用车牌号和车牌颜色进行精确查询)
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<Fxzf> findByPagerDetail(Pageable<Fxzf> pager, Fxzf condition);
	
	
	/**
	 * 批量新增
	 * @param fxzfList
	 */
	void batchInsert(List<Fxzf> fxzfList);
	
	/**
	 * 告警触发接口(判断是否超过告警阀值)
	 * @param fxzf
	 * @return
	 */
	Boolean warnTrigger(Fxzf fxzf);
	
	/**
	 * 审核信息
	 */
	void checkFxzfMessage(Fxzf condition);
	
	/**
	 * 获取某个超限状态的数目
	 */
	int getOverLoadStatusCount(Fxzf fxzf);
	
	/**
	 * 获取所有超限状态的数目
	 */
	int getIllegalLevelCount(Fxzf fxzf);
	
	/**
	 * 获取治超站过车数量、违法过车数量、治超站当天违法过车数量(监控系统调用)
	 * @param fxzf
	 * @return
	 */
	Integer getCountByCondition(Fxzf fxzf);
	
	/**
	 * 获取某违法程度过车数量(监控系统调用)
	 * @param fxzf
	 * @return
	 */
	Integer getOverLoadCount(Fxzf fxzf);
	
	/**
	 * 根据过车记录ID删除该记录信息
	 * @param id
	 */
	void deleteById(String id);
	

	/**
	 * 获取没有被上传的前10条信息
	 */
	List<Fxzf> getFxzfToTransport();
	
	/**
	 * 更新过车数据
	 * @param fxzf
	 */
	void upDateFxzftransport(Fxzf fxzf);
}
