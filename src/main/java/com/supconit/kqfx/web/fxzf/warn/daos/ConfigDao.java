package com.supconit.kqfx.web.fxzf.warn.daos;

import java.util.List;

import com.supconit.kqfx.web.fxzf.warn.entities.Config;

import hc.orm.BasicDao;

/**
 * ConfigDao
 * @author gs
 * @CreateTime 下午4:05:42
 */
public interface ConfigDao extends BasicDao<Config, String>{

	List<Config> findAll();

	List<Config> validateCode(Config config);

	Config getByCode(String code);

}
