package com.supconit.kqfx.web.fxzf.warn.daos.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.fxzf.warn.daos.WarnInfoDao;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnInfo;

@Repository("fxzf_warn_warninfo_dao")
public class WarnInfoDaoImpl extends AbstractBasicDaoImpl<WarnInfo, String> implements
		WarnInfoDao {
	
	private static final String	NAMESPACE	= WarnInfo.class.getName();

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public List<WarnInfo> getAll() {
		
		return selectList("getAll");
	}

	@Override
	public WarnInfo getByTempletTypeAndStation(WarnInfo warnInfo) {
		
		return this.selectOne("getByTempletTypeAndStation", warnInfo);
	}

}
