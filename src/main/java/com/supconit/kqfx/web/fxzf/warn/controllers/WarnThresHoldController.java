package com.supconit.kqfx.web.fxzf.warn.controllers;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;


@RequestMapping("/fxzf/warn/thresHold")
@Controller("fxzf_warn_threshold_controller")
public class WarnThresHoldController {
	
	private transient static final Logger logger = LoggerFactory.getLogger(ConfigController.class);
	
	private static final String MODULE_CODE = "FXZF_WARN_STANDARD";
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Resource
	private HttpServletRequest request;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model){
		logger.info("-----查看超限阀值页面----");
		this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(),
				"查看超限超限标准", request.getRemoteAddr());
		return "fxzf/warn/thresHold/view";
	}

}
