package com.supconit.kqfx.web.fxzf.search.entities;

import java.util.Date;

import hc.base.domains.OrderPart;

/**
 * 违章车辆轨迹实体类
 * @author gaoshuo
 *
 */
public class IllegalTrail extends  hc.base.domains.StringId  implements hc.base.domains.Orderable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6526635842847511216L;

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
	private Integer overLoadTimes;
	/**
	 * 最后违法时间
	 */
	private Date updateTime;
	/**
	 * 删除标志位
	 */
	private Integer deleted;
	

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

	public Integer getOverLoadTimes() {
		return overLoadTimes;
	}

	public void setOverLoadTimes(Integer overLoadTimes) {
		this.overLoadTimes = overLoadTimes;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
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
