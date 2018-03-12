package com.supconit.kqfx.web.list.daos;

import hc.base.domains.Pageable;
import hc.orm.BasicDao;

import java.util.List;

import com.supconit.kqfx.web.list.entities.BlackList;

public interface BlackListDao extends BasicDao<BlackList, String> {

	List<BlackList> getAll();
	
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
	 * 根据ID删除黑名单
	 */
	void deleteById(String id);
	
	/**
	 * 根据车牌号找到颜色
	 * @param condition
	 * @return
	 */
	int findByPlate(BlackList condition);
	
	/**
	 * 根据车牌号和颜色找到对应的车辆
	 * @param condition
	 * @return
	 */
	BlackList findByPlateAndColor(BlackList condition);
	
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
}
