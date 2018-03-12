package com.supconit.kqfx.web.fxzf.search.entities;

import hc.base.domains.PK;

import java.util.List;

/**
 * 业户信息
 */
public class YhInfo implements PK<String> {

    private static final long serialVersionUID = 1L;
    private String id;
    /** 经营类型 */
    private String operationType;
    /** 业户名称 */
    private String yhName;
    /** 业户地址 */
    private String yhAddress;
    /** 经济类型 */
    private String economicType;
    /** 受理编号 */
    private String acceptNo;
    /** 邮政编号 */
    private String postalNo;
    /** 经营范围 */
    private String operationArea;
    /** 行政区化代码 */
    private String regionCode;
    /** 行政区化名称 */
    private String regionName;
    /** 法人代表 */
    private String legalPersion;
    /** 法人代表电话 */
    private String legalTelephone;
    /** 法人代表手机 */
    private String legalCellphone;
    /** 法人证件号 */
    private String legalCertificate;
    /** 负责人 */
    private String principalPersion;
    /** 经办人 */
    private String agentPersion;
    /** 电话号码 */
    private String telephoneNo;
    /** 传真号码 */
    private String faxNo;
    /** 手机号码 */
    private String cellphoneNo;
    /** 电子邮箱 */
    private String email;
    /** 经营许可证号 */
    private String certificateNo;
    /** 有效期起 */
    @com.alibaba.fastjson.annotation.JSONField(format = "yyyy-MM-dd")
    private java.util.Date indateBegin;
    /** 有效期止 */
    @com.alibaba.fastjson.annotation.JSONField(format = "yyyy-MM-dd")
    private java.util.Date indateEnd;
    /** 工商执照 */
    private String businessLicenseNo;
    /** 工商管理部门 */
    private String commerceDepartment;
    /** 工商执照发证日期 */
    @com.alibaba.fastjson.annotation.JSONField(format = "yyyy-MM-dd")
    private java.util.Date businessLicenseDate;
    /** 注册资金 */
    private Long registeredFund;
    /** 户籍地运管机构 */
    private String domicilePipe;
    /** 是否营运 */
    private String operationState;
    /** 经度 */
    private Double longitude;
    /** 纬度 */
    private Double latidute;
    /** YH_CL_SL */
    private Long yhClSl;
    /** YH_RY_SL */
    private Long yhRySl;
    /** YHLX_OTHER_FLAG */
    private Integer yhlxOtherFlag;
    /** 车辆信息列表 */
    private List<VehicleInfo> vehicleList;
    /** 非现执法实体类 */
    private Fxzf fxzf;
    /** 违法次数 */
    private Integer illegalTimes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getYhName() {
        return yhName;
    }

    public void setYhName(String yhName) {
        this.yhName = yhName;
    }

    public String getYhAddress() {
        return yhAddress;
    }

    public void setYhAddress(String yhAddress) {
        this.yhAddress = yhAddress;
    }

    public String getEconomicType() {
        return economicType;
    }

    public void setEconomicType(String economicType) {
        this.economicType = economicType;
    }

    public String getAcceptNo() {
        return acceptNo;
    }

    public void setAcceptNo(String acceptNo) {
        this.acceptNo = acceptNo;
    }

    public String getPostalNo() {
        return postalNo;
    }

    public void setPostalNo(String postalNo) {
        this.postalNo = postalNo;
    }

    public String getOperationArea() {
        return operationArea;
    }

    public void setOperationArea(String operationArea) {
        this.operationArea = operationArea;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getLegalPersion() {
        return legalPersion;
    }

    public void setLegalPersion(String legalPersion) {
        this.legalPersion = legalPersion;
    }

    public String getLegalTelephone() {
        return legalTelephone;
    }

    public void setLegalTelephone(String legalTelephone) {
        this.legalTelephone = legalTelephone;
    }

    public String getLegalCellphone() {
        return legalCellphone;
    }

    public void setLegalCellphone(String legalCellphone) {
        this.legalCellphone = legalCellphone;
    }

    public String getLegalCertificate() {
        return legalCertificate;
    }

    public void setLegalCertificate(String legalCertificate) {
        this.legalCertificate = legalCertificate;
    }

    public String getPrincipalPersion() {
        return principalPersion;
    }

    public void setPrincipalPersion(String principalPersion) {
        this.principalPersion = principalPersion;
    }

    public String getAgentPersion() {
        return agentPersion;
    }

    public void setAgentPersion(String agentPersion) {
        this.agentPersion = agentPersion;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getCellphoneNo() {
        return cellphoneNo;
    }

    public void setCellphoneNo(String cellphoneNo) {
        this.cellphoneNo = cellphoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public java.util.Date getIndateBegin() {
        return indateBegin;
    }

    public void setIndateBegin(java.util.Date indateBegin) {
        this.indateBegin = indateBegin;
    }

    public java.util.Date getIndateEnd() {
        return indateEnd;
    }

    public void setIndateEnd(java.util.Date indateEnd) {
        this.indateEnd = indateEnd;
    }

    public String getBusinessLicenseNo() {
        return businessLicenseNo;
    }

    public void setBusinessLicenseNo(String businessLicenseNo) {
        this.businessLicenseNo = businessLicenseNo;
    }

    public String getCommerceDepartment() {
        return commerceDepartment;
    }

    public void setCommerceDepartment(String commerceDepartment) {
        this.commerceDepartment = commerceDepartment;
    }

    public java.util.Date getBusinessLicenseDate() {
        return businessLicenseDate;
    }

    public void setBusinessLicenseDate(java.util.Date businessLicenseDate) {
        this.businessLicenseDate = businessLicenseDate;
    }

    public Long getRegisteredFund() {
        return registeredFund;
    }

    public void setRegisteredFund(Long registeredFund) {
        this.registeredFund = registeredFund;
    }

    public String getDomicilePipe() {
        return domicilePipe;
    }

    public void setDomicilePipe(String domicilePipe) {
        this.domicilePipe = domicilePipe;
    }

    public String getOperationState() {
        return operationState;
    }

    public void setOperationState(String operationState) {
        this.operationState = operationState;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatidute() {
        return latidute;
    }

    public void setLatidute(Double latidute) {
        this.latidute = latidute;
    }

    public Long getYhClSl() {
        return yhClSl;
    }

    public void setYhClSl(Long yhClSl) {
        this.yhClSl = yhClSl;
    }

    public Long getYhRySl() {
        return yhRySl;
    }

    public void setYhRySl(Long yhRySl) {
        this.yhRySl = yhRySl;
    }

    public Integer getYhlxOtherFlag() {
        return yhlxOtherFlag;
    }

    public void setYhlxOtherFlag(Integer yhlxOtherFlag) {
        this.yhlxOtherFlag = yhlxOtherFlag;
    }

    public List<VehicleInfo> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<VehicleInfo> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public Fxzf getFxzf() {
        return fxzf;
    }

    public void setFxzf(Fxzf fxzf) {
        this.fxzf = fxzf;
    }

    public Integer getIllegalTimes() {
        return illegalTimes;
    }

    public void setIllegalTimes(Integer illegalTimes) {
        this.illegalTimes = illegalTimes;
    }
}
