package com.supconit.kqfx.web.analysis.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.analysis.dao.JgZcdDao;
import com.supconit.kqfx.web.analysis.entities.JgZcd;

@Repository("analysis_jgzcd_dao")
public class JgZcdDaoImpl extends AbstractBasicDaoImpl<JgZcd, String> implements
		JgZcdDao {
	
	private static final String NAMESPACE = JgZcd.class.getName();

	@Override
	protected String getNamespace() {
		
		return NAMESPACE;
	}

	@Override
	public List<JgZcd> getJgZcdList() {
		
		return selectList("getJgZcdList");
	}

	@Override
	public List<JgZcd> getZcdListByAuth(Long jgid) {
		
		return selectList("getZcdListByAuth",jgid);
	}

	@Override
	public JgZcd getByZczId(String detectStation) {
		
		return selectOne("getByZczId", detectStation);
	}

	@Override
	public List<JgZcd> getZtreeNodeListByAuth(Long jgid) {
		
		return selectList("getZtreeNodeListByAuth",jgid);
	}

}
