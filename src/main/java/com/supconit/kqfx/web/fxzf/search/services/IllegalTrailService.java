package com.supconit.kqfx.web.fxzf.search.services;

import java.util.List;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.orm.BasicOrmService;

import com.supconit.kqfx.web.fxzf.search.entities.IllegalTrail;


public interface IllegalTrailService extends BasicOrmService<IllegalTrail,String>{

	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<IllegalTrail> findByPager(Pageable<IllegalTrail> pager, IllegalTrail condition);
	
	/**
	 * 同步违法车辆数据至车辆轨迹表
	 * @param license 车牌
	 * @param plateColor 车牌颜色
	 */
	AjaxMessage syncIllegalTrail(String license, String plateColor);
	
	/**
	 * 根据车牌以及车牌颜色查询唯一违法轨迹信息
	 * @param license 车牌
	 * @param plateColor 车牌颜色
	 * @return
	 */
	IllegalTrail findByLicenseAndColor(String license, String plateColor);
	
	/**
	 * 根据车牌以及车牌颜色判断是否已符合黑名单
	 * @param license  车牌
	 * @param plateColor 车牌颜色
	 * @return
	 */
	Boolean IsAccordBlackList(String license, String plateColor);
	
	/**
	 * 查询次数大于count的违章车记录
	 */
	List<IllegalTrail> findByOverLoadTimes(long counts);
	
	/**
	 * 根据ID删除对应的记录
	 * @param id
	 */
	void deleteById(String id);
}
