package com.supconit.kqfx.web.fxzf.warn.entities;

import hc.base.domains.PK;

import java.util.Date;

import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;

/**
 * 告警历史实体创建
 * @author gaoshuo
 */
public class WarnHistory implements  PK<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8909701814340357083L;
	
	/**
	 * ID
	 */
	private String id;
	
	/**
	 * 告警类型(数据字典 1为情报板告警 2为WEB端告警 3为APP告警)
	 */
	private Integer warnType;
	
	/**
	 * 车牌号
	 */
	private String license;
	
	/**
	 * 车牌颜色
	 */
	private String plateColor;
	
	/**
	 * 告警内容
	 */
	private String warnInfo;
	
	/**
	 * 告警时间
	 */
	private Date warnTime;
	
	/**
	 * 删除标志位
	 */
	private Integer deleted;
	
	/**
	 * 非现数据关联ID
	 */
	private String fxzfId;
	
	/**
	 * 非现对象
	 */
	private Fxzf fxzf;
	
	/**
	 * 虚拟变量
	 */

	private Date backTime;
	
	private String overType;
	
	private Integer[] type;
	
	
	//统计分析虚拟字段
	/**
	 * 开始时间
	 */
	private Date beginDate;
	
	/**
	 * 结束时间
	 */
	private Date endDate;
	
	/**
	 * 治超站查询条件
	 */
	private String[] detects;
	
	/**
	 * 治超站名称
	 */
	private String detectStation;
	
	/**
	 * 治超站1的告警信息
	 */
	private Integer detectOne;
	
	/**
	 * 治超站2的告警信息
	 */
	private Integer detectTwo;
	
	/**
	 * 治超站3的告警信息
	 */
	private Integer detectThree;
	
	/**
	 * 治超站4的告警信息
	 */
	private Integer detectFour;
	
	/**
	 * 治超站5的告警信息
	 */
	private Integer detectFive;
	
	/**
	 * 汇总的告警信息
	 */
	private Integer detectTotal;
	
	/**
	 * 统计时间字符串
	 */
	private String tjDateStr;
	
	private String flag;
	
	

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getBackTime() {
		return backTime;
	}

	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}

	public String getOverType() {
		return overType;
	}

	public void setOverType(String overType) {
		this.overType = overType;
	}

	public Integer[] getType() {
		return type;
	}

	public void setType(Integer[] type) {
		this.type = type;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getWarnType() {
		return warnType;
	}

	public void setWarnType(Integer warnType) {
		this.warnType = warnType;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(String plateColor) {
		this.plateColor = plateColor;
	}

	public String getWarnInfo() {
		return warnInfo;
	}

	public void setWarnInfo(String warnInfo) {
		this.warnInfo = warnInfo;
	}

	public Date getWarnTime() {
		return warnTime;
	}

	public void setWarnTime(Date warnTime) {
		this.warnTime = warnTime;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getFxzfId() {
		return fxzfId;
	}

	public void setFxzfId(String fxzfId) {
		this.fxzfId = fxzfId;
	}

	public Fxzf getFxzf() {
		return fxzf;
	}

	public void setFxzf(Fxzf fxzf) {
		this.fxzf = fxzf;
	}

	public String getDetectStation() {
		return detectStation;
	}

	public void setDetectStation(String detectStation) {
		this.detectStation = detectStation;
	}

	public Integer getDetectOne() {
		return detectOne;
	}

	public void setDetectOne(Integer detectOne) {
		this.detectOne = detectOne;
	}

	public Integer getDetectTwo() {
		return detectTwo;
	}

	public void setDetectTwo(Integer detectTwo) {
		this.detectTwo = detectTwo;
	}

	public Integer getDetectThree() {
		return detectThree;
	}

	public void setDetectThree(Integer detectThree) {
		this.detectThree = detectThree;
	}

	public Integer getDetectFour() {
		return detectFour;
	}

	public void setDetectFour(Integer detectFour) {
		this.detectFour = detectFour;
	}

	public Integer getDetectFive() {
		return detectFive;
	}

	public void setDetectFive(Integer detectFive) {
		this.detectFive = detectFive;
	}

	public Integer getDetectTotal() {
		return detectTotal;
	}

	public void setDetectTotal(Integer detectTotal) {
		this.detectTotal = detectTotal;
	}

	public String getTjDateStr() {
		return tjDateStr;
	}

	public void setTjDateStr(String tjDateStr) {
		this.tjDateStr = tjDateStr;
	}
	
	

}
