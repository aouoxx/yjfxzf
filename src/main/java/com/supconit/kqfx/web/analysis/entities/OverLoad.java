package com.supconit.kqfx.web.analysis.entities;

import java.util.Date;

import hc.base.domains.PK;

/**
 * 统计分析每日车辆超限统计实体
 * @author cjm
 *
 */
public class OverLoad implements PK<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主鍵
	 */
	private String id;
	
	/**
	 * 治超站
	 */
	private String detectStation;
	
	/**
	 * 超重等级(数据字典类似于>=50t)
	 */
	private String overload;
	
	/**
	 * 统计次数
	 */
	private Long cars;
	
	/**
	 * 统计日期
	 */
	private Date tjDate;
	
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	/**
	 * 虚拟字段
	 */
	private String flag;
	
	private Integer over55;
	
	private Integer over70;
	
	private Integer over100;
	
	private Integer overTotal;
	
	private String station;
	
	private String[] detects;
	
	
	
	
	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String[] getDetects() {
		return detects;
	}

	public void setDetects(String[] detects) {
		this.detects = detects;
	}

	public Integer getOverTotal() {
		return overTotal;
	}

	public void setOverTotal(Integer overTotal) {
		this.overTotal = overTotal;
	}

	public Integer getOver55() {
		return over55;
	}

	public void setOver55(Integer over55) {
		this.over55 = over55;
	}

	public Integer getOver70() {
		return over70;
	}

	public void setOver70(Integer over70) {
		this.over70 = over70;
	}

	public Integer getOver100() {
		return over100;
	}

	public void setOver100(Integer over100) {
		this.over100 = over100;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDetectStation() {
		return detectStation;
	}

	public void setDetectStation(String detectStation) {
		this.detectStation = detectStation;
	}

	public String getOverload() {
		return overload;
	}

	public void setOverload(String overload) {
		this.overload = overload;
	}

	public Long getCars() {
		return cars;
	}

	public void setCars(Long cars) {
		this.cars = cars;
	}

	public Date getTjDate() {
		return tjDate;
	}

	public void setTjDate(Date tjDate) {
		this.tjDate = tjDate;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
