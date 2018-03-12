package com.supconit.kqfx.web.fxzf.search.services;

import hc.orm.BasicOrmService;

import java.util.List;

import com.supconit.kqfx.web.fxzf.search.entities.ZczTree;
/**
 * 治超站树service
 * @author dell
 *
 */
public interface ZczTreeService extends BasicOrmService<ZczTree,Long>{

	/**
	 * 获取治超站数据结构
	 * @return
	 */
	List<ZczTree> getAllNodes(ZczTree node);
}
