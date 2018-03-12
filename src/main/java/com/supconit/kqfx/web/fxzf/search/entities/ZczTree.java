package com.supconit.kqfx.web.fxzf.search.entities;
import java.util.List;

import hc.base.domains.OrderPart;
/**
 * 治超站树结构
 * @author gs
 */
public class ZczTree extends hc.base.domains.LongId implements
hc.base.domains.Orderable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1549294064789608777L;
	/**
	 * 父节点
	 */
	private Long pid;
	/**
	 * 节点名称
	 */
	private String name;
	
	private String value;
	
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private List<ZczTree> zczList;
	
	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public List<ZczTree> getZczList() {
		return zczList;
	}

	public void setZczList(List<ZczTree> zczList) {
		this.zczList = zczList;
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
