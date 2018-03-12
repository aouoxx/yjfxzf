package com.supconit.kqfx.web.fxzf.qbb.entities;

import hc.base.domains.PK;

import java.util.Date;

import jodd.util.StringUtil;

import com.supconit.kqfx.web.fxzf.avro.qbbItemInfo;
import com.supconit.kqfx.web.util.EncodeTransforUtil;

public class QbbBfxx implements PK<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private String id;
	
	/**
	 * 信息内容
	 */
	private String xxnr;
	
	/**
	 * 出字方式
	 */
	private String czfs;
	
	/**
	 * 字体
	 */
	private String font;
	
	/**
	 * 颜色
	 */
	private String color;
	
	/**
	 * 停留时间(分钟)
	 */
	private Long remainTime;
	
	/**
	 * 发布时间
	 */
	private Date publishTime;
	
	/**
	 * 发布时间
	 */
	private String publishTimeString;
	
	/**
	 * 轮询时间(单位为百分之一秒，范围 00002-30000， 缺省为 00002)
	 */
	private Long circleTime;
	
	/**
	 * 情报板位置
	 */
	private String location;
	
	/**
	 * 发布类型(0为自动发布 1为手动发布)
	 */
	private String type;
	
	/**
	 * 播放列表顺序
	 */
	private Integer listOrder;
	
	/**
	 * 删除标志位
	 */
	private Integer deleted;
	
	/**
	 * 判断是否正在播放
	 */
	private String flag;
	
	/**====================================***/
	/**
	 * 信息内容1
	 */
	private String xxnr1;
	/**
	 * 信息内容2
	 */
	private String xxnr2;
	/**
	 * 信息内容3
	 */
	private String xxnr3;
	/**
	 * 信息内容4
	 */
	private String xxnr4;
	/**
	 * 信息内容5
	 */
	private String xxnr5;
	
	/**
	 * 情报板位置
	 */
	private String locationStr;
	
	/**
	 * 对应志超站播放列表的数据
	 */
	private int count;

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 超过16个字符自动截取
	 * @return
	 */
	public String getXxnr() {
		return EncodeTransforUtil.getXxnrResult(xxnr,null);
	}
	
	/**
	 * 超过16个字符自动截取
	 * @return
	 */
	public void setXxnr(String xxnr) {
		this.xxnr = EncodeTransforUtil.getXxnrResult(xxnr,null);
	}

	public String getCzfs() {
		return czfs;
	}

	public void setCzfs(String czfs) {
		this.czfs = czfs;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(Long remainTime) {
		this.remainTime = remainTime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Long getCircleTime() {
		return circleTime;
	}

	public void setCircleTime(Long circleTime) {
		this.circleTime = circleTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getListOrder() {
		return listOrder;
	}

	public void setListOrder(Integer listOrder) {
		this.listOrder = listOrder;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public static qbbItemInfo translateToQbbItem(QbbBfxx qbbBfxx){
		qbbItemInfo info = new qbbItemInfo();
		info.setSCnt(qbbBfxx.getXxnr());
		info.setSColor(qbbBfxx.getColor());
		//单位为百分之一秒，范围 00002-30000， 缺省为 600,为60s
		info.setSDelay(null!=qbbBfxx.getCircleTime()?String.valueOf(qbbBfxx.getCircleTime()):"600");
		info.setSFont(qbbBfxx.getFont());
		info.setSWay(!StringUtil.isEmpty(qbbBfxx.getCzfs())?qbbBfxx.getCzfs():"00");
		//范围00-49
		info.setSSpeed("00");
		//超限信息	0
		//宣传语	1	支持节目单的轮询播放
		//
		info.setSDataType(0+"");
		return info;
	}

	public String getXxnr1() {
		return xxnr1;
	}

	public void setXxnr1(String xxnr1) {
		this.xxnr1 = xxnr1;
	}

	public String getXxnr2() {
		return xxnr2;
	}

	public void setXxnr2(String xxnr2) {
		this.xxnr2 = xxnr2;
	}

	public String getXxnr3() {
		return xxnr3;
	}

	public void setXxnr3(String xxnr3) {
		this.xxnr3 = xxnr3;
	}

	public String getXxnr4() {
		return xxnr4;
	}

	public void setXxnr4(String xxnr4) {
		this.xxnr4 = xxnr4;
	}

	public String getXxnr5() {
		return xxnr5;
	}

	public void setXxnr5(String xxnr5) {
		this.xxnr5 = xxnr5;
	}

	public String getLocationStr() {
		return locationStr;
	}

	public void setLocationStr(String locationStr) {
		this.locationStr = locationStr;
	}

	public String getPublishTimeString() {
		return publishTimeString;
	}

	public void setPublishTimeString(String publishTimeString) {
		this.publishTimeString = publishTimeString;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
