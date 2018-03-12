package com.supconit.kqfx.web.fxzf.msg.entities;

import java.util.Date;

import hc.base.domains.OrderPart;

/**
 * 告警信息实体类
 * @author gaoshuo
 *
 */
public class Msg extends hc.base.domains.StringId  implements hc.base.domains.Orderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 698242157940644050L;

	@Override
	public OrderPart[] getOrderParts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOrderParts(OrderPart[] orderParts) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 公告或告警信息关联ID
	 */
	private String pid;
	
	/**
	 * 公告或告警标志 flag：0告警  1公告
	 */
	private String flag;
	
	/**
	 * 公告或告警有效期
	 */
	private Date usefuldateTime;
	
	/**
	 * 公告或告警内容
	 */
	private String content;
	
	/**
	 * 机构ID用于获取回应的用户信息
	 */
	private String jgid;

	/**
	 * 告警或公告时间
	 */
	private Date updateTime;
	
	/**
	 * 是否显示信息
	 */
	private String msgflag;
	
	
	
	
	
	public String getMsgflag() {
		return msgflag;
	}

	public void setMsgflag(String msgflag) {
		this.msgflag = msgflag;
	}

	public Date getUsefuldateTime() {
		return usefuldateTime;
	}

	public void setUsefuldateTime(Date usefuldateTime) {
		this.usefuldateTime = usefuldateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}
	
	
	
	
}
