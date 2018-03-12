package com.supconit.kqfx.web.list.daos.impl;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.list.daos.BlackListDao;
import com.supconit.kqfx.web.list.entities.BlackList;

@Repository("list_blacklist_dao")
public class BlackListDaoImpl extends AbstractBasicDaoImpl<BlackList, String> implements
		BlackListDao {
	
	private static final String	NAMESPACE	= BlackList.class.getName();
	
	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public List<BlackList> getAll() {
		
		return this.selectList("getAll");
	}

	@Override
	public Pageable<BlackList> findByPager(Pageable<BlackList> pager,
			BlackList condition) {
		return findByPager(pager, "selectPager", "countPager", condition);
	}

	@Override
	public void upDateByOverLoadTimes(BlackList condition) {
		update("OverLoadTimes",condition);
		
	}

	@Override
	public void deleteById(String id) {
		update("deleteById", id);
		
	}

	@Override
	public int findByPlate(BlackList condition) {
		// TODO Auto-generated method stub
		return selectOne("findByPlate", condition);
	}
	
	@Override
	public void batchDeleteBlackList(List<String> ids) {
		delete("batchDeleteBlackList", ids);
		
	}

	@Override
	public void batchInertBlackList(List<BlackList> lists) {
		insert("batchInertBlackList",lists);
		
	}

	@Override
	public void deleteAllNoHandler() {
		delete("deleteAllNoHandler");
		
	}

	@Override
	public List<BlackList> getAllHandler() {
		// TODO Auto-generated method stub
		return selectList("getAllHandler");
	}

	@Override
	public void updateHandlerOverTimes(BlackList condition) {
		update("updateHandlerOverTimes", condition);
		
	}

	@Override
	public BlackList findByPlateAndColor(BlackList condition) {
		// TODO Auto-generated method stub
		return selectOne("findByPlateAndColor", condition);
	}
	
	

}
