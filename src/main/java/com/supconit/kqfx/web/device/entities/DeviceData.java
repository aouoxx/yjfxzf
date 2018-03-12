package com.supconit.kqfx.web.device.entities;

import java.util.Date;

public class DeviceData {
	/**
	 * 设备ID
	 */
	private String indexCode;
	/**
	 * 设备秘钥
	 */
	private String deviceKey;
	/**
	 * 设备状态
	 */
	private String deviceState;
	/**
	 * 异常原因
	 */
	private String exceptionReason;
	/**
	 * 设备上报时间
	 */
	private Date checkTime;
	/**
	 * 设备状态上报时间
	 */
	private String transferTime;
	
	/**
	 * 上报时间
	 */
	private Date nowTime;
	
	/**
	 * 前推时间
	 */
	private Date beforeTime;
	

	public String getDeviceKey() {
		return deviceKey;
	}
	public void setDeviceKey(String deviceKey) {
		this.deviceKey = deviceKey;
	}
	public String getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(String deviceState) {
		this.deviceState = deviceState;
	}

	public String getExceptionReason() {
		return exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public Date getBeforeTime() {
		return beforeTime;
	}

	public void setBeforeTime(Date beforeTime) {
		this.beforeTime = beforeTime;
	}
	public String getIndexCode() {
		return indexCode;
	}
	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public Date getNowTime() {
		return nowTime;
	}
	public void setNowTime(Date nowTime) {
		this.nowTime = nowTime;
	}
	public String getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}

}
