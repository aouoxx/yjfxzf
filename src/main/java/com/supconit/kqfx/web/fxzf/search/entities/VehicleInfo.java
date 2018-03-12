package com.supconit.kqfx.web.fxzf.search.entities;


import hc.base.domains.PK;

import java.util.Date;

/**
 * 车辆信息实体信息
 */
public class VehicleInfo implements PK<String> {

    private static final long serialVersionUID = 1L;
    private String id;
    /** 业户ID */
    private String yhId;
    /** 车牌号码 */
    private String planeNo;
    /** 车牌颜色 */
    protected String planeColor;
    /** 车身颜色 */
    private String vehicleColor;
    /** 经营类型 */
    private String vehicleType;
    /** 车牌类型 */
    protected String plateType;
    /** 车辆等级 */
    private String vehicleLevel;
    /** 挂车号码 */
    private String trailerNo;
    /** 品牌类型 */
    private String brandType;
    /** 厂牌 */
    private String brandFactory;
    /** 车辆档案号 */
    private String vehicleFn;
    /** 燃料类型 */
    private String fuelType;
    /** 核定载客 */
    private Long checkPassenger;
    /** 核载重量 */
    private Double checkWeight;
    /** 罐体容积 */
    private Double tankVolume;
    /** 车长_mm */
    private Long vlength;
    /** 车宽_mm */
    private Long vweight;
    /** 车高_mm */
    private Long vheight;
    /** 发动机号 */
    private String engineNo;
    /** 底盘号 */
    private String chassisNo;
    /** 出厂日期 */
    @com.alibaba.fastjson.annotation.JSONField(format = "yyyy-MM-dd")
    private Date factoryDate;
    /** 启用日期 */
    @com.alibaba.fastjson.annotation.JSONField(format = "yyyy-MM-dd")
    private Date startupDate;
    /** 行政区划代码 */
    private String regionCode;
    /** 道路运输证号 */
    private String transportNo;
    /** 道路运输证备注 */
    private String transportNote;
    /** 道路运输证字 */
    private String transportMark;
    /** 车辆业户信息 */
    private YhInfo yhInfo;
    /** ===虚拟字段(车辆违法统计)== **/
    //统计开始时间
    private Date beginTime;
    //统计开始时间
    private Date endTime;
    //车牌号
    private String vehicleplate;
    //违法次数
    private Integer illegaltimes;
    //违法统计更新时间
    private Date updateTime;

    private String detectstationtype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYhId() {
        return yhId;
    }

    public void setYhId(String yhId) {
        this.yhId = yhId;
    }

    public String getPlaneNo() {
        return planeNo;
    }

    public void setPlaneNo(String planeNo) {
        this.planeNo = planeNo;
    }

    public String getPlaneColor() {
        return planeColor;
    }

    public void setPlaneColor(String planeColor) {
        this.planeColor = planeColor;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getVehicleLevel() {
        return vehicleLevel;
    }

    public void setVehicleLevel(String vehicleLevel) {
        this.vehicleLevel = vehicleLevel;
    }

    public String getTrailerNo() {
        return trailerNo;
    }

    public void setTrailerNo(String trailerNo) {
        this.trailerNo = trailerNo;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getBrandFactory() {
        return brandFactory;
    }

    public void setBrandFactory(String brandFactory) {
        this.brandFactory = brandFactory;
    }

    public String getVehicleFn() {
        return vehicleFn;
    }

    public void setVehicleFn(String vehicleFn) {
        this.vehicleFn = vehicleFn;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public Long getCheckPassenger() {
        return checkPassenger;
    }

    public void setCheckPassenger(Long checkPassenger) {
        this.checkPassenger = checkPassenger;
    }

    public Double getCheckWeight() {
        return checkWeight;
    }

    public void setCheckWeight(Double checkWeight) {
        this.checkWeight = checkWeight;
    }

    public Double getTankVolume() {
        return tankVolume;
    }

    public void setTankVolume(Double tankVolume) {
        this.tankVolume = tankVolume;
    }

    public Long getVlength() {
        return vlength;
    }

    public void setVlength(Long vlength) {
        this.vlength = vlength;
    }

    public Long getVweight() {
        return vweight;
    }

    public void setVweight(Long vweight) {
        this.vweight = vweight;
    }

    public Long getVheight() {
        return vheight;
    }

    public void setVheight(Long vheight) {
        this.vheight = vheight;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public Date getFactoryDate() {
        return factoryDate;
    }

    public void setFactoryDate(Date factoryDate) {
        this.factoryDate = factoryDate;
    }

    public Date getStartupDate() {
        return startupDate;
    }

    public void setStartupDate(Date startupDate) {
        this.startupDate = startupDate;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getTransportNote() {
        return transportNote;
    }

    public void setTransportNote(String transportNote) {
        this.transportNote = transportNote;
    }

    public String getTransportMark() {
        return transportMark;
    }

    public void setTransportMark(String transportMark) {
        this.transportMark = transportMark;
    }

    public YhInfo getYhInfo() {
        return yhInfo;
    }

    public void setYhInfo(YhInfo yhInfo) {
        this.yhInfo = yhInfo;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getVehicleplate() {
        return vehicleplate;
    }

    public void setVehicleplate(String vehicleplate) {
        this.vehicleplate = vehicleplate;
    }

    public Integer getIllegaltimes() {
        return illegaltimes;
    }

    public void setIllegaltimes(Integer illegaltimes) {
        this.illegaltimes = illegaltimes;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDetectstationtype() {
        return detectstationtype;
    }

    public void setDetectstationtype(String detectstationtype) {
        this.detectstationtype = detectstationtype;
    }
}
