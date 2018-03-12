package com.supconit.kqfx.web.fxzf.search.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.fxzf.search.daos.ZczTreeDao;
import com.supconit.kqfx.web.fxzf.search.entities.ZczTree;
import com.supconit.kqfx.web.fxzf.search.services.ZczTreeService;

@Service("kqfxzf_kqfx_zczTree_service")
public class ZczTreeServiceImpl implements ZczTreeService {

	@Autowired
	private ZczTreeDao zczTreeDao;
	
	@Override
	public ZczTree getById(Long arg0) {
		return zczTreeDao.getById(arg0);
	}

	@Override
	public void save(ZczTree arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insert(ZczTree entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ZczTree entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ZczTree entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ZczTree> getAllNodes(ZczTree node) {
		return zczTreeDao.getAllNodes(node);
	}

}
