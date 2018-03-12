package com.supconit.kqfx.web.list.services;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.orm.BasicOrmService;

import java.util.List;

import com.supconit.kqfx.web.list.entities.BlackList;

public interface BlackListService extends BasicOrmService<BlackList, String> {
	
	/**
	 * 获取所有黑名单数据
	 * @return  List<BlackList>黑名单列表
	 */
	List<BlackList>  getAll();
	
	/**
	 * 同步违法车辆数据至黑名单
	 * @param license 车牌
	 * @param plateColor 车牌颜色
	 */
	AjaxMessage syncBlackList(String license, String plateColor);
	
	
	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<BlackList> findByPager(Pageable<BlackList> pager, BlackList condition);
	
	/**
	 * 删除历史超限次数小于当前阈值的黑名单
	 * @param condition
	 */
	void upDateByOverLoadTimes(BlackList condition);
	
	/**
	 * 根据ID删除数据
	 * @param id
	 */
	void deleteById(String id);
	
	/**
	 * 根据车牌号和颜色查找黑名单
	 */
	int findByPlate(BlackList condition);
	
	/**
	 * 批量删除黑名单
	 * @param ids
	 */
	void batchDeleteBlackList(List<String> ids);
	
	
	/**
	 * 批量插入黑名单
	 * @param lists
	 */
	void batchInertBlackList(List<BlackList> lists);
	
	
	/**
	 * 删除所有非手动添加的黑名单
	 */
	void deleteAllNoHandler();
	
	/**
	 * 获取所有手工添加的黑名单
	 */
	List<BlackList> getAllHandler();
	
	/**
	 * 更新手动添加黑名单的违法次数
	 * @param condition
	 */
	void updateHandlerOverTimes(BlackList condition);
	
	/**
	 * 根据车牌号和颜色找到对应的车辆
	 * @param condition
	 * @return
	 */
	BlackList findByPlateAndColor(BlackList condition);
	
}
