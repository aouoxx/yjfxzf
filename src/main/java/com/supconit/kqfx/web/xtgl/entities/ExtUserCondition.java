package com.supconit.kqfx.web.xtgl.entities;

import com.supconit.honeycomb.business.organization.entities.Department;

public class ExtUserCondition extends ExtUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 84176531394055411L;
	
	protected Boolean			notAssigned;
	protected Long				roleId;
	protected Long				notRoleId;
	protected Long				departmentId;
	protected Long				positionId;
	protected Department		department;

	protected Long jgbh;
	
	protected Jgxx jgxx;

	
	
	
	public Boolean getNotAssigned() {
		return notAssigned;
	}

	public void setNotAssigned(Boolean notAssigned) {
		this.notAssigned = notAssigned;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getNotRoleId() {
		return notRoleId;
	}

	public void setNotRoleId(Long notRoleId) {
		this.notRoleId = notRoleId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
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
	
}
