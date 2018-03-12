package com.supconit.kqfx.web.analysis.dao;

import java.util.List;

import com.supconit.kqfx.web.analysis.entities.JgZcd;

import hc.orm.BasicDao;

public interface JgZcdDao extends BasicDao<JgZcd, String> {

	List<JgZcd> getJgZcdList();

	List<JgZcd> getZcdListByAuth(Long jgid);

	JgZcd getByZczId(String detectStation);

	List<JgZcd> getZtreeNodeListByAuth(Long jgid);

}
