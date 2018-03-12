package com.supconit.kqfx.web.fxzf.search.entities;

import hc.base.domains.OrderPart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hc.base.domains.Orderable;
import hc.base.domains.StringId;
import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.supconit.kqfx.web.fxzf.avro.redis_FxzfInfo;
import com.supconit.kqfx.web.util.DateUtil;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.StakeUtil;

/**
 * 非现执法数据实体类
 * @author gaoshuo
 */
public class Fxzf extends StringId implements Orderable {

    private static final Logger logger = LoggerFactory.getLogger(Fxzf.class);

    private static final long serialVersionUID = -4412755926859182488L;

    /** 治超点 */
    private String detectStation;
    /** 车道号 */
    private Integer lane;
    /** 总重（T）*/
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
    /** 正确的车牌图*/
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
    /** 虚拟字段 */
    private String[] detects;
    private String overStatus;
    private Integer[] status;
    private String overPunish;
    private Integer[] punish;
    private String overLoadFlag;
    // 监控页面虚拟字段(情报板信息内容)
    private String xxnr;
    //告警类型
    private String warnType;
    //开始重量
    private Double beginWeight;
    //结束重量
    private Double endWeight;

    private YhInfo yhInfo;

    public String getAxisWeightTotal() {
        return axisWeightTotal;
    }

    public void setAxisWeightTotal(String axisWeightTotal) {
        this.axisWeightTotal = axisWeightTotal;
    }

    public String getOverLoadFlag() {
        return overLoadFlag;
    }

    public void setOverLoadFlag(String overLoadFlag) {
        this.overLoadFlag = overLoadFlag;
    }

    public String[] getDetects() {
        return detects;
    }

    public void setDetects(String[] detects) {
        this.detects = detects;
    }

    public Integer[] getStatus() {
        return status;
    }

    public void setStatus(Integer[] status) {
        this.status = status;
    }

    public Integer getWarnFlag() {
        return warnFlag;
    }

    public void setWarnFlag(Integer warnFlag) {
        this.warnFlag = warnFlag;
    }

    public Integer[] getPunish() {
        return punish;
    }

    public void setPunish(Integer[] punish) {
        this.punish = punish;
    }

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public void setOverLoad(Double overLoad) {
        this.overLoad = overLoad;
    }

    public Double getOverLoad() {
        return overLoad;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getDetectStation() {
        return detectStation;
    }

    public String getOverStatus() {
        return overStatus;
    }

    public void setOverStatus(String overStatus) {
        this.overStatus = overStatus;
    }

    public String getOverPunish() {
        return overPunish;
    }

    public void setOverPunish(String overPunish) {
        this.overPunish = overPunish;
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

    @Override
    public OrderPart[] getOrderParts() {
        return null;
    }

    @Override
    public void setOrderParts(OrderPart[] orderParts) {

    }

    public Integer getPicFlag() {
        return picFlag;
    }

    public void setPicFlag(Integer picFlag) {
        this.picFlag = picFlag;
    }

    private static final Double MODULE_18 = 18d;
    private static final Double MODULE_25 = 25d;
    private static final Double MODULE_27 = 27d;
    private static final Double MODULE_31 = 31d;
    private static final Double MODULE_36 = 36d;
    private static final Double MODULE_43 = 43d;
    private static final Double MODULE_46 = 46d;

    /**
     * 转译数据
     * @param info
     * @return
     */
    public static Fxzf translateFxzf(redis_FxzfInfo info) {
        Fxzf fxzf = new Fxzf();
        try {
            fxzf.setId(IDGenerator.idGenerator());
            fxzf.setDetectStation(String.valueOf(info.getSDetectStation()).trim());
            fxzf.setLane(Integer.valueOf(String.valueOf(info.getSLane()).trim()));
            //保留两位小数
            //总重
            fxzf.setWeight(!StringUtil.isEmpty(String.valueOf(info.getSWeight()).trim()) ? StakeUtil.round(Double.valueOf(String.valueOf(info.getSWeight()).trim()), 2) : null);
            //速度
            fxzf.setSpeed(!StringUtil.isEmpty(String.valueOf(info.getSSpeed()).trim()) ? Double.valueOf(String.valueOf(info.getSSpeed()).trim()) : null);
            //车轴
            Integer axisNum = Integer.valueOf(String.valueOf(info.getSAxisCount()).trim());
            fxzf.setAxisCount(axisNum);
            //车牌颜色
            fxzf.setLicenseColor(String.valueOf(info.getSHeadLicenseColor()).trim());
            /** 计算超限量，超限等级，超限率*/
            if (axisNum == 2) {
                //2轴超限
                if(fxzf.getWeight() > MODULE_18){
                    //超限量
                    fxzf.setOverLoad(Double.valueOf(fxzf.getWeight() - MODULE_18));
                    //超限率
                    fxzf.setOverLoadPercent(StakeUtil.round(fxzf.getOverLoad()/MODULE_18, 2));
                    //超重等级
                    if (fxzf.getOverLoad() <= 1) {
                        fxzf.setOverLoadStatus(1);
                    } else if (fxzf.getOverLoad() > 1d && fxzf.getOverLoad() <= 6d) {
                        fxzf.setOverLoadStatus(2);
                    } else if (fxzf.getOverLoad() > 6d && fxzf.getOverLoad() <= 10d) {
                        fxzf.setOverLoadStatus(3);
                    } else if (fxzf.getOverLoad() > 10d && fxzf.getOverLoad() <= 16d) {
                        fxzf.setOverLoadStatus(4);
                    } else if (fxzf.getOverLoad() > 16d) {
                        fxzf.setOverLoadStatus(5);
                    }
                }else {
                    fxzf.setOverLoad(0d);
                    fxzf.setOverLoadPercent(0.00);
                    fxzf.setOverLoadStatus(0);
                }
            } else if (axisNum == 3) {
                //3轴超限
                if(fxzf.getWeight() > MODULE_25){
                    if(fxzf.getLicenseColor().equals("蓝色")){
                        //汽车
                        if(fxzf.getWeight() > MODULE_27){
                            fxzf.setOverLoad(Double.valueOf(fxzf.getWeight() - MODULE_27));
                            fxzf.setOverLoadPercent(StakeUtil.round(fxzf.getOverLoad()/MODULE_27, 2));
                        }else {
                            fxzf.setOverLoad(0d);
                            fxzf.setOverLoadPercent(0.00);
                            fxzf.setOverLoadStatus(0);
                        }
                    }else {
                        //货车
                        fxzf.setOverLoad(Double.valueOf(fxzf.getWeight() - MODULE_25));
                        fxzf.setOverLoadPercent(StakeUtil.round(fxzf.getOverLoad()/MODULE_25, 2));
                    }
                    //超重等级
                    if (fxzf.getOverLoad() <= 1.5d) {
                        fxzf.setOverLoadStatus(1);
                    } else if (fxzf.getOverLoad() > 1.5d && fxzf.getOverLoad() <= 10d) {
                        fxzf.setOverLoadStatus(2);
                    } else if (fxzf.getOverLoad() > 10d && fxzf.getOverLoad() <= 15d) {
                        fxzf.setOverLoadStatus(3);
                    } else if (fxzf.getOverLoad() > 15d && fxzf.getOverLoad() <= MODULE_25) {
                        fxzf.setOverLoadStatus(4);
                    } else if (fxzf.getOverLoad() > MODULE_25) {
                        fxzf.setOverLoadStatus(5);
                    }
                }else {
                    fxzf.setOverLoad(0d);
                    fxzf.setOverLoadPercent(0.00);
                    fxzf.setOverLoadStatus(0);
                }
            } else if (axisNum == 4) {
                //4轴超限
                if(fxzf.getWeight() > MODULE_31){
                    if(fxzf.getLicenseColor().equals("蓝色")){
                        //汽车
                        if(fxzf.getWeight() > MODULE_36){
                            fxzf.setOverLoad(Double.valueOf(fxzf.getWeight() - MODULE_36));
                            fxzf.setOverLoadPercent(StakeUtil.round(fxzf.getOverLoad()/MODULE_36, 2));
                        }else {
                            fxzf.setOverLoad(0d);
                            fxzf.setOverLoadPercent(0.00);
                            fxzf.setOverLoadStatus(0);
                        }
                    }else {
                        //货车
                        fxzf.setOverLoad(Double.valueOf(fxzf.getWeight() - MODULE_31));
                        fxzf.setOverLoadPercent(StakeUtil.round(fxzf.getOverLoad()/MODULE_31, 2));
                    }
                    //超重等级
                    if (fxzf.getOverLoad() <= 2d) {
                        fxzf.setOverLoadStatus(1);
                    } else if (fxzf.getOverLoad() > 2d && fxzf.getOverLoad() <= 13d) {
                        fxzf.setOverLoadStatus(2);
                    } else if (fxzf.getOverLoad() > 13d && fxzf.getOverLoad() <= 20d) {
                        fxzf.setOverLoadStatus(3);
                    } else if (fxzf.getOverLoad() > 20d && fxzf.getOverLoad() <= 33d) {
                        fxzf.setOverLoadStatus(4);
                    } else if (fxzf.getOverLoad() > 33d) {
                        fxzf.setOverLoadStatus(5);
                    }
                }else {
                    fxzf.setOverLoad(0d);
                    fxzf.setOverLoadPercent(0.00);
                    fxzf.setOverLoadStatus(0);
                }
            } else if (axisNum == 5) {
                //5轴超限
                if(fxzf.getWeight() > MODULE_43){
                    fxzf.setOverLoad(Double.valueOf(fxzf.getWeight() - MODULE_43));
                    fxzf.setOverLoadPercent(StakeUtil.round(fxzf.getOverLoad()/MODULE_43, 2));
                    //超重等级
                    if (fxzf.getOverLoad() <= 2.5d) {
                        fxzf.setOverLoadStatus(1);
                    } else if (fxzf.getOverLoad() > 2.5d && fxzf.getOverLoad() <= 14d) {
                        fxzf.setOverLoadStatus(2);
                    } else if (fxzf.getOverLoad() > 14d && fxzf.getOverLoad() <= MODULE_25) {
                        fxzf.setOverLoadStatus(3);
                    } else if (fxzf.getOverLoad() > MODULE_25 && fxzf.getOverLoad() <= 42d) {
                        fxzf.setOverLoadStatus(4);
                    } else if (fxzf.getOverLoad() > 42d) {
                        fxzf.setOverLoadStatus(5);
                    }
                }else {
                    fxzf.setOverLoad(0d);
                    fxzf.setOverLoadPercent(0.00);
                    fxzf.setOverLoadStatus(0);
                }
            } else {
                //6轴及以上超限
                if(fxzf.getWeight() > MODULE_46){
                    fxzf.setOverLoad(Double.valueOf(fxzf.getWeight() - MODULE_46));
                    fxzf.setOverLoadPercent(StakeUtil.round(fxzf.getOverLoad()/MODULE_46, 2));
                    //超重等级
                    if (fxzf.getOverLoad() <= 3d) {
                        fxzf.setOverLoadStatus(1);
                    } else if (fxzf.getOverLoad() > 3d && fxzf.getOverLoad() <= MODULE_18) {
                        fxzf.setOverLoadStatus(2);
                    } else if (fxzf.getOverLoad() > MODULE_18 && fxzf.getOverLoad() <= 28d) {
                        fxzf.setOverLoadStatus(3);
                    } else if (fxzf.getOverLoad() > 28d && fxzf.getOverLoad() <= MODULE_46) {
                        fxzf.setOverLoadStatus(4);
                    } else if (fxzf.getOverLoad() > MODULE_46) {
                        fxzf.setOverLoadStatus(5);
                    }
                }else {
                    fxzf.setOverLoad(0d);
                    fxzf.setOverLoadPercent(0.00);
                    fxzf.setOverLoadStatus(0);
                }
            }

            // 如果时间为空则为系统时间
            fxzf.setCaptureTime(!StringUtil.isEmpty(String.valueOf(info.getSCaptureTime()).trim())
                    ? DateUtil.formatStringToDate(String.valueOf(info.getSCaptureTime()).trim(), DateUtil.DATE_FORMAT_TIME_T) : new Date());
            if (info.getSHeadLicense() == null)
                logger.info("==========车头信息转换");
            fxzf.setHeadLicense(String.valueOf(info.getSHeadLicense()).trim());
            fxzf.setHeadLicenseColor(String.valueOf(info.getSHeadLicenseColor()).trim());
            fxzf.setHeadLicensePicdir(String.valueOf(info.getSHeadLicensePicDir()).trim());
            fxzf.setHeadCarPicdir(String.valueOf(info.getSHeadCarPicDir()).trim());



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
    public static List<Fxzf> translateFxzfList(List<redis_FxzfInfo> info) {
        List<Fxzf> result = new ArrayList<Fxzf>();
        if (!CollectionUtils.isEmpty(info)) {
            for (redis_FxzfInfo redis_FxzfInfo : info) {
                result.add(Fxzf.translateFxzf(redis_FxzfInfo));
            }
        }
        return result;
    }

    public String getXxnr() {
        return xxnr;
    }

    public void setXxnr(String xxnr) {
        this.xxnr = xxnr;
    }

    public String getDetectStationFlag() {
        return detectStationFlag;
    }

    public void setDetectStationFlag(String detectStationFlag) {
        this.detectStationFlag = detectStationFlag;
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

    public YhInfo getYhInfo() {
        return yhInfo;
    }

    public void setYhInfo(YhInfo yhInfo) {
        this.yhInfo = yhInfo;
    }
}
