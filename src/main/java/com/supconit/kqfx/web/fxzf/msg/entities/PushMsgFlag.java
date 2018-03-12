package com.supconit.kqfx.web.fxzf.msg.entities;

import hc.base.domains.OrderPart;

public class PushMsgFlag  extends hc.base.domains.StringId  implements hc.base.domains.Orderable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 设备 channelID
	 */
	private String channelId;
	
	/**
	 * 设备种类DeviceType
	 */
	private String type;
	
	/**
	 * 标志位名字
	 */
	private String flagName;
	
	/**
	 * 标志位对应的值
	 */
	private String flagValue;
	
	/**
	 * 更新时间
	 */
	private String updateTime;
	
	/**
	 *USERID数组 
	 */
	private String[] userIds;
	
	
	
	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlagName() {
		return flagName;
	}

	public void setFlagName(String flagName) {
		this.flagName = flagName;
	}

	public String getFlagValue() {
		return flagValue;
	}

	public void setFlagValue(String flagValue) {
		this.flagValue = flagValue;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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
