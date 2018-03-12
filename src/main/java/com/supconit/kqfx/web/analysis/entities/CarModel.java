package com.supconit.kqfx.web.analysis.entities;

import java.util.Date;

import hc.base.domains.PK;

/**
 * 统计分析每日车辆类型统计实体
 * @author cjm
 *
 */
public class CarModel implements PK<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	/**
	 * 治超站
	 */
	private String detectStation;
	
	/**
	 * 轴数
	 */
	private Integer axisCount;
	
	/**
	 * 车辆类型
	 */
	private Integer carModel;
	
	/**
	 * 统计次数
	 */
	private Long reportTimes;
	
	/**
	 * 统计日期
	 */
	private Date tjDate;
	
	/**
	 * 更新日期
	 */
	private Date updateTime;
	
	
	//查询虚拟字段
	/**
	 * 开始时间
	 */
	private Date beginDate;
	
	/**
	 * 结束时间
	 */
	private Date endDate;
	
	/**
	 * 治超站点数组
	 */
	private String[] detects;
	
	
	/**
	 * 两轴车数量信息
	 */
	private Integer axisTwo;
	
	/**
	 * 三轴车数量信息
	 */
	private Integer axisThree;
	
	/**
	 * 四轴车数量信息
	 */
	private Integer axisFour;
	
	/**
	 * 五轴车数量信息
	 */
	private Integer axisFive;
	
	/**
	 * 六轴车数量信息
	 */
	private Integer axisSix;
	
	/**
	 * 六轴以上车数量信息
	 */
	private Integer axisSeven;
	
	/**
	 * 车辆总数
	 */
	private Integer axisTotal;
	
	public Integer getAxisTwo() {
		return axisTwo;
	}

	public void setAxisTwo(Integer axisTwo) {
		this.axisTwo = axisTwo;
	}

	public Integer getAxisThree() {
		return axisThree;
	}

	public void setAxisThree(Integer axisThree) {
		this.axisThree = axisThree;
	}

	public Integer getAxisFour() {
		return axisFour;
	}

	public void setAxisFour(Integer axisFour) {
		this.axisFour = axisFour;
	}

	public Integer getAxisFive() {
		return axisFive;
	}

	public void setAxisFive(Integer axisFive) {
		this.axisFive = axisFive;
	}

	public Integer getAxisSix() {
		return axisSix;
	}

	public void setAxisSix(Integer axisSix) {
		this.axisSix = axisSix;
	}

	public Integer getAxisSeven() {
		return axisSeven;
	}

	public void setAxisSeven(Integer axisSeven) {
		this.axisSeven = axisSeven;
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

	public Integer getAxisCount() {
		return axisCount;
	}

	public void setAxisCount(Integer axisCount) {
		this.axisCount = axisCount;
	}

	public Integer getCarModel() {
		return carModel;
	}

	public void setCarModel(Integer carModel) {
		this.carModel = carModel;
	}

	public Long getReportTimes() {
		return reportTimes;
	}

	public void setReportTimes(Long reportTimes) {
		this.reportTimes = reportTimes;
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

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String[] getDetects() {
		return detects;
	}

	public void setDetects(String[] detects) {
		this.detects = detects;
	}

	public Integer getAxisTotal() {
		return axisTotal;
	}

	public void setAxisTotal(Integer axisTotal) {
		this.axisTotal = axisTotal;
	}
}
