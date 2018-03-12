package com.supconit.kqfx.web.fxzf.warn.services.Impl;

import hc.orm.AbstractBasicOrmService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.supconit.kqfx.web.fxzf.avro.redis_ConfigInfo;
import com.supconit.kqfx.web.fxzf.avro.redis.ChargeRedisService;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.warn.daos.ConfigDao;
import com.supconit.kqfx.web.fxzf.warn.entities.Config;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.service.time.TimeWebServiceImpl;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.SysConstants;

@Service("fxzf_warn_config_service")
public class ConfigServiceImpl extends AbstractBasicOrmService<Config, String> implements ConfigService{

	@Autowired
	private ConfigDao configDao;
	
	@Autowired WriteRedisService writeRedisService;
	
	@Autowired
	private TimeWebServiceImpl timeWebServiceImpl;
	
	@Autowired
	private ChargeRedisService chargeRedisService;
	
	@Override
	public Config getById(String arg0) {
		
		return this.configDao.getById(arg0);
	}

	@Override
	public void insert(Config entity) {
		this.configDao.insert(entity);
	}

	@Override
	public void update(Config entity) {
		this.configDao.update(entity);
	}

	@Override
	public void delete(Config entity) {
		this.configDao.delete(entity);
	}

	@Override
	public List<Config> findAll() {
		
		return this.configDao.findAll();
	}

	@Override
	public Boolean validateCode(Config config) {
		List<Config> result = this.configDao.validateCode(config);
		
		return CollectionUtils.isEmpty(result)?true:false;
	}

	@Override
	public Config getByCode(String code) {
		
		return this.configDao.getByCode(code);
	}

	@Override
	public Double getValueByCode(String code) {
		Config config = this.configDao.getByCode(code);
		return null!=config?config.getValue():0;
	}

	@Override
	public void initParameter() {
		try{
			Config config=configDao.getByCode(SysConstants.DOWN_FLOW);
			if(config==null){
				Config newconfig = new Config();
				newconfig.setName("称重下浮百分比");
				newconfig.setId(IDGenerator.idGenerator());
				newconfig.setValue( (double) 0);
				newconfig.setCode(SysConstants.DOWN_FLOW);
				insert(newconfig);
				config=newconfig;
			}
			//将下浮百分比写到Redis中
			redis_ConfigInfo configInfo = new redis_ConfigInfo();
			configInfo.setDOWNFLOW(String.valueOf(100-config.getValue()));
			/**
			 * 获取系统时间
			 */
			timeWebServiceImpl.synSystemTime();
			String time = chargeRedisService.readSynKey(SysConstants.PARAMETER1);
			configInfo.setSSystemtime(time);
			configInfo.setParameter2("0");
			configInfo.setParameter3("0");
			configInfo.setParameter4("0");
			configInfo.setParameter5("0");
			writeRedisService.WriteKeyToRedis(SysConstants.SYSTEM_PARAMETER, configInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
