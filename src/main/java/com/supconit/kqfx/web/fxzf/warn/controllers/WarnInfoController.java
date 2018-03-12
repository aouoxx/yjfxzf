package com.supconit.kqfx.web.fxzf.warn.controllers;

import hc.base.domains.AjaxMessage;
import hc.business.dic.domains.FlatData;
import hc.business.dic.services.DataDictionaryService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnInfo;
import com.supconit.kqfx.web.fxzf.warn.services.WarnInfoService;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

@RequestMapping("/fxzf/warn/warninfo")
@Controller("fxzf_warn_warninfo_controller")
public class WarnInfoController {
	
	private transient static final Logger logger = LoggerFactory.getLogger(WarnInfoController.class);

	private static final String MENU_CODE = "FXZF_WARN_TEMPLET";
	
	private static final String DIC_CODE = "WARN_INFO_TYPE";
	
	@Autowired
	private WarnInfoService	warnInfoService;
	@Autowired
	private DataDictionaryService	dataDictionaryService;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private SystemLogService	systemLogService;

	/**
	 * 准备实体对象。
	 * 
	 * @return
	 */	
	@ModelAttribute("prepareWarnInfo")
	public WarnInfo prepareWarnInfo() {
		return new WarnInfo();
	}
	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model){

		List<WarnInfo> warnInfoList = this.warnInfoService.getAll();
		FlatData data = (FlatData) this.dataDictionaryService.getByCode(DIC_CODE);
		
		model.put("typeInfo", data);
		model.put("warnInfoList", JSON.toJSONString(warnInfoList));

		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"告警模板信息页面", request.getRemoteAddr());
		return "fxzf/warn/warninfo/list";
	}
	
	/**
	 * 保存编辑内容。
	 * 
	 * @param equipment
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@Transactional
	public AjaxMessage doEdit(String[] warnInfoId,String[] detectStation,String[] warnType,String[] qbbTempletId,String[] templetInfo) {
		try {
			List<WarnInfo> warnInfo = new ArrayList<WarnInfo>();
			if(!ArrayUtils.isEmpty(detectStation))
			{
				WarnInfo info = null;
				for (int i = 0; i < templetInfo.length; i++) {
					info = new WarnInfo();
					info.setDeleted(0);
					info.setId(warnInfoId.length>i?warnInfoId[i]:null);
					info.setDetectStation(detectStation[i]);
					info.setWarnType(warnType[i]);
					info.setTempletInfo(templetInfo.length>i?templetInfo[i]:null);
					info.setQbbTempletId(qbbTempletId.length>i?qbbTempletId[i]:null);
					warnInfo.add(info);
				}
			}
			
			if(!CollectionUtils.isEmpty(warnInfo))
			{
				for (WarnInfo warnInfo2 : warnInfo) {
					if(StringUtil.isEmpty(warnInfo2.getId()))
					{
						warnInfo2.setId(IDGenerator.idGenerator());
						this.warnInfoService.insert(warnInfo2);
					}else{
						this.warnInfoService.update(warnInfo2);
					}
				}
			}
			this.systemLogService.log(MENU_CODE, OperateType.update.getCode(),
					"告警模板信息配置", request.getRemoteAddr());
			return AjaxMessage.success();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.systemLogService.log(MENU_CODE, OperateType.update.getCode(),
					"告警模板信息配置", request.getRemoteAddr());
			return AjaxMessage.error(e.getMessage());
		}
	}
	
}
