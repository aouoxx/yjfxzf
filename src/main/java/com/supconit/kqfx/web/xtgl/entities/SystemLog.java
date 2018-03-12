/**
 * 
 */
package com.supconit.kqfx.web.xtgl.entities;
import java.util.Date;

import com.supconit.honeycomb.business.authorization.entities.Menu;

/**
 * 系统日志
 * 
 * @author 
 * @create 2014-04-09 15:46:42 
 * @since 
 * 
 */
public class SystemLog  implements hc.base.domains.PK<String>{

	private static final long	serialVersionUID	= 1L;
	
	/** 标识ID */
	private String id;
	/** 模块代码 */
	private String moduleCode;
	/** 操作类型 */
	private String operateType;
	/** 操作内容 */
	private String operateContent;
	/** 操作者 */
	private Long operateUser;
	/** 访问IP */
	private String operateIp;
	/** 操作时间 */
	private Date operateTime;
	
	private Date startTime;
	private Date endTime;
	private String startTimeString;
	private String endTimeString;
	
	private String moduleStr;
	private String operateTypeStr;

	private Menu menu;
	
	private String userName;
	private String name;
	
	
	
	public String getStartTimeString() {
		return startTimeString;
	}

	public void setStartTimeString(String startTimeString) {
		this.startTimeString = startTimeString;
	}

	public String getEndTimeString() {
		return endTimeString;
	}

	public void setEndTimeString(String endTimeString) {
		this.endTimeString = endTimeString;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	private ExtUser user;
	
	private ExtPerson person;
	
	
		
			
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
				
	public String getModuleCode() {
		return moduleCode;
	}
	
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
				
	public String getOperateType() {
		return operateType;
	}
	
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
				
	public String getOperateContent() {
		return operateContent;
	}
	
	public void setOperateContent(String operateContent) {
		this.operateContent = operateContent;
	}
				
	public Long getOperateUser() {
		return operateUser;
	}
	
	public void setOperateUser(Long operateUser) {
		this.operateUser = operateUser;
	}
				
	public String getOperateIp() {
		return operateIp;
	}
	
	public void setOperateIp(String operateIp) {
		this.operateIp = operateIp;
	}
				
	public Date getOperateTime() {
		return operateTime;
	}
	
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public ExtUser getUser() {
		return user;
	}

	public void setUser(ExtUser user) {
		this.user = user;
	}

	public String getModuleStr() {
		return moduleStr;
	}

	public void setModuleStr(String moduleStr) {
		this.moduleStr = moduleStr;
	}

	public String getOperateTypeStr() {
		return operateTypeStr;
	}

	public void setOperateTypeStr(String operateTypeStr) {
		this.operateTypeStr = operateTypeStr;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public ExtPerson getPerson() {
		return person;
	}

	public void setPerson(ExtPerson person) {
		this.person = person;
	}


}
