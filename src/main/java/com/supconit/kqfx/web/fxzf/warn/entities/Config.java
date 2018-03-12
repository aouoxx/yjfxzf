package com.supconit.kqfx.web.fxzf.warn.entities;

/**
 * 系统配置项实体类
 * @author cjm
 *
 */
public class Config implements hc.base.domains.PK<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	private  String id; 
	
	/**
	 * 配置项名称
	 */
	private String name;
	
	/**
	 * 配置项编码
	 */
	private String code;
	
	/**
	 * 配置项数值
	 */
	private Double value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	
}
