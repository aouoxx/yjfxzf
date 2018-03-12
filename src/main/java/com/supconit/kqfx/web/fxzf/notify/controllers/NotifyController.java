package com.supconit.kqfx.web.fxzf.notify.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pagination;
import hc.mvc.annotations.FormBean;
import hc.safety.manager.SafetyManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.honeycomb.business.authorization.services.RoleService;
import com.supconit.honeycomb.business.organization.services.DepartmentService;
import com.supconit.honeycomb.business.organization.services.PersonService;
import com.supconit.kqfx.web.fxzf.msg.services.MsgService;
import com.supconit.kqfx.web.fxzf.notify.entities.Notify;
import com.supconit.kqfx.web.fxzf.notify.services.NotifyService;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;


@Controller
@RequestMapping("/fxzf/notify")
public class NotifyController {
	
	private transient static final Logger logger = LoggerFactory
			.getLogger(NotifyController.class);
	
	private static final String MODULE_CODE = "FXZF_NOFITY";
	
	@Autowired
	private NotifyService notifyService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private MsgService msgService;
	
	@Autowired
	private SafetyManager safetyManager;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private PersonService personService;
	
	/**
	 * 违章车轨迹实体类
	 * @return
	 */
	@ModelAttribute("Notify")
	private Notify getNotify(){
		Notify notify = new Notify();
		return notify;
	}
	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String display(ModelMap map,
			@ModelAttribute("Notify") Notify notify) {
		logger.info("----------------------非现数据查询列表---------------");
		this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(),
				"查看公告列表", request.getRemoteAddr());
		try {
			User user = (User) safetyManager.getAuthenticationInfo().getUser();
			if(null!=user&&null!=user.getPerson()&&null!=user.getPersonId())
			{
				ExtPerson person = personService.getById(user.getPersonId());
				//根据JGID进行权限限制
				//若是超级管理员查询JGID = null
				//大桥的为 JGID = 133
				// 二桥为JGID=134
				map.put("jgid", person.getJgbh());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return "/fxzf/notify/list";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	 public Pagination<Notify> list(Pagination<Notify> pager,
			 @FormBean(value = "condition", modelCode = "Notify") Notify condition) {
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"查询公告列表", request.getRemoteAddr());
			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
			notifyService.findByPager(pager, condition);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(ModelMap map,
			@ModelAttribute("Notify") Notify notify) {
		logger.info("----------------------添加公告---------------");
		User user = (User) safetyManager.getAuthenticationInfo().getUser();
		if(null!=user&&null!=user.getPerson()&&null!=user.getPersonId())
		{
			ExtPerson person = personService.getById(user.getPersonId());
			//根据JGID进行权限限制
			//若是超级管理员查询JGID = null
			//大桥的为 JGID = 133
			// 二桥为JGID=134
			map.put("jgid", person.getJgbh());
		}
		return "/fxzf/notify/add";
	}
	
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	 public AjaxMessage add(
			 @FormBean(value = "condition", modelCode = "Notify") Notify condition){
		this.systemLogService.log(MODULE_CODE, OperateType.send.getCode(),
				"发布公告", request.getRemoteAddr());
		condition.setId(IDGenerator.idGenerator());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		format.format(date);
		condition.setUpdateTime(date);
		notifyService.insert(condition);
		//暂时设置jgid
		String jgid="";
		if(condition.getRange().equals("一桥"))
		{
			jgid="133";
		}else if(condition.getRange().equals("二桥")){
			jgid="134";
		}else if(condition.getRange().equals("一桥,二桥")){
			jgid="133,134";
		}
		return AjaxMessage.success(200);
	}
	
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(ModelMap map,
			@ModelAttribute("Notify") Notify notify) {
		logger.info("----------------------查看公告---------------");
		this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(),
				"查看公告详情", request.getRemoteAddr());
		notify = notifyService.getById(notify.getId());
		map.put("condition", notify);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sendhour = format.format(notify.getUpdateTime());
		String endhour = format.format(notify.getEndDateTime());
		map.put("send", sendhour);
		map.put("end", endhour);
		
		return "/fxzf/notify/view";
	}
}
