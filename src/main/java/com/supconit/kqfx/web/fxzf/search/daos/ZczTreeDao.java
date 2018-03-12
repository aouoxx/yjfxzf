package com.supconit.kqfx.web.fxzf.search.daos;

import hc.orm.BasicDao;

import java.util.List;

import com.supconit.kqfx.web.fxzf.search.entities.ZczTree;
/**
 * 获取治超站树Dao
 * @author gs
 *
 */
public interface ZczTreeDao extends BasicDao<ZczTree, Long>{
		/**
		 * 获取治超站数据结构
		 * @return
		 */
		List<ZczTree> getAllNodes(ZczTree node);
}
