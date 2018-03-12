package com.supconit.kqfx.web.fxzf.search.daos.Impl;

import java.util.List;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.fxzf.search.daos.FxzfSearchDao;
import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;


@Repository("taizhou_offsite_enforcement_searchFxzf_dao")
public class FxzfSearchDaoImpl extends AbstractBasicDaoImpl<Fxzf, String>
  implements FxzfSearchDao {
	
	private static final String NAMESPACE=Fxzf.class.getName();

	@Override
	public Pageable<Fxzf> findByPager(Pageable<Fxzf> pager, Fxzf condition) {
		return findByPager(pager, "selectPager", "countPager", condition);
	}

	@Override
	protected String getNamespace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}
	
	@Override
	public void batchInsert(List<Fxzf> fxzfList) {
		
		insert("batchInsert", fxzfList);
	}

	@Override
	public void checkFxzfMessage(Fxzf condition) {
		update("checkFxzfMessage",condition);
		
	}
	
	
	@Override
	public Fxzf getById(String id) {
		// TODO Auto-generated method stub
		return super.getById(id);
	}

	@Override
	public int getOverLoadStatusCount(Fxzf fxzf) {
		// TODO Auto-generated method stub
		return selectOne("getOverLoadStatusCount", fxzf);
	}

	@Override
	public int getIllegalLevelCount(Fxzf fxzf) {
		// TODO Auto-generated method stub
		return selectOne("getIllegalLevelCount", fxzf);
	}

	@Override
	public Pageable<Fxzf> findByPagerDetail(Pageable<Fxzf> pager, Fxzf condition) {
		// TODO Auto-generated method stub
		return findByPager(pager, "selectPagerDetail", "countPagerDetail", condition);
	}

	@Override
	public Integer getCountByCondition(Fxzf fxzf) {
		
		return selectOne("getCountByCondition", fxzf);
	}

	@Override
	public Integer getOverLoadCount(Fxzf fxzf) {
		
		return selectOne("getOverLoadCount", fxzf);
	}

	@Override
	public void deleteById(String id) {
		delete("deleteById", id);
		
	}

	@Override
	public List<Fxzf> getFxzfToTransport( ) {
		return selectList("getFxzfToTransport");
	}

	@Override
	public void upDateFxzftransport(Fxzf fxzf) {
		update("upDateFxzftransport", fxzf);
		
	}


	
	
}
