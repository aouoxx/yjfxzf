package com.supconit.kqfx.web.fxzf.warn.services;

import hc.orm.BasicOrmService;

import java.util.List;

import com.supconit.kqfx.web.fxzf.warn.entities.Config;

public interface ConfigService extends BasicOrmService<Config, String>{
	
	/**
	 * 查找全部配置项实体
	 * @return
	 */
	List<Config> findAll();
	
	/**
	 * 验证Code唯一
	 * @param config
	 * @return
	 */
	Boolean validateCode(Config config);
	
	/**
	 * 根据code查询配置项信息
	 * @param code 配置项编码
	 * @return
	 */
	Config getByCode(String code);
	
	/**
	 * 根据code查询配置项信息数值
	 * @param code 配置项编码
	 * @return 配置项数值
	 */
	Double getValueByCode(String code);
	
	/**
	 * 初始化系统变量
	 */
	void initParameter();
	
}
