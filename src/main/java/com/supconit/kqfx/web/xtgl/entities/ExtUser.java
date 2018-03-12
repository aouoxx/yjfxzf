package com.supconit.kqfx.web.xtgl.entities;

import com.supconit.honeycomb.business.authorization.entities.User;

public class ExtUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6938078770258652528L;
	
	
	
	public ExtUser(){
		super();
	}
	
	public ExtUser(Long id) {
		super();
		this.id = id;
	}

	public ExtUser(String username){
		super();
		this.username = username;
	}
	
	
	private String personName;
	private String roleName;
	

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	
	
	
	
}
