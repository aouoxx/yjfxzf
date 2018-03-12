package com.supconit.kqfx.web.fxzf.search.entities;

import hc.base.domains.OrderPart;

import java.util.Date;

public class FxzfLane  extends hc.base.domains.StringId implements
hc.base.domains.Orderable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 治超点
	 */
	private String detectStation;
	/**
	 * 车道号
	 */
	private Integer lane;
	/**
	 * 总重（KG）
	 */
	private Double weight;
	/**
	 * 速度(km/h)
	 */
	private Double speed;
	/**
	 * 轴数
	 */
	private Integer axisCount;
	/**
	 * 超重等级
	 */
	private Integer overLoadStatus;
	/**
	 * 超重率
	 */
	private Double overLoadPercent;
	/**
	 * 车辆类型
	 */
	private Integer carModel;
	/**
	 * 过车时间
	 */
	private Date captureTime;
	/**
	 * 车牌号
	 */
	private String license;
	/**
	 * 车牌颜色
	 */
	private String licenseColor;

	/**
	 * 车头车牌号图片地址
	 */
	private String headLicensePicdir;
	/**
	 * 车头全车图片地址
	 */
	private String headCarPicdir;
	/**
	 * 车尾车牌号图片地址
	 */
	private String tailLicensePicdir;
	/**
	 * 车尾全车图片地址
	 */
	private String tailCarPicdir;
	/**
	 * 行车方向
	 */
	private Integer carDirection;
	/**
	 * 超限处罚(审核节点)
	 */
	private Integer overLoadPunish;
	/**
	 * 处罚单ID
	 */
	private String punishId;
	/**
	 * 处罚意见
	 */
	private String punishOpintion;
	/**
	 * 不通过原因
	 */
	private String punishReason;
	/**
	 * 图片是否存在
	 */
	private Integer picFlag;
	
	/**
	 * 情报板
	 */
	private  String xxnr;
	
	/**
	 * 超重等级
	 */
	private String overLoadString;
	
	
	/**
	 * 权限关联的治超站
	 * @return
	 */
	private String[] stationStrings;
	
	
	public String getDetectStation() {
		return detectStation;
	}

	public void setDetectStation(String detectStation) {
		this.detectStation = detectStation;
	}

	public Integer getLane() {
		return lane;
	}

	public void setLane(Integer lane) {
		this.lane = lane;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Integer getAxisCount() {
		return axisCount;
	}

	public void setAxisCount(Integer axisCount) {
		this.axisCount = axisCount;
	}

	public Integer getOverLoadStatus() {
		return overLoadStatus;
	}

	public void setOverLoadStatus(Integer overLoadStatus) {
		this.overLoadStatus = overLoadStatus;
	}

	public Double getOverLoadPercent() {
		return overLoadPercent;
	}

	public void setOverLoadPercent(Double overLoadPercent) {
		this.overLoadPercent = overLoadPercent;
	}

	public Integer getCarModel() {
		return carModel;
	}

	public void setCarModel(Integer carModel) {
		this.carModel = carModel;
	}

	public Date getCaptureTime() {
		return captureTime;
	}

	public void setCaptureTime(Date captureTime) {
		this.captureTime = captureTime;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getLicenseColor() {
		return licenseColor;
	}

	public void setLicenseColor(String licenseColor) {
		this.licenseColor = licenseColor;
	}
	public String getHeadLicensePicdir() {
		return headLicensePicdir;
	}

	public void setHeadLicensePicdir(String headLicensePicdir) {
		this.headLicensePicdir = headLicensePicdir;
	}

	public String getHeadCarPicdir() {
		return headCarPicdir;
	}

	public void setHeadCarPicdir(String headCarPicdir) {
		this.headCarPicdir = headCarPicdir;
	}

	public String getTailLicensePicdir() {
		return tailLicensePicdir;
	}

	public void setTailLicensePicdir(String tailLicensePicdir) {
		this.tailLicensePicdir = tailLicensePicdir;
	}

	public String getTailCarPicdir() {
		return tailCarPicdir;
	}

	public void setTailCarPicdir(String tailCarPicdir) {
		this.tailCarPicdir = tailCarPicdir;
	}

	public Integer getCarDirection() {
		return carDirection;
	}

	public void setCarDirection(Integer carDirection) {
		this.carDirection = carDirection;
	}

	public Integer getOverLoadPunish() {
		return overLoadPunish;
	}

	public void setOverLoadPunish(Integer overLoadPunish) {
		this.overLoadPunish = overLoadPunish;
	}

	public String getPunishId() {
		return punishId;
	}

	public void setPunishId(String punishId) {
		this.punishId = punishId;
	}

	public String getPunishOpintion() {
		return punishOpintion;
	}

	public void setPunishOpintion(String punishOpintion) {
		this.punishOpintion = punishOpintion;
	}

	public String getPunishReason() {
		return punishReason;
	}

	public void setPunishReason(String punishReason) {
		this.punishReason = punishReason;
	}

	public Integer getPicFlag() {
		return picFlag;
	}

	public void setPicFlag(Integer picFlag) {
		this.picFlag = picFlag;
	}

	

	public String getXxnr() {
		return xxnr;
	}

	public void setXxnr(String xxnr) {
		this.xxnr = xxnr;
	}

	public String getOverLoadString() {
		return overLoadString;
	}

	public void setOverLoadString(String overLoadString) {
		this.overLoadString = overLoadString;
	}
	
	


	public String[] getStationStrings() {
		return stationStrings;
	}

	public void setStationStrings(String[] stationStrings) {
		this.stationStrings = stationStrings;
	}

	@Override
	public OrderPart[] getOrderParts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOrderParts(OrderPart[] orderParts) {
		// TODO Auto-generated method stub
		
	}

}
