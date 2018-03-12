package com.supconit.kqfx.web.fxzf.search.daos.Impl;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.fxzf.search.daos.FxzfStaticDao;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfStatic;

/**
 * 
 * @author JNJ
 * @time 2016年12月8日10:14:49
 * 
 */
@Repository
public class FxzfStaticDaoImpl extends AbstractBasicDaoImpl<FxzfStatic, String> implements FxzfStaticDao {

	private static final String NAMESPACE = FxzfStatic.class.getName();

	@Override
	public Pageable<FxzfStatic> findByPager(Pageable<FxzfStatic> pager, FxzfStatic condition) {
		return findByPager(pager, "selectPager", "countPager", condition);
	}

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public void batchInsert(List<FxzfStatic> fxzfList) {
		insert("batchInsert", fxzfList);
	}

	@Override
	public void checkFxzfMessage(FxzfStatic condition) {
		update("checkFxzfMessage", condition);

	}

	@Override
	public FxzfStatic getById(String id) {
		return super.getById(id);
	}

	@Override
	public int getOverLoadStatusCount(FxzfStatic fxzfStatic) {
		return selectOne("getOverLoadStatusCount", fxzfStatic);
	}

	@Override
	public int getIllegalLevelCount(FxzfStatic fxzfStatic) {
		return selectOne("getIllegalLevelCount", fxzfStatic);
	}

	@Override
	public Pageable<FxzfStatic> findByPagerDetail(Pageable<FxzfStatic> pager, FxzfStatic condition) {
		return findByPager(pager, "selectPagerDetail", "countPagerDetail", condition);
	}

	@Override
	public Integer getCountByCondition(FxzfStatic fxzfStatic) {
		return selectOne("getCountByCondition", fxzfStatic);
	}

	@Override
	public Integer getOverLoadCount(FxzfStatic fxzfStatic) {
		return selectOne("getOverLoadCount", fxzfStatic);
	}

	@Override
	public void deleteById(String id) {
		delete("deleteById", id);
	}

	@Override
	public List<FxzfStatic> getFxzfToTransport() {
		return selectList("getFxzfToTransport");
	}

	@Override
	public void upDateFxzftransport(FxzfStatic fxzfStatic) {
		update("upDateFxzftransport", fxzfStatic);
	}

}
