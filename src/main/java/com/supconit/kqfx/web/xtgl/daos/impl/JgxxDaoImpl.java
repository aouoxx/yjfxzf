package com.supconit.kqfx.web.xtgl.daos.impl;

import hc.orm.AbstractBasicDaoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.xtgl.daos.JgxxDao;
import com.supconit.kqfx.web.xtgl.entities.Jgxx;

@Repository
public class JgxxDaoImpl extends AbstractBasicDaoImpl<Jgxx, Long> implements JgxxDao {
	private static final String	NAMESPACE	= Jgxx.class.getName();

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}


	@Override
	public List<Jgxx> selectAllJgxx(Jgxx condition) {
		
		return this.selectList("selectAllJgxx",condition);
	}

}