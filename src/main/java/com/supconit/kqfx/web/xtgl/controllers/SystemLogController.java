/**
 * 
 */
package com.supconit.kqfx.web.xtgl.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pagination;
import hc.mvc.annotations.FormBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.honeycomb.business.authorization.entities.Menu;
import com.supconit.honeycomb.business.authorization.services.MenuService;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.entities.SystemLog;
import com.supconit.kqfx.web.xtgl.services.ExtPersonService;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;



/** ========================== 自定义区域结束 ========================== **/
/**
 * 系统日志控制层。
 * 
 * @author 
 * @create 2014-04-09 15:46:42 
 * @since 
 * 
 */
@Controller("xtgl_systemLog_controller")
@RequestMapping("/xtgl/system-log")
public class SystemLogController {
	

	/**
	 * 日志服务。
	 */
	private transient static final Logger	logger	= LoggerFactory.getLogger(SystemLogController.class);
	
	private static final String IP_WIN7_VALUE = "0:0:0:0:0:0:0:1";
	private static final String IP_LOCAL_VALUE = "127.0.0.1"; 

	/**
	 * 注入服务。
	 */
	@Autowired
	private SystemLogService						systemLogService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private ExtPersonService extPersonService;

	/**
	 * 准备实体对象。
	 * 
	 * @return
	 */	
	@ModelAttribute("prepareSystemLog")
	public SystemLog prepareSystemLog() {
		return new SystemLog();
	}
	
	/**
	 * 列表展现。
	 * 
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.put("operateType", OperateType.values());
		return "/business/systemManage/systemLog/list";
	}

	/**
	 * AJAX获取列表数据。
	 * 
	 * @param pager
	 *            分页信息
	 * @param condition
	 *            查询条件
	 * @return
	 */	
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public Pagination<SystemLog> list(Pagination<SystemLog> pager, @FormBean(value = "condition", modelCode = "prepareSystemLog") SystemLog condition) {
		try {
			/* Validate Pager Parameters */
			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE) return pager;
					
			if(StringUtil.isNotBlank(condition.getModuleCode()) && StringUtil.isNotEmpty(condition.getModuleCode())){
				Menu menu = this.menuService.getByCode(condition.getModuleCode());
				condition.setMenu(menu);
			}
			this.systemLogService.find(pager, condition); //获取分页查询结果
			Map<Long, ExtPerson> personMap = new HashMap<Long, ExtPerson>();
			Set<Long> personIdsSet = new HashSet<Long>();
			for (SystemLog systemLog : pager) {
				if(null != systemLog.getUser()){
					if(null != systemLog.getUser().getPersonId()){
						personIdsSet.add(systemLog.getUser().getPersonId());
					}
				}
			}
			List<ExtPerson> personList = this.extPersonService.findByIds(personIdsSet);
			for (ExtPerson extPerson : personList) {
				personMap.put(extPerson.getId(), extPerson);
			}
			for (SystemLog systemLog : pager) {
 				systemLog.setModuleStr(getMenuList(systemLog.getModuleCode()));
				systemLog.setOperateTypeStr(OperateType.getDescByCode(systemLog.getOperateType()));
				if (IP_WIN7_VALUE.equals(systemLog.getOperateIp())) {
					systemLog.setOperateIp(IP_LOCAL_VALUE);
				}
				if(StringUtil.isBlank(systemLog.getModuleStr())){
					systemLog.setModuleStr(systemLog.getModuleCode());
				}
				if(null != systemLog.getUser()){
					if(null != systemLog.getUser().getPersonId()){
						ExtPerson aPerson = personMap.get(systemLog.getUser().getPersonId());
						systemLog.setPerson(aPerson);
						systemLog.setName(aPerson.getName());
						systemLog.setUserName(systemLog.getUser().getUsername());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}
	
	/**
	 * 新增展示。
	 * 
	 * @param id
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String edit(ModelMap model) {
		return "/business/systemManage/systemLog/add";
	}
	
	/**
	 * 编辑展示。
	 * 
	 * @param id
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(required = true) String id,  ModelMap model) {
		model.put("systemLog", this.systemLogService.getById(id));
		return "/business/systemManage/systemLog/edit";
	}

	/**
	 * 保存编辑内容。
	 * 
	 * @param equipment
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"add", "edit"}, method = RequestMethod.POST)
	public AjaxMessage doEdit(@FormBean(value = "systemLog", modelCode = "prepareSystemLog") SystemLog systemLog) {
		// TODO Validate
		try {
			this.systemLogService.save(systemLog);
			return AjaxMessage.success(systemLog.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	/**
	 * 删除一条记录。
	 * 
	 * @param systemLog
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public AjaxMessage doDelete(SystemLog systemLog) {
		try {
			if (null == systemLog || null == systemLog.getId()) return AjaxMessage.error("错误的参数。");
			this.systemLogService.delete(systemLog);
			return AjaxMessage.success(systemLog.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	/**
	 * 查看展示。
	 * 
	 * @param id
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam(required = true) String id,  ModelMap model) {
		model.put("systemLog", this.systemLogService.getById(id));
		return "/business/systemManage/systemLog/view";
	}
	
		
	/** ========================== 自定义区域开始 ========================== **/
	/************************* 自定义区域内容不会被覆盖 *************************/
	
	private String getMenuList(String code){
		List<Menu> menuList = new ArrayList<Menu>();
		MenuNext(code, menuList);
		if(menuList.size() > 0){
			Collections.reverse(menuList);
			String moduleStr = "";
			for (Menu menu : menuList) {
				moduleStr += " > "+ menu.getName();
			}
			return moduleStr.substring(2);
		}else{
			return "";
		}
	}
	
	private void MenuNext(String code,List<Menu> menuList){
		Menu menu = menuService.getByCode(code);
		if(menu != null){
			menuList.add(menu);
			Menu parent = menuService.getById(menu.getPid());
			if(!Menu.CODE_FUNCTION.equals(parent.getCode())){
				MenuNext(parent.getCode(), menuList);
			}
		}
	}
	
	/** ========================== 自定义区域结束 ========================== **/
}