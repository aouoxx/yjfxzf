package com.supconit.kqfx.web.list.entities;

import java.util.Date;

/**
 * 黑名单实体类
 * @author caijianming
 *
 */
public class BlackList implements hc.base.domains.PK<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	/**
	 * 车牌号
	 */
	private String license;
	
	/**
	 * 车牌颜色
	 */
	private String plateColor;
	
	/**
	 * 历史超限次数
	 */
	private Long overloadTimes;
	
	/**
	 * 加入黑名单时间
	 */
	private Date updateTime;
	
	/**
	 * 说明
	 */
	private String illustrate;
	
	/**
	 * 操作人员账号
	 */
	private String operator;
	
	/**
	 * 操作时间
	 */
	private Date operateTime;
	
	/**
	 * 是否人工添加(0为自动添加,1为人工添加)
	 */
	private Integer addByOperatorFlag;
	
	/**
	 * 删除标志位
	 */
	private Integer deleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Long getOverloadTimes() {
		return overloadTimes;
	}

	public void setOverloadTimes(Long overloadTimes) {
		this.overloadTimes = overloadTimes;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getIllustrate() {
		return illustrate;
	}

	public void setIllustrate(String illustrate) {
		this.illustrate = illustrate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Integer getAddByOperatorFlag() {
		return addByOperatorFlag;
	}

	public void setAddByOperatorFlag(Integer addByOperatorFlag) {
		this.addByOperatorFlag = addByOperatorFlag;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
}
