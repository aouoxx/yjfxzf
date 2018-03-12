package com.supconit.kqfx.web.fxzf.warn.daos.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.fxzf.warn.daos.ConfigDao;
import com.supconit.kqfx.web.fxzf.warn.entities.Config;

@Repository("fxzf_warn_config_dao")
public class ConfigDaoImpl extends AbstractBasicDaoImpl<Config, String> implements
		ConfigDao {

	
	private static final String	NAMESPACE	= Config.class.getName();
	
	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public List<Config> findAll() {
		
		return selectList("findAll");
	}

	@Override
	public List<Config> validateCode(Config config) {
		
		return selectList("validateCode", config);
	}

	@Override
	public Config getByCode(String code) {
		
		return selectOne("getByCode", code);
	}


}
