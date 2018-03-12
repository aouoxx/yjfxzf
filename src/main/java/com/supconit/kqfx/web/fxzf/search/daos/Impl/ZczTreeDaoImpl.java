package com.supconit.kqfx.web.fxzf.search.daos.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.fxzf.avro.redis_BoardData;
import com.supconit.kqfx.web.fxzf.search.daos.ZczTreeDao;
import com.supconit.kqfx.web.fxzf.search.entities.ZczTree;

/**
 * 治超站树Dao
 * @author dell
 *
 */
@Repository("kqfxzf_kqfx_zczTree_dao")
public class ZczTreeDaoImpl extends AbstractBasicDaoImpl<ZczTree, Long> implements ZczTreeDao{

	private final static String NAMESPACE=ZczTree.class.getName();
	
	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}
	@Override
	public ZczTree getById(Long arg0) {
		return selectOne("getById", arg0);
	}
	@Override
	public List<ZczTree> getAllNodes(ZczTree node) {
		return selectList("getAllNodes",node);
	}

	

}
