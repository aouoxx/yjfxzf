package com.supconit.kqfx.web.fxzf.warn.entities;

import hc.base.domains.PK;

import com.supconit.kqfx.web.fxzf.qbb.entities.QbbMbxx;

public class WarnInfo implements PK<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private String id;
	
	/**
	 * 告警模板类型
	 */
	private String warnType;
//	private String templetType;
	
	/**
	 * 对应的治超站
	 */
	private String detectStation;
	
	/**
	 * 情报板模板ID
	 */
	private String qbbTempletId;
	
	/**
	 * 情报板内容
	 */
	private String 	templetInfo;
	
	/**
	 * 删除标志位
	 */
	private Integer deleted;
	
	/**
	 * 情报板模板信息
	 */
	private QbbMbxx qbbmbxx;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public String getTempletType() {
//		return templetType;
//	}
//
//	public void setTempletType(String templetType) {
//		this.templetType = templetType;
//	}

	public String getQbbTempletId() {
		return qbbTempletId;
	}

	public void setQbbTempletId(String qbbTempletId) {
		this.qbbTempletId = qbbTempletId;
	}

	public String getTempletInfo() {
		return templetInfo;
	}

	public void setTempletInfo(String templetInfo) {
		this.templetInfo = templetInfo;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public QbbMbxx getQbbmbxx() {
		return qbbmbxx;
	}

	public void setQbbmbxx(QbbMbxx qbbmbxx) {
		this.qbbmbxx = qbbmbxx;
	}

	public String getDetectStation() {
		return detectStation;
	}

	public void setDetectStation(String detectStation) {
		this.detectStation = detectStation;
	}

	public String getWarnType() {
		return warnType;
	}

	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}
	
	
}
