package com.supconit.kqfx.web.xtgl.services.impl;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;

import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.kqfx.web.xtgl.daos.OnLineUserDao;
import com.supconit.kqfx.web.xtgl.entities.OnLineUser;
import com.supconit.kqfx.web.xtgl.services.OnLineUserService;


/**
 * @author 
 * @create 2014-05-06 15:21:36 
 * @since 
 * 
 */
@Service("wzzhgl_xtgl_onLineUser_service")
public class OnLineUserServiceImpl  extends hc.orm.AbstractBasicOrmService<OnLineUser, String> implements OnLineUserService  {

	@Autowired
	private OnLineUserDao onlineUserDao;
	@Override
	public OnLineUser getById(String arg0) {
			return null;
	}

	@Override
	public void save(OnLineUser arg0) {
		
	}

	@Override
	public void insert(OnLineUser entity) {
		
	}

	@Override
	public void update(OnLineUser entity) {
		
	}

	@Override
	public void delete(OnLineUser entity) {
		
	}

	@Override
	public Pageable<OnLineUser> find(Pagination<OnLineUser> pager,
			OnLineUser condition) {
		
		
		pager.setPageSize(1000);
//		 Pageable<OnLineUser> find=new Pagination<OnLineUser>();
		
		Iterator iter =condition.getOnLineUserMap().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			
			User val = (User) entry.getValue();
			OnLineUser online=new OnLineUser();
			online=this.onlineUserDao.getById(val.getId().toString());
			online.setIp(val.getAvatar());
			pager.add(online);
			
		}
		pager.setTotal(pager.size());
		return pager;
	}
	
	

}