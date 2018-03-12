/**
 * 
 */
package com.supconit.kqfx.web.xtgl.services.impl;

import hc.base.domains.Pageable;
import hc.safety.manager.SafetyManager;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.xtgl.daos.SystemLogDao;
import com.supconit.kqfx.web.xtgl.entities.SystemLog;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * @author 
 * @create 2014-04-09 15:46:42 
 * @since 
 * 
 */
@Service("xtgl_systemLog_service")
public class SystemLogServiceImpl extends hc.orm.AbstractBasicOrmService<SystemLog, String> implements SystemLogService {
	
	private transient static final Logger	logger	= LoggerFactory.getLogger(SystemLogServiceImpl.class);
	
	@Autowired
	private SystemLogDao		systemLogDao;
	@Autowired
	private SafetyManager	safetyManager;
	
	@Override
	@Transactional(readOnly = true)
	public SystemLog getById(String id) {
		return this.systemLogDao.getById(id);
	}

	@Override
	@Transactional
	public void insert(SystemLog entity) {
		this.systemLogDao.insert(entity);
	}

	@Override
	@Transactional
	public void update(SystemLog entity) {
		this.systemLogDao.update(entity);
	}

	@Override
	@Transactional
	public void delete(SystemLog entity) {
		this.systemLogDao.delete(entity);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Pageable<SystemLog> find(Pageable<SystemLog> pager, SystemLog condition) {
		
		return this.systemLogDao.findByPager(pager, condition);
	}

	@Override
	@Transactional
	public void log(String moduleCode, String operateType,
			String operateContent, String operateIp) {
		if(moduleCode == null || moduleCode.isEmpty()){
			moduleCode = " ";
		}
		if(operateType == null || operateType.isEmpty()){
			operateType = " ";
		}
		if(operateContent == null || operateContent.isEmpty()){
			operateContent = " ";
		}
		if(operateIp == null || operateIp.isEmpty()){
			operateIp = " ";
		}
		SystemLog systemLog = new SystemLog();
		systemLog.setId(IDGenerator.idGenerator());
		systemLog.setModuleCode(moduleCode);
		systemLog.setOperateType(operateType);
		systemLog.setOperateContent(operateContent);
		User user = (User)safetyManager.getCurrentUser();
		systemLog.setOperateUser(user.getId());
		systemLog.setOperateIp(operateIp);
		systemLog.setOperateTime(new Date());
		this.systemLogDao.insert(systemLog);
	}
	
	
}
