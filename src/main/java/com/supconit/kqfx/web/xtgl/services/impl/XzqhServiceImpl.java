/**
 * 
 */
package com.supconit.kqfx.web.xtgl.services.impl;

import hc.base.domains.Pageable;
import hc.safety.manager.SafetyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.kqfx.web.xtgl.daos.XzqhDao;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.entities.Jgxx;
import com.supconit.kqfx.web.xtgl.entities.Xzqh;
import com.supconit.kqfx.web.xtgl.services.ExtPersonService;
import com.supconit.kqfx.web.xtgl.services.JgxxService;
import com.supconit.kqfx.web.xtgl.services.XzqhService;

/**
 * @author 
 * @create 2014-03-17 16:03:40 
 * @since 
 * 
 */
@Service("xtgl_xzqh_service")
public class XzqhServiceImpl extends hc.orm.AbstractBasicOrmService<Xzqh, String> implements XzqhService {
	
	private transient static final Logger	logger	= LoggerFactory.getLogger(XzqhServiceImpl.class);
	
	@Autowired
	private XzqhDao		xzqhDao;
	
	@Autowired
	private SafetyManager	safetyManager;
	
	@Autowired
	private ExtPersonService extPersonService;
	
	@Autowired
	private JgxxService jgxxService;
	
	@Override
	@Transactional(readOnly = true)
	public Xzqh getById(String id) {
		return this.xzqhDao.getById(id);
	}

	@Override
	@Transactional
	public void insert(Xzqh entity) {
		this.xzqhDao.insert(entity);
	}

	@Override
	@Transactional
	public void update(Xzqh entity) {
		this.xzqhDao.update(entity);
	}

	@Override
	@Transactional
	public void delete(Xzqh entity) {
		this.xzqhDao.delete(entity);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Pageable<Xzqh> find(Pageable<Xzqh> pager, Xzqh condition) {
		
		return this.xzqhDao.findByPager(pager, condition);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Xzqh> findList(Xzqh condition) {
		return this.xzqhDao.findList(condition);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> findXzqhByCurrentUser() {
		Map<String, Object> xzqhMap = new HashMap<String, Object>();
		//人员所属区划用于默认查询当前要显示的视频数
		xzqhMap.put("xzqhdm", "");
		//人员下全部行政区划(如市、区县)用于点击查询当前要显示的视频数
		xzqhMap.put("xzqhList", new ArrayList<Xzqh>());
		
		//获取当前用户
		User user =	(User)safetyManager.getCurrentUser();
		if(null == user.getPersonId()){
			return xzqhMap;
		}
		//查找用户关联人员
		ExtPerson person = extPersonService.getById(user.getPersonId());
		if(null == person || null == person.getJgbh()){
			return xzqhMap;
		}
		//查找人员所属机构
		Jgxx jgxx = jgxxService.getById(person.getJgbh());
		if(null == jgxx || null == jgxx.getXzqhdm() || jgxx.getXzqhdm().trim().isEmpty()){
			return xzqhMap;
		}
		//行政区划代码
		String xzqhdm = jgxx.getXzqhdm();
		String xzqhdm2 = "";
		String qhdm = "";
		//市级
		if("00".equals(xzqhdm.substring(4, 6))){
			qhdm = xzqhdm.substring(0,4)+"%000";
			xzqhdm2 = xzqhdm.substring(0, 4)+"00000";
		}else{ //县级
			qhdm = xzqhdm.substring(0,6)+"000";
			xzqhdm2 = xzqhdm.substring(0, 6)+"00000";
		}
		Xzqh xzqh = new Xzqh();
		xzqh.setXzqhdm(qhdm);
		List<Xzqh> xzqhList = this.findList(xzqh);
		
		//人员所属区划用于默认查询当前要显示的视频数
		xzqhMap.put("xzqhdm", xzqhdm2);
		//人员下全部行政区划(如市、区县)用于点击查询当前要显示的视频数
		xzqhMap.put("xzqhList", xzqhList);
		
		return xzqhMap;
	}

	@Override
	public Xzqh getByXzqhdm(String xzqhdm) {
		return this.xzqhDao.getById(xzqhdm);
	}
	@Override
	public String findXzqhDmsByDqmc(String dqmc) {
		List<Xzqh> xzqhList = this.findList(new Xzqh(dqmc));
		StringBuffer sb = new StringBuffer();
		if(xzqhList.size() > 0 ){
			
			for (Xzqh xzqh : xzqhList) {
				sb.append(",\'").append(xzqh.getXzqhdm()).append("\'");
			}
		}
		return sb.length()>1 ? sb.substring(1):"";
	}
	
}
