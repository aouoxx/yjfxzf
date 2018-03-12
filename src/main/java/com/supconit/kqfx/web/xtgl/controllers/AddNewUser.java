package com.supconit.kqfx.web.xtgl.controllers;

import hc.base.domains.AjaxMessage;
import hc.mvc.annotations.FormBean;
import hc.safety.manager.SafetyManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.honeycomb.business.authorization.constants.SettingConstants;
import com.supconit.honeycomb.business.authorization.entities.AuthenticationUser;
import com.supconit.honeycomb.business.authorization.entities.Role;
import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.honeycomb.business.authorization.services.RoleService;
import com.supconit.honeycomb.business.authorization.services.UserService;
import com.supconit.honeycomb.business.organization.services.DepartmentService;
import com.supconit.honeycomb.business.organization.services.PersonService;
import com.supconit.kqfx.web.fxzf.msg.daos.MsgDao;
import com.supconit.kqfx.web.fxzf.msg.entities.Msg;
import com.supconit.kqfx.web.fxzf.msg.entities.PushMsgFlag;
import com.supconit.kqfx.web.fxzf.msg.services.MsgService;
import com.supconit.kqfx.web.fxzf.msg.services.PushMsgFlagService;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.services.ExtPersonService;

/**
 * 
 * @author gs
 *
 */
@Controller
@RequestMapping(SettingConstants.PATH + "useradd")
public class AddNewUser {
	
	private transient static final Logger	logger	= LoggerFactory.getLogger(AddNewUser.class);

	@Autowired
	private UserService						userService;
	@Autowired
	private PersonService					personService;
	@Autowired
	private ExtPersonService 				extPersonService;
	@Autowired
	private DepartmentService				departmentService;
	@Autowired
	private RoleService						roleService;
	@Autowired
	private SafetyManager					safetyManager;
	@Autowired
	private MsgService                      msgService;
	@Autowired
	private MsgDao                          msgDao;
	@Autowired
	private PushMsgFlagService 				pushMsgFlagService;
	
	@Value("${enable}")
	private String enable;
	@Value("${disable}")
	private String disable;
	@Value("${notify}")
	private String notify;
	@Value("${pushwarn}")
	private String warn;
	@Value("${analysis}")
	private String analysis;
	
	
	@ModelAttribute("newUser")
	public User generateUser() {
		return new User();
	}

	@ModelAttribute("newAuthenticationUser")
	public User newAuthenticationUser() {
		return new AuthenticationUser();
	}
	
	
	@ResponseBody
	@RequestMapping(value = { "add", "edit" }, method = RequestMethod.POST)
	public AjaxMessage doEdit(@FormBean(value = "user", modelCode = "newAuthenticationUser") AuthenticationUser user,
			@RequestParam(value = "selectedRoleIds", required = false) String selectedRoleIds) {
		// TODO VALIDATE
		User sample = this.userService.getByUsername(user.getUsername());
		if (null != sample && (null == user.getId() || !user.getId().equals(sample.getId()))) return AjaxMessage.error("用户名已经存在");
		sample = this.userService.getByEmail(user.getEmail());
		if (null != sample && (null == user.getId() || !user.getId().equals(sample.getId()))) return AjaxMessage.error("电子邮件地址已经存在");

		// VALIDATE OK
		// 加密用户密码，采用SHA-1，无SALT
		if (StringUtil.isNotBlank(user.getPassword())) {
			user.setPassword(this.safetyManager.encryptedCredentials(user.getPassword(), user.getSalt()));
		} else {
			user.setPassword(null);// 不修改密码
		}

		if (StringUtil.isNotBlank(selectedRoleIds)) {
			String[] ids = StringUtil.split(selectedRoleIds, ",");
			Set<Long> set = new HashSet<Long>();
			for (String id : ids) {
				if (StringUtil.isBlank(id)) continue;
				set.add(Long.valueOf(id.trim()));
			}
			if (!set.isEmpty()) {
				Set<Role> roles = new HashSet<Role>(set.size());
				for (Long id : set) {
					roles.add(new Role(id));
				}
				user.setRoles(roles);
			} else {
				Set<Role> roles = Collections.emptySet();
				user.setRoles(roles);
			}
		} else {
			Set<Role> roles = Collections.emptySet();
			user.setRoles(roles);
		}

		try {
			this.userService.save(user);
			
			//获取关联的人员信息
			ExtPerson person = extPersonService.getById(user.getPerson().getId());
			User usernew = userService.getByUsername(user.getUsername());
			long jgid = person.getJgbh();
			//删除过时消息
			Date usefulDate = new Date();
			Msg objMsg = new Msg();
			objMsg.setUsefuldateTime(usefulDate);
			msgService.deleteByUsefulDate(objMsg);
			if(usernew!=null){
			String useid = String.valueOf(usernew.getId());
			if(jgid==133){
				//添加一桥的用户,获取未过时消息
				insertDialogMsg("133",useid);
			}else if(jgid==134){
				//添加二桥的用户，,获取未过时消息
				insertDialogMsg("134",useid);
			}else{
				//添加超级管理员的用户
				insertDialogMsg("133_134",useid);
			}
			
			//为新添加的用户设置手机推送标志信息 告警默认为N，其余为Y
			PushMsgFlag msgFlag = new PushMsgFlag();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			msgFlag.setId(IDGenerator.idGenerator());
			//设置用户ID
			msgFlag.setUserId(useid);
			//插入公告信息标记
			msgFlag.setFlagName(notify);
			msgFlag.setFlagValue(enable);
			msgFlag.setUpdateTime(format.format(date));
			//插入告警信息标记
			pushMsgFlagService.insert(msgFlag);
			msgFlag.setId(IDGenerator.idGenerator());
			msgFlag.setFlagName(warn);
			msgFlag.setFlagValue(disable);
			pushMsgFlagService.insert(msgFlag);
			//插入统计信息标记
			msgFlag.setId(IDGenerator.idGenerator());
			msgFlag.setFlagName(analysis);
			msgFlag.setFlagValue(enable);
			pushMsgFlagService.insert(msgFlag);
			}
			return AjaxMessage.success();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	
	private void insertDialogMsg(String jgid,String userid){
		List<Msg> msgs = msgService.getByUserId("133");
		if(!msgs.isEmpty()){
			List<Msg> dtos = new ArrayList<Msg>();
			for(Msg msg : msgs){
				Msg dto = new Msg();
				dto.setContent(msg.getContent());
				dto.setFlag(msg.getFlag());
				dto.setId(msg.getId());
				dto.setJgid(jgid);
				dto.setMsgflag(msg.getMsgflag());
				dto.setPid(msg.getPid());
				dto.setUpdateTime(msg.getUpdateTime());
				dto.setUsefuldateTime(msg.getUsefuldateTime());
				dto.setUserId(userid);
				dtos.add(dto);
			}
			msgDao.batchInert(dtos);
		}
	}
	

	
	
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public AjaxMessage delete(@RequestParam("id") Long id) {
		User user = this.userService.getById(id);
		if (null == user) return AjaxMessage.error("错误的用户ID");
		// 当前登录用户不能删除
		User currentUser = (User) this.safetyManager.getCurrentUser();
		if (currentUser.getId().equals(id)) return AjaxMessage.error("当前用户不能删除.");
		// TODO 有业务，如流程在身的不能删除，这个可以用BusinessTrigger实现。
		try {
			this.userService.delete(user);
			//删除与该用户相关的公告和告警信息
			Msg msg = new Msg();
			msg.setUserId(String.valueOf(id));
			msgService.deleteByUserId(msg);
			//删除与该用户相关的信息推送标志位信息
			pushMsgFlagService.deleteByUserId(String.valueOf(id));
			//删除与该用户相关联的设备ID信息
			pushMsgFlagService.deleteChannelByUserId(String.valueOf(id));
			return AjaxMessage.success();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	
}
