package com.supconit.kqfx.web.analysis.services.impl;

import hc.business.dic.entities.Data;
import hc.business.dic.services.DataDictionaryService;
import hc.orm.AbstractBasicOrmService;
import hc.safety.manager.SafetyManager;

import java.util.ArrayList;
import java.util.List;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.supconit.honeycomb.business.authorization.entities.Role;
import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.honeycomb.business.authorization.services.RoleService;
import com.supconit.kqfx.web.analysis.dao.JgZcdDao;
import com.supconit.kqfx.web.analysis.entities.JgZcd;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.fxzf.monitor.entities.ZTreeNode;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.entities.Jgxx;
import com.supconit.kqfx.web.xtgl.services.ExtPersonService;
import com.supconit.kqfx.web.xtgl.services.JgxxService;

@Service("analysis_jgzcd_service")
public class JgZcdServiceImpl extends AbstractBasicOrmService<JgZcd, String>
		implements JgZcdService {
	
	@Autowired
	private JgZcdDao jgZcdDao;
	
	@Autowired
	private JgxxService	jgxxService;
	
	@Autowired
	private ExtPersonService	extPersonService;
	
	@Autowired 
	private RoleService	roleService;
	
	@Autowired
	private DataDictionaryService	dataDictionaryService;
	
	@Autowired
	private SafetyManager	safetyManager;
	
	private static final String DIC_DETECT = "DETECTIONSTATION";
	
	@Override
	public JgZcd getById(String id) {
		
		return jgZcdDao.getById(id);
	}

	@Override
	public void insert(JgZcd entity) {
		this.jgZcdDao.insert(entity);
	}

	@Override
	public void update(JgZcd entity) {
		this.jgZcdDao.update(entity);
	}

	@Override
	public void delete(JgZcd entity) {
		this.jgZcdDao.delete(entity);
	}

	@Override
	public List<JgZcd> getJgZcdListByAuth() {
		Long jgid = this.getJgIdByAuth();
		
		List<JgZcd> jgList = new ArrayList<JgZcd>();
		
		if(jgid!=null){
			JgZcd jgZcd = new JgZcd();
			jgZcd.setJgid(jgid);
			jgZcd.setJgxx(this.jgxxService.getById(jgZcd.getJgid()));
			jgList.add(jgZcd);
		}else{
			jgList = this.jgZcdDao.getJgZcdList();
			if(!CollectionUtils.isEmpty(jgList))
			{
				for (JgZcd jgZcd : jgList) {
					jgZcd.setJgxx(this.jgxxService.getById(jgZcd.getJgid()));
				}
			}
		}
		
		return jgList;
	}

	@Override
	public List<JgZcd> getZcdListByAuth() {
		Long jgid = this.getJgIdByAuth();
		
		List<JgZcd> zcdList = this.jgZcdDao.getZcdListByAuth(jgid);
		
		if(!CollectionUtils.isEmpty(zcdList))
		{
			for (JgZcd jgZcd : zcdList) {
				Data data = this.dataDictionaryService.getByDataCode(DIC_DETECT, jgZcd.getDeteStation());
				jgZcd.setDeteStationName(null!=data?data.getName():"");
			}
		}
		
		return zcdList;
	}

	@Override
	public List<JgZcd> getByJgid(Long jgid) {
		return jgZcdDao.getZcdListByAuth(jgid);
	}

	@Override
	public JgZcd getByZczId(String detectStation) {
		
		return jgZcdDao.getByZczId(detectStation);
	}

	@Override
	public Long getJgIdByAuth() {
		//当前登录用户
		User user = (User) this.safetyManager.getCurrentUser();
		Long jgid = null;
		if(null!=user&&null!=user.getPerson()&&null!=user.getPersonId())
		{
			Boolean isAdmin = false;
			List<Role> rolelist = this.roleService.findAssigned(user.getId());
			if(!CollectionUtils.isEmpty(rolelist)){
				for (Role role : rolelist) {
					if ("ROLE_ADMIN".equals(role.getCode())) isAdmin = true;
				}
			}
			
			if(!isAdmin)
			{
				ExtPerson person = this.extPersonService.getById(user.getPersonId());
				jgid = null!=person?person.getJgbh():null;
			}
		}
		return jgid;
	}

	@Override
	public List<ZTreeNode> getZtreeNodeByAuth() {
		
		List<ZTreeNode> result = new ArrayList<ZTreeNode>();
		Long jgid = this.getJgIdByAuth();
		List<JgZcd> zcdList = this.jgZcdDao.getZtreeNodeListByAuth(jgid);
		if(!CollectionUtils.isEmpty(zcdList)){
			ZTreeNode node = null;
			for (JgZcd jgZcd : zcdList) {
				Jgxx jgxx = this.jgxxService.getById(jgZcd.getJgid());
				if(null!=jgxx){
					node = new ZTreeNode();
					List<ZTreeNode> children = new ArrayList<ZTreeNode>();
					node.setId(String.valueOf(jgxx.getId()));
					node.setpId(ZTreeNode.root_id);
					node.setName(jgxx.getJgmc());
					node.setOpen(true);
					
					if(null!=jgZcd.getJgid()){
						ZTreeNode child = null;
						List<JgZcd> zcdList2 = this.jgZcdDao.getZcdListByAuth(jgZcd.getJgid());
						if(!CollectionUtils.isEmpty(zcdList2))
						{
							for (JgZcd jgZcd2 : zcdList2) {
								if(!StringUtil.isEmpty(jgZcd2.getDeteStation())){
									child = new ZTreeNode();
									Data data = this.dataDictionaryService.getByDataCode(DIC_DETECT, jgZcd2.getDeteStation());
									child.setId(jgZcd2.getDeteStation());
									child.setpId(String.valueOf(jgxx.getId()));
									child.setName(null!=data?data.getName():"");
									child.setOpen(true);
									children.add(child);
								}
								
							}
						}
					}
					node.setChildren(children);
				}
				result.add(node);
			}
		}
		return result;
	}

}
