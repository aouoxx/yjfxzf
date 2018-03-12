package com.supconit.kqfx.web.fxzf.warn.daos.Impl;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.fxzf.warn.daos.WarnHistoryDao;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnHistory;

@Repository("taizhou_offsite_enforcement_warnHistory_dao")
public class WarnHistoryDaoImpl  extends AbstractBasicDaoImpl<WarnHistory, String> implements WarnHistoryDao {

	private static final String NAMESPACE=WarnHistory.class.getName();

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public Pageable<WarnHistory> findByPager(Pageable<WarnHistory> pager,
			WarnHistory condition) {
		return findByPager(pager, "selectPager", "countPager", condition);
	}

	@Override
	public List<WarnHistory> findAgainTimeData(String license,
			String plateColor, Double againTime) {
		Map<String,Object> para = new HashMap<String,Object>();
		para.put("license", license);
		para.put("plateColor", plateColor);
		para.put("againTime", againTime);
		
		return selectList("findAgainTimeData", para);
	}

	@Override
	public List<WarnHistory> AnalysisDayWarnByCondition(WarnHistory condition) {
		// TODO Auto-generated method stub
		return selectList("AnalysisDayWarnByCondition", condition);
	}

	@Override
	public Pageable<WarnHistory> findByPagerExport(Pageable<WarnHistory> pager,
			WarnHistory condition) {
		// TODO Auto-generated method stub
		return findByPager(pager, "selectPagerExport", "countPagerExport", condition);
	}

	@Override
	public List<WarnHistory> getWarnByFxzfId(String fxzfid) {
		// TODO Auto-generated method stub
		return selectList("getWarnByFxzfId", fxzfid);
	}

	@Override
	public WarnHistory getWarnHistoryQbb(Fxzf warn) {
		// TODO Auto-generated method stub
		return selectOne("getWarnHistoryQbb", warn);
	}
}
