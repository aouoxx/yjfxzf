package com.supconit.kqfx.web.fxzf.msg.entities;

import hc.base.domains.OrderPart;

/**
 * 是否显示消息弹框
 * @author Administrator
 *
 */
public class MsgFlag extends hc.base.domains.StringId  implements hc.base.domains.Orderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private  String userId;
	
	private String flag;
	
	
	
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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
