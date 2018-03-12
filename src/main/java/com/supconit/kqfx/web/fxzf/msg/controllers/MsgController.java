package com.supconit.kqfx.web.fxzf.msg.controllers;

import hc.base.domains.AjaxMessage;
import hc.safety.manager.SafetyManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.honeycomb.business.authorization.services.RoleService;
import com.supconit.honeycomb.business.organization.services.DepartmentService;
import com.supconit.honeycomb.business.organization.services.PersonService;
import com.supconit.kqfx.web.fxzf.msg.entities.Msg;
import com.supconit.kqfx.web.fxzf.msg.services.MsgService;
import com.supconit.kqfx.web.fxzf.notify.services.NotifyService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnHistoryService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnInfoService;

@RequestMapping("/fxzf/msg")
@Controller("taizhou_offsite_enforcement_msg_controller")
public class MsgController {
	
	@Autowired
	private SafetyManager safetyManager;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private MsgService msgService;
	
	@Autowired 
	private NotifyService notifyService;
	
	@Autowired
	private WarnInfoService warnInfoService;
	
	@Autowired
	private WarnHistoryService warnHistoryService;
	
	private transient static final Logger logger = LoggerFactory.getLogger(MsgController.class);
	
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	 public AjaxMessage getMsg(HttpSession session){
		//获取登陆用户的userid;
		User user = (User) safetyManager.getAuthenticationInfo().getUser();
		//获取弹框是否显示的标志位	
		List<Msg> msgs = new ArrayList<Msg>();
		//删除所有用户中消息有效期大于当前时间的信息
		Date usefulDate = new Date();
		Msg objMsg = new Msg();
		objMsg.setUsefuldateTime(usefulDate);
		//objMsg.setUserId(String.valueOf(user.getId()));
		msgService.deleteByUsefulDate(objMsg);
		msgs = msgService.getByUserId(String.valueOf(user.getId()));
		//设置弹框是否显示的标志位
		for(Msg obj : msgs){
//			obj.setMsgflag(flag.getFlag());
			obj.setMsgflag((String) session.getAttribute(String.valueOf(user.getId())));
		}
		
		//过滤有效期大于当前时间信息
		if(!CollectionUtils.isEmpty(msgs)){
			return AjaxMessage.success(msgs);
		}else{
			return AjaxMessage.success("1002");
		}
		
		
	}
	
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public AjaxMessage deleteMsg(String id){
		//删除信息
		logger.info("===========删除信息===============");
		//获取登陆用户的userid;
		User user = (User) safetyManager.getAuthenticationInfo().getUser();
		Msg objMsg = new Msg();
		objMsg.setId(id);
		objMsg.setUserId(String.valueOf(user.getId()));
		msgService.deleteByUserId(objMsg);
		return AjaxMessage.success("200");
	}
	
	@ResponseBody
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public AjaxMessage updateMspFlag(String flag,HttpSession session){
		
		User user = (User) safetyManager.getAuthenticationInfo().getUser();
		session.setAttribute(String.valueOf(user.getId()), flag);
		return AjaxMessage.success("200");
	}
	
	@ResponseBody
	@RequestMapping(value = "verify", method = RequestMethod.POST)
	public AjaxMessage verifyUser(String jgid){
		return AjaxMessage.success("200");

	}
	
	
}
