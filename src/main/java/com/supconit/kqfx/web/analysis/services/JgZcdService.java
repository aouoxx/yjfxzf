package com.supconit.kqfx.web.analysis.services;

import hc.orm.BasicOrmService;

import java.util.List;

import com.supconit.kqfx.web.analysis.entities.JgZcd;
import com.supconit.kqfx.web.fxzf.monitor.entities.ZTreeNode;

public interface JgZcdService extends BasicOrmService<JgZcd, String> {
	
	/**
	 * 根据当前登录人员获取对应的机构权限
	 * @param user
	 * @return
	 */
	List<JgZcd> getJgZcdListByAuth();
	
	/**
	 * 根据当前登录人员获取对应的治超站权限
	 * @param user
	 * @return
	 */
	List<JgZcd> getZcdListByAuth();
	
	List<JgZcd> getByJgid(Long jgid);

	JgZcd getByZczId(String detectStation);
	
	/**
	 * 根据当前登录人员获取对应的Jgid
	 * @return
	 */
	Long getJgIdByAuth();
	
	/**
	 * 根据用户权限获取对应的治超点、机构信息
	 * @return
	 */
	List<ZTreeNode> getZtreeNodeByAuth();
	
}
