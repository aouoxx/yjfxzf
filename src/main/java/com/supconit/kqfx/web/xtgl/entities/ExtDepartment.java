package com.supconit.kqfx.web.xtgl.entities;

import hc.orm.search.SQLTerm;
import hc.orm.search.Term;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import com.supconit.honeycomb.business.organization.entities.Department;

public class ExtDepartment extends Department {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2621782527457989023L;
	
	
	/** 所属机构ID */
	protected Long jgbh;
	
	/** 所属机构信息 */
	protected Jgxx jgxx;
	
	private String jgxxJgmc;
	private String parentJgmc;
	private String parentJgId;
	
	/**
	 * 
	 */
	public ExtDepartment(){
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public ExtDepartment(Long id){
		setId(id);
	}
	
	public Long getJgbh() {
		return jgbh;
	}

	public void setJgbh(Long jgbh) {
		this.jgbh = jgbh;
	}

	
	public Jgxx getJgxx() {
		return jgxx;
	}

	public void setJgxx(Jgxx jgxx) {
		this.jgxx = jgxx;
	}

	
	
	
	public String getJgxxJgmc() {
		return jgxxJgmc;
	}

	public void setJgxxJgmc(String jgxxJgmc) {
		this.jgxxJgmc = jgxxJgmc;
	}

	public String getParentJgmc() {
		return parentJgmc;
	}

	public void setParentJgmc(String parentJgmc) {
		this.parentJgmc = parentJgmc;
	}

	
	
	public String getParentJgId() {
		return parentJgId;
	}

	public void setParentJgId(String parentJgId) {
		this.parentJgId = parentJgId;
	}

	@Override
	public Term getTerm(String prefix) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		if (null != this.getId()) {
			buildSQLAnd(sql, prefix, "ID");
			params.add(this.getId());
		}
		if (null != this.getPid() && this.getPid() > 0) {
			buildSQLAnd(sql, prefix, "PID");
			params.add(this.getPid());
		}
		if (StringUtil.isNotBlank(this.getCode())) {
			buildSQLLike(sql, prefix, "CODE");
			params.add("%" + this.getCode() + "%");
		}
		if (StringUtil.isNotBlank(this.getName())) {
			buildSQLLike(sql, prefix, "NAME");
			params.add("%" + this.getName() + "%");
		}
		if(null != this.getJgbh() && this.getJgbh() > 1){
			buildSQLAnd(sql, prefix, "JGBH");
			params.add(this.getJgbh());
		}
		if (sql.indexOf("AND ") == 0) sql.delete(0, 3);
		SQLTerm term = new SQLTerm(sql.toString());
		term.setParams(params.toArray());
		return term;
	}
	
	
}
