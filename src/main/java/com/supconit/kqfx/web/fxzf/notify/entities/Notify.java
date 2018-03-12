package com.supconit.kqfx.web.fxzf.notify.entities;



import hc.base.domains.OrderPart;

import java.util.Date;

/**
 * 公告载体
 * @author dell
 *
 */
public class Notify extends hc.base.domains.StringId  implements hc.base.domains.Orderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4782534558715735403L;
	
	/**
	 * 公告标题
	 */
	private String title;
	
	/**
	 * 公告内容
	 */
	private String content;
	
	/**
	 * 公告类型
	 */
	private String type;
	
	/**
	 * 公告发布时间
	 */
	private Date updateTime;
	
	/**
	 * 公告范围
	 */
	private String range;
	
	/**
	 * 公告载体
	 */
	private String carrier;
	
	/**
	 * 公告有效期截止时间
	 */
	private Date endDateTime;
	
	/**
	 * 虚拟变量
	 */
	
	private Date backTime;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date date) {
		this.updateTime = date;
	}


	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
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

	public Date getBackTime() {
		return backTime;
	}

	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	
	
}
