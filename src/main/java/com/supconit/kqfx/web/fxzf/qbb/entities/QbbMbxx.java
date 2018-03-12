package com.supconit.kqfx.web.fxzf.qbb.entities;

import java.util.Date;

import hc.base.domains.PK;

public class QbbMbxx implements PK<String>{
	
	
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
	 * 保存时间
	 */
	private Date time;
	
	/**
	 * 删除标志位
	 */
	private Integer deleted;
	
	/**
	 * 0为情报板发布模板，1为情报板告警模板
	 */
	private Integer flag;
	
	/** 出字方式（文字）*/
	private String czfsStr;
	/** 字体（文字）*/
	private String fontStr;
	/** 颜色（文字）*/
	private String colorStr;
	
	private String xxnrHtml;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXxnr() {
		return xxnr;
	}

	public void setXxnr(String xxnr) {
		this.xxnr = xxnr;
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getCzfsStr() {
		return czfsStr;
	}

	public void setCzfsStr(String czfsStr) {
		this.czfsStr = czfsStr;
	}

	public String getFontStr() {
		return fontStr;
	}

	public void setFontStr(String fontStr) {
		this.fontStr = fontStr;
	}

	public String getColorStr() {
		return colorStr;
	}

	public void setColorStr(String colorStr) {
		this.colorStr = colorStr;
	}

	public String getXxnrHtml() {
		return xxnrHtml;
	}

	public void setXxnrHtml(String xxnrHtml) {
		this.xxnrHtml = xxnrHtml;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	} 
	
	
}
