package com.supconit.kqfx.web.analysis.entities;

import com.supconit.kqfx.web.xtgl.entities.Jgxx;

import hc.base.domains.PK;

public class JgZcd implements PK<String>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	/**
	 * 机构ID
	 */
	private Long jgid;
	
	/**
	 * 治超站点
	 */
	private String deteStation;
	
	/**
	 * 机构信息
	 */
	private Jgxx jgxx;
	
	/**
	 * 治超站名称
	 */
	private String deteStationName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getJgid() {
		return jgid;
	}

	public void setJgid(Long jgid) {
		this.jgid = jgid;
	}

	public String getDeteStation() {
		return deteStation;
	}

	public void setDeteStation(String deteStation) {
		this.deteStation = deteStation;
	}

	public Jgxx getJgxx() {
		return jgxx;
	}

	public void setJgxx(Jgxx jgxx) {
		this.jgxx = jgxx;
	}

	public String getDeteStationName() {
		return deteStationName;
	}

	public void setDeteStationName(String deteStationName) {
		this.deteStationName = deteStationName;
	}
}
