package com.supconit.kqfx.web.list.services.impl;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicOrmService;

import java.util.List;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.list.daos.WhiteListDao;
import com.supconit.kqfx.web.list.entities.WhiteList;
import com.supconit.kqfx.web.list.services.WhiteListService;


@Service("list_whitelist_service")
public class WhiteListServiceImpl extends AbstractBasicOrmService<WhiteList, String>
		implements WhiteListService {

	@Autowired
	private WhiteListDao	whiteListDao;
	
	@Override
	public WhiteList getById(String arg0) {
		
		return this.whiteListDao.getById(arg0);
	}

	@Override
	public void save(WhiteList arg0) {
		if(StringUtil.isEmpty(arg0.getId())) 
		{
			this.whiteListDao.insert(arg0);
		}else{
			this.whiteListDao.update(arg0);
		}
	}

	@Override
	public void insert(WhiteList entity) {
		this.whiteListDao.insert(entity);
	}

	@Override
	public void update(WhiteList entity) {
		this.whiteListDao.update(entity);
	}

	@Override
	public void delete(WhiteList entity) {
		this.whiteListDao.delete(entity);
	}

	@Override
	public List<WhiteList> getAll() {
		
		return this.whiteListDao.getAll();
	}

	@Override
	public Pageable<WhiteList> findByPager(Pageable<WhiteList> pager,
			WhiteList condition) {
		// TODO Auto-generated method stub
		return whiteListDao.findByPager(pager, condition);
	}

	@Override
	public int findByPlate(WhiteList condition) {
		// TODO Auto-generated method stub
		return whiteListDao.findByPlate(condition);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		 whiteListDao.deleteById(id);
	}

}
