package com.supconit.kqfx.web.xtgl.entities;

import java.util.HashMap;

import com.supconit.honeycomb.business.authorization.entities.User;
						

/**
 * @author 
 * @create 2014-05-06 15:21:36 
 * @since 
 * 
 */
public class OnLineUser implements hc.base.domains.PK<String>{

	private static final long	serialVersionUID	= 1L;
		
	private String id;		
	private String personId;	
	private String userName;	
	private String description;	
	private String name;
	private String email;	
	private String ryzw;		
	private String rysjhm;	
	private String jgmc;
	private String ip;
	
	private HashMap<String,User> onLineUserMap;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRyzw() {
		return ryzw;
	}

	public void setRyzw(String ryzw) {
		this.ryzw = ryzw;
	}

	public String getRysjhm() {
		return rysjhm;
	}

	public void setRysjhm(String rysjhm) {
		this.rysjhm = rysjhm;
	}

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public HashMap<String, User> getOnLineUserMap() {
		return onLineUserMap;
	}

	public void setOnLineUserMap(HashMap<String, User> onLineUserMap) {
		this.onLineUserMap = onLineUserMap;
	}
	
    
}