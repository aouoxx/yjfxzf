package com.supconit.kqfx.web.analysis.entities;

import java.util.Date;

import hc.base.domains.PK;

/**
 * 非现执法每日按类型统计实体
 * @author cjm
 *
 */
public class DayReport implements PK<String> {
	
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
	 * 治超点
	 */
	private String detectStation;
	
	/**
	 * 超重等级
	 */
	private Integer overloadStatus;
	
	/**
	 * 统计次数
	 */
	private Long reportTimes;
	
	/**
	 * 统计日期
	 */
	private Date tjDate;
	
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	/**==========虚拟字段===========***/
	/**
	 * 图表类型(0为饼图,1为柱状图)
	 */
	private Integer chart;
	
	/**
	 * 违法程度字典名称
	 */
	private String overloadStatusStr;
	
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
	 * 治超站2的车流量信息
	 */
	private Integer detectOne;
	
	/**
	 * 治超站3的车流量信息
	 */
	private Integer detectTwo;
	
	/**
	 * 治超站4的车流量信息
	 */
	private Integer detectThree;
	
	/**
	 * 治超站5的车流量信息
	 */
	private Integer detectFour;
	
	/**
	 * 治超站汇总的车流量信息
	 */
	private Integer detectFive;
	
	/**
	 * 统计时间字符串
	 */
	private String tjDateStr;
	
	/**
	 * 治超站1的车流量信息
	 */
	private Integer detectTotal;

	/**
	 * 违法程度查询条件
	 */
	private Integer[] illegals;

	/**
	 * 不同治超站违法程度的和
	 */
	private Integer total;
	
	
	/**
	 *桥梁数据 
	 */
	private Integer bridgeOne;
	
	private Integer bridgeTwo;
	
	/**
	 * 虚拟字段违法程度 
	 */
	private Integer overLoadNormal;
	private Integer overLoadQingWei;
	private Integer overLoadYiBan;
	private Integer overLoadJiaoZhong;
	private Integer overLoadYanzhong;
	private Integer overLoadTebieYanZhong;

	/**
	 * 违法总和
	 */
	private Integer overloadTotal;
	/**
	 * 治超站名称
	 */
	private String detectStationName;
	
//	private Integer[] illegals;
	
	
	
	

	public Integer getBridgeOne() {
		return bridgeOne;
	}

	public Integer getOverLoadNormal() {
		return overLoadNormal;
	}

	public void setOverLoadNormal(Integer overLoadNormal) {
		this.overLoadNormal = overLoadNormal;
	}

	public Integer getOverLoadQingWei() {
		return overLoadQingWei;
	}

	public void setOverLoadQingWei(Integer overLoadQingWei) {
		this.overLoadQingWei = overLoadQingWei;
	}

	public Integer getOverLoadYiBan() {
		return overLoadYiBan;
	}

	public void setOverLoadYiBan(Integer overLoadYiBan) {
		this.overLoadYiBan = overLoadYiBan;
	}

	public Integer getOverLoadJiaoZhong() {
		return overLoadJiaoZhong;
	}

	public void setOverLoadJiaoZhong(Integer overLoadJiaoZhong) {
		this.overLoadJiaoZhong = overLoadJiaoZhong;
	}

	public Integer getOverLoadYanzhong() {
		return overLoadYanzhong;
	}

	public void setOverLoadYanzhong(Integer overLoadYanzhong) {
		this.overLoadYanzhong = overLoadYanzhong;
	}

	public Integer getOverLoadTebieYanZhong() {
		return overLoadTebieYanZhong;
	}

	public void setOverLoadTebieYanZhong(Integer overLoadTebieYanZhong) {
		this.overLoadTebieYanZhong = overLoadTebieYanZhong;
	}

	public void setBridgeOne(Integer bridgeOne) {
		this.bridgeOne = bridgeOne;
	}

	public Integer getBridgeTwo() {
		return bridgeTwo;
	}

	public void setBridgeTwo(Integer bridgeTwo) {
		this.bridgeTwo = bridgeTwo;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer[] getIllegals() {
		return illegals;
	}

	public void setIllegals(Integer[] illegals) {
		this.illegals = illegals;
	}

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

	public String getDetectStation() {
		return detectStation;
	}

	public void setDetectStation(String detectStation) {
		this.detectStation = detectStation;
	}

	public Integer getOverloadStatus() {
		return overloadStatus;
	}

	public void setOverloadStatus(Integer overloadStatus) {
		this.overloadStatus = overloadStatus;
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

	public Integer getChart() {
		return chart;
	}

	public void setChart(Integer chart) {
		this.chart = chart;
	}

	public String getOverloadStatusStr() {
		return overloadStatusStr;
	}

	public void setOverloadStatusStr(String overloadStatusStr) {
		this.overloadStatusStr = overloadStatusStr;
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

	public Integer getOverloadTotal() {
		return overloadTotal;
	}

	public void setOverloadTotal(Integer overloadTotal) {
		this.overloadTotal = overloadTotal;
	}

	public String getDetectStationName() {
		return detectStationName;
	}

	public void setDetectStationName(String detectStationName) {
		this.detectStationName = detectStationName;
	}
	
}
