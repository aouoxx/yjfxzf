package com.supconit.kqfx.web.fxzf.search.entities;

import hc.base.domains.OrderPart;
import hc.base.domains.Orderable;
import hc.base.domains.StringId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.supconit.kqfx.web.fxzf.avro.redis_FxzfInfo;
import com.supconit.kqfx.web.util.DateUtil;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.StakeUtil;

/**
 * 静态称重数据实体类
 * 
 * @author JNJ
 * @time 2016年12月8日
 * 
 */
public class FxzfStatic extends StringId implements Orderable {

	private static final long serialVersionUID = 615079232429044806L;

	private static final Logger logger = LoggerFactory.getLogger(FxzfStatic.class);

	/** 车道号 */
	private Integer lane;
	/** 总重(KG) */
	private Double weight;
	/** 速度(km/h) */
	private Double speed;
	/** 轴数 */
	private Integer axisCount;
	/** 轴长 */
	private String axisLength;
	/** 各个轴重 */
	private String[] axisWeight;
	/** 轴重 */
	private String axisWeightTotal;
	/** 超重等级 */
	private Integer overLoadStatus;
	/** 超重率 */
	private Double overLoadPercent;
	/** 车辆类型 */
	private Integer carModel;
	/** 过车时间 */
	private Date captureTime;

	/** 正确的车牌图 */
	private String license;
	/** 正确的车牌颜色 */
	private String licenseColor;

	/** 正确的车辆图片 */
	private String carPicdir;
	/** 正确的车牌图片 */
	private String licensePicdir;

	/** 车头车牌号 */
	private String headLicense;
	/** 车头车牌置信度 */
	private Double headLicenseZxdD;
	/** 车头车牌颜色 */
	private String headLicenseColor;

	/** 车头车牌号图片地址 */
	private String headLicensePicdir;
	/** 车头全车图片地址 */
	private String headCarPicdir;

	/** 车尾车牌号 */
	private String tailLicense;
	/** 车尾车牌置信度 */
	private Double tailLicenseZxdD;
	/** 车尾车牌颜色 */
	private String tailLicenseColor;

	/** 车尾车牌号图片地址 */
	private String tailLicensePicdir;
	/** 车尾全车图片地址 */
	private String tailCarPicdir;

	/** 长度 */
	private Double length;
	/** 宽度 */
	private Double width;
	/** 高度 */
	private Double height;
	/** 是否已经传送的标志 0：表示未传送 1：表示已传送 */
	private Integer transport;
	/** 传送完成的时间 */
	private Date transportTime;

	/** 行车方向 */
	private Integer carDirection;
	/** 超限处罚(审核节点) */
	private Integer overLoadPunish;
	/** 处罚单ID */
	private String punishId;
	/** 处罚意见 */
	private String punishOpintion;
	/** 不通过原因 */
	private String punishReason;
	/** 图片是否存在 */
	private Integer picFlag;

	/** 治超站标志 */
	private String detectStationFlag;
	/** 是否为告警信息 */
	private Integer warnFlag;

	/** 超限量 */
	private Double overLoad;

	/** 虚拟字段 */
	private Date backTime;
	private String[] detects;
	private String overStatus;
	private Integer[] status;
	private String overPunish;
	private Integer[] punish;
	private String overLoadFlag;

	// 监控页面虚拟字段(情报板信息内容)
	private String xxnr;
	// 告警类型
	private String warnType;
	// 开始重量
	private Double beginWeight;
	// 结束重量
	private Double endWeight;
	/** 开始车速 */
	private Double beginSpeed;
	/** 结束车速 */
	private Double endSpeed;
	/** 开始超限率 */
	private Double beginOverLoadPercent;
	/** 截止超限率 */
	private Double endOverLoadPercent;

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

	public String getAxisLength() {
		return axisLength;
	}

	public void setAxisLength(String axisLength) {
		this.axisLength = axisLength;
	}

	public String[] getAxisWeight() {
		return axisWeight;
	}

	public void setAxisWeight(String[] axisWeight) {
		this.axisWeight = axisWeight;
	}

	public String getAxisWeightTotal() {
		return axisWeightTotal;
	}

	public void setAxisWeightTotal(String axisWeightTotal) {
		this.axisWeightTotal = axisWeightTotal;
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

	public String getCarPicdir() {
		return carPicdir;
	}

	public void setCarPicdir(String carPicdir) {
		this.carPicdir = carPicdir;
	}

	public String getLicensePicdir() {
		return licensePicdir;
	}

	public void setLicensePicdir(String licensePicdir) {
		this.licensePicdir = licensePicdir;
	}

	public String getHeadLicense() {
		return headLicense;
	}

	public void setHeadLicense(String headLicense) {
		this.headLicense = headLicense;
	}

	public Double getHeadLicenseZxdD() {
		return headLicenseZxdD;
	}

	public void setHeadLicenseZxdD(Double headLicenseZxdD) {
		this.headLicenseZxdD = headLicenseZxdD;
	}

	public String getHeadLicenseColor() {
		return headLicenseColor;
	}

	public void setHeadLicenseColor(String headLicenseColor) {
		this.headLicenseColor = headLicenseColor;
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

	public String getTailLicense() {
		return tailLicense;
	}

	public void setTailLicense(String tailLicense) {
		this.tailLicense = tailLicense;
	}

	public Double getTailLicenseZxdD() {
		return tailLicenseZxdD;
	}

	public void setTailLicenseZxdD(Double tailLicenseZxdD) {
		this.tailLicenseZxdD = tailLicenseZxdD;
	}

	public String getTailLicenseColor() {
		return tailLicenseColor;
	}

	public void setTailLicenseColor(String tailLicenseColor) {
		this.tailLicenseColor = tailLicenseColor;
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

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Integer getTransport() {
		return transport;
	}

	public void setTransport(Integer transport) {
		this.transport = transport;
	}

	public Date getTransportTime() {
		return transportTime;
	}

	public void setTransportTime(Date transportTime) {
		this.transportTime = transportTime;
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

	public String getDetectStationFlag() {
		return detectStationFlag;
	}

	public void setDetectStationFlag(String detectStationFlag) {
		this.detectStationFlag = detectStationFlag;
	}

	public Integer getWarnFlag() {
		return warnFlag;
	}

	public void setWarnFlag(Integer warnFlag) {
		this.warnFlag = warnFlag;
	}

	public Double getOverLoad() {
		return overLoad;
	}

	public void setOverLoad(Double overLoad) {
		this.overLoad = overLoad;
	}

	public Date getBackTime() {
		return backTime;
	}

	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}

	public String[] getDetects() {
		return detects;
	}

	public void setDetects(String[] detects) {
		this.detects = detects;
	}

	public String getOverStatus() {
		return overStatus;
	}

	public void setOverStatus(String overStatus) {
		this.overStatus = overStatus;
	}

	public Integer[] getStatus() {
		return status;
	}

	public void setStatus(Integer[] status) {
		this.status = status;
	}

	public String getOverPunish() {
		return overPunish;
	}

	public void setOverPunish(String overPunish) {
		this.overPunish = overPunish;
	}

	public Integer[] getPunish() {
		return punish;
	}

	public void setPunish(Integer[] punish) {
		this.punish = punish;
	}

	public String getOverLoadFlag() {
		return overLoadFlag;
	}

	public void setOverLoadFlag(String overLoadFlag) {
		this.overLoadFlag = overLoadFlag;
	}

	public String getXxnr() {
		return xxnr;
	}

	public void setXxnr(String xxnr) {
		this.xxnr = xxnr;
	}

	public String getWarnType() {
		return warnType;
	}

	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}

	public Double getBeginWeight() {
		return beginWeight;
	}

	public void setBeginWeight(Double beginWeight) {
		this.beginWeight = beginWeight;
	}

	public Double getEndWeight() {
		return endWeight;
	}

	public void setEndWeight(Double endWeight) {
		this.endWeight = endWeight;
	}

	public Double getBeginSpeed() {
		return beginSpeed;
	}

	public void setBeginSpeed(Double beginSpeed) {
		this.beginSpeed = beginSpeed;
	}

	public Double getEndSpeed() {
		return endSpeed;
	}

	public void setEndSpeed(Double endSpeed) {
		this.endSpeed = endSpeed;
	}

	public Double getBeginOverLoadPercent() {
		return beginOverLoadPercent;
	}

	public void setBeginOverLoadPercent(Double beginOverLoadPercent) {
		this.beginOverLoadPercent = beginOverLoadPercent;
	}

	public Double getEndOverLoadPercent() {
		return endOverLoadPercent;
	}

	public void setEndOverLoadPercent(Double endOverLoadPercent) {
		this.endOverLoadPercent = endOverLoadPercent;
	}

	/**
	 * 转译数据
	 * 
	 * @param info
	 * @return
	 */
	public static FxzfStatic translateFxzf(redis_FxzfInfo info) {

		FxzfStatic fxzf = new FxzfStatic();
		try {
			fxzf.setId(IDGenerator.idGenerator());

			fxzf.setLane(Integer.valueOf(String.valueOf(info.getSLane()).trim()));
			// 保留两位小数
			fxzf.setWeight(!StringUtil.isEmpty(String.valueOf(info.getSWeight()).trim()) ? StakeUtil.round(Double.valueOf(String.valueOf(info.getSWeight()).trim()), 2) : null);
			fxzf.setSpeed(!StringUtil.isEmpty(String.valueOf(info.getSSpeed()).trim()) ? Double.valueOf(String.valueOf(info.getSSpeed()).trim()) : null);
			fxzf.setAxisCount(Integer.valueOf(String.valueOf(info.getSAxisCount()).trim()));
			fxzf.setOverLoadStatus(Integer.valueOf(String.valueOf(info.getSOverloadStatus()).trim()));
			// 保留两位小数
			fxzf.setOverLoadPercent(!StringUtil.isEmpty(String.valueOf(info.getSOverloadPercent()).trim()) ? StakeUtil.round(
					Double.valueOf(String.valueOf(info.getSOverloadPercent()).trim()), 2) : null);
			fxzf.setCarModel(!StringUtil.isEmpty(String.valueOf(info.getSCarModel()).trim()) ? Integer.valueOf(String.valueOf(info.getSCarModel()).trim()) : 0);// 车辆类型
			// 如果时间为空则为系统时间
			fxzf.setCaptureTime(!StringUtil.isEmpty(String.valueOf(info.getSCaptureTime()).trim()) ? DateUtil.formatStringToDate(String.valueOf(info.getSCaptureTime()).trim(),
					DateUtil.DATE_FORMAT_TIME_AVRO) : new Date());
			if (info.getSHeadLicense() == null)

				logger.info("==========车头信息转换");
			fxzf.setHeadLicense(String.valueOf(info.getSHeadLicense()).trim());
			fxzf.setHeadLicenseZxdD(Double.valueOf(String.valueOf(info.getSHeadLicenseZxd()).trim()));
			fxzf.setHeadLicenseColor(String.valueOf(info.getSHeadLicenseColor()).trim());
			fxzf.setHeadLicensePicdir("today/" + String.valueOf(info.getSHeadLicensePicDir()).trim());
			fxzf.setHeadCarPicdir("today/" + String.valueOf(info.getSHeadCarPicDir()).trim());

			logger.info("==========车尾信息转换");
			fxzf.setTailLicense(String.valueOf(info.getSTailLicense()).trim());
			fxzf.setTailLicenseZxdD(Double.valueOf(String.valueOf(info.getSTailLicenseZxd()).trim()));
			fxzf.setTailLicenseColor(String.valueOf(String.valueOf(info.getSTailLicenseColor()).trim()));
			fxzf.setTailLicensePicdir("today/" + String.valueOf(info.getSTailLicensePicDir()).trim());
			fxzf.setTailCarPicdir("today/" + String.valueOf(info.getSTailCarPicDir()).trim());

			logger.info("==========车辆限度转换");
			fxzf.setLength(Double.valueOf(String.valueOf(info.getSCarLength()).trim()) + 0);
			fxzf.setWidth(Double.valueOf(String.valueOf(info.getSCarWidth()).trim()) + 0);
			fxzf.setHeight(Double.valueOf(String.valueOf(info.getSCarHeight()).trim()) + 0);

			logger.info("==========获取轴长和轴重==========");
			fxzf.setAxisLength(String.valueOf(info.getSMaxWheelBase()).trim());
			fxzf.setAxisWeightTotal(String.valueOf(info.getSAxisWeight()).trim());
			fxzf.setAxisWeight(String.valueOf(info.getSAxisWeight()).trim().split(","));

			// 默认为0未超限,1为超限
			fxzf.setWarnFlag(0);

			if (fxzf.getOverLoadStatus() == 0) {
				fxzf.setOverLoadPunish(0);// 超限处罚正常
			} else {
				fxzf.setOverLoadPunish(1);// 超限处罚审核
				fxzf.setOverLoad(Double.valueOf(String.valueOf(info.getSOverWeight()).trim()));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return fxzf;
	}

	/**
	 * 批量转译
	 * 
	 * @param info
	 * @return
	 */
	public static List<FxzfStatic> translateFxzfList(List<redis_FxzfInfo> info) {
		List<FxzfStatic> result = new ArrayList<FxzfStatic>();

		if (!CollectionUtils.isEmpty(info)) {
			for (redis_FxzfInfo redis_FxzfInfo : info) {
				result.add(FxzfStatic.translateFxzf(redis_FxzfInfo));
			}
		}

		return result;
	}

	@Override
	public OrderPart[] getOrderParts() {
		return null;
	}

	@Override
	public void setOrderParts(OrderPart[] orderParts) {
	}

}
