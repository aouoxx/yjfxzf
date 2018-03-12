package com.supconit.kqfx.web.list.daos.impl;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.list.daos.WhiteListDao;
import com.supconit.kqfx.web.list.entities.WhiteList;

@Repository("list_whitelist_dao")
public class WhiteListDaoImpl extends AbstractBasicDaoImpl<WhiteList, String> implements
	WhiteListDao {
	
	private static final String	NAMESPACE	= WhiteList.class.getName();
	

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}


	@Override
	public List<WhiteList> getAll() {
		
		return this.selectList("getAll");
	}


	@Override
	public Pageable<WhiteList> findByPager(Pageable<WhiteList> pager,
			WhiteList condition) {
		return findByPager(pager, "selectPager", "countPager", condition);
	}


	@Override
	public int findByPlate(WhiteList condition) {
		// TODO Auto-generated method stub
		return selectOne("findByPlate", condition);
	}

	@Override
	public void deleteById(String id) {
		update("deleteById", id);
		
	}
}
