package com.supconit.kqfx.web.device.entities;

public class DeviceInfo implements hc.base.domains.PK<String>{
  /**
	 * 
	 */
 private static final long serialVersionUID = 4608870032825088745L;
  /**
   * 设备编号
   */
  private String  id;
  /**
   * 设备序号
   */
  private String no;
  /**
   * 设备Ip
   */
  private String deviceIp;
  /**
   * 设备类型
   */
  private String deviceType;
  /**
   * 设备名字
   */
  private String deviceName;
  /**
   * 设备厂商
   */
  private String deviceFactory;
  /**
   * 设备桩号
   */
  private String deviceZh;
  /**
   * 设备治超站
   */
  private String deviceStation;
  /**
   * 治超站方向
   */
  private String deviceDirection;
  /**
   * 设备描述
   */
  private String deviceDispriction;
  /**
   * 判断周期
   */
  private Integer chargeCycle;
  /**
   * 成功阈值
   */
  private Integer alarmThreshold;
	@Override
	public String getId() {
		
		return this.id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
		
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getDeviceIp() {
		return deviceIp;
	}
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceFactory() {
		return deviceFactory;
	}
	public void setDeviceFactory(String deviceFactory) {
		this.deviceFactory = deviceFactory;
	}
	public String getDeviceZh() {
		return deviceZh;
	}
	public void setDeviceZh(String deviceZh) {
		this.deviceZh = deviceZh;
	}
	public String getDeviceStation() {
		return deviceStation;
	}
	public void setDeviceStation(String deviceStation) {
		this.deviceStation = deviceStation;
	}
	public String getDeviceDirection() {
		return deviceDirection;
	}
	public void setDeviceDirection(String deviceDirection) {
		this.deviceDirection = deviceDirection;
	}
	public String getDeviceDispriction() {
		return deviceDispriction;
	}
	public void setDeviceDispriction(String deviceDispriction) {
		this.deviceDispriction = deviceDispriction;
	}
	public Integer getChargeCycle() {
		return chargeCycle;
	}
	public void setChargeCycle(Integer chargeCycle) {
		this.chargeCycle = chargeCycle;
	}
	public Integer getAlarmThreshold() {
		return alarmThreshold;
	}
	public void setAlarmThreshold(Integer alarmThreshold) {
		this.alarmThreshold = alarmThreshold;
	}
	
}
