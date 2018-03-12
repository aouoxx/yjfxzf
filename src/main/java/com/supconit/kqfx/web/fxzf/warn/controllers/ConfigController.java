package com.supconit.kqfx.web.fxzf.warn.controllers;

import hc.base.domains.AjaxMessage;
import hc.mvc.annotations.FormBean;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.kqfx.web.fxzf.avro.redis_ConfigInfo;
import com.supconit.kqfx.web.fxzf.avro.redis.ChargeRedisService;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.warn.entities.Config;
import com.supconit.kqfx.web.fxzf.warn.entities.ConfigCollections;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnHistoryService;
import com.supconit.kqfx.web.service.time.TimeWebServiceImpl;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.SysConstants;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

@RequestMapping("/fxzf/warn/config")
@Controller("fxzf_warn_config_controller")
public class ConfigController {
	
	private transient static final Logger logger = LoggerFactory.getLogger(ConfigController.class);
	
	private static final String MODULE_CODE = "FXZF_WARN_CONFIG";
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private WarnHistoryService	warnHistoryService;
	
	@Autowired
	private WriteRedisService writeRedisService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private TimeWebServiceImpl timeWebServiceImpl;
	
	@Autowired
	private ChargeRedisService chargeRedisService;
	/**
	 * 准备实体对象。
	 * 
	 * @return
	 */	
	@ModelAttribute("prepareConfig")
	public Config prepareConfig() {
		return new Config();
	}
	
	@ModelAttribute("ConfigCollections")
	public ConfigCollections ConfigCollections() {
		return new ConfigCollections();
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model){
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
							"告警配置信息", request.getRemoteAddr());
		List<Config> lists = configService.findAll();
		for(int i=0 ;i<lists.size();i++){
			model.put(lists.get(i).getCode(), lists.get(i).getValue());
		}
		return "fxzf/warn/config/list";
	}
	
	
	@Transactional
	@ResponseBody
	@RequestMapping(value = "config", method = RequestMethod.POST)
	AjaxMessage config(@FormBean(value = "condition", modelCode = "ConfigCollections") ConfigCollections conditions){
		
		this.systemLogService.log(MODULE_CODE, OperateType.update.getCode(),
				"告警配置信息", request.getRemoteAddr());
		for(Config config:conditions.getConfigs()){
			//检查T_CONFIG中是否含有该值
			String code = config.getCode();
			Config condition = configService.getByCode(code);
			if(code.equals(SysConstants.DOWN_FLOW)){
				if(condition!=null){
					//更新告警配置数据库
					config.setName(condition.getName());
					config.setId(condition.getId());
					configService.update(config);
				}else{
					config.setName("称重下浮百分比");
					config.setId(IDGenerator.idGenerator());
					configService.insert(config);
				}
				//将下浮百分比更新到Redis中
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
				writeRedisService.WriteKeyToRedis(SysConstants.SYSTEM_PARAMETER,configInfo );
			}else{
				if(condition!=null){
					//更新告警配置数据库
					config.setName(condition.getName());
					config.setId(condition.getId());
					configService.update(config);
				}else{
					//插入告警配置数据库
					if(code.equals(SysConstants.OVERLOAD_PERCENT)){
						config.setName("超限率");
					}
					if(code.equals(SysConstants.OVERLOAD_WEIGHT)){
						config.setName("超重");
					}
					if(code.equals(SysConstants.LENGTH)){
						config.setName("车长");
					}
					if(code.equals(SysConstants.WIDTH)){
						config.setName("车宽");
					}
					if(code.equals(SysConstants.HEIGHT)){
						config.setName("车高");
					}
					if(code.equals(SysConstants.QBB_STAY_TIME)){
						config.setName("情报板停留时间");
					}
					config.setId(IDGenerator.idGenerator());
					configService.insert(config);
			    }
			  }
			logger.info(config.toString());
		}
		return AjaxMessage.success("200");
	}
	
}
