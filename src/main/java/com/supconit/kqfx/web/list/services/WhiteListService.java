package com.supconit.kqfx.web.list.services;

import hc.base.domains.Pageable;
import hc.orm.BasicOrmService;

import java.util.List;

import com.supconit.kqfx.web.list.entities.WhiteList;

public interface WhiteListService extends BasicOrmService<WhiteList, String> {
	
	/**
	 * 获取所有白名单数据
	 * @return  List<BlackList>白名单列表
	 */
	List<WhiteList> getAll();
	
	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<WhiteList> findByPager(Pageable<WhiteList> pager, WhiteList condition);
	
	
	/**
	 * 根据车牌号和颜色查找黑名单
	 */
	int findByPlate(WhiteList condition);
	
	/**
	 * 根据ID删除数据
	 * @param id
	 */
	void deleteById(String id);
	
}
