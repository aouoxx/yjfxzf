package com.supconit.kqfx.web.xtgl.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.mvc.annotations.FormBean;
import hc.mvc.controllers.AbstractXssFilterController;
import hc.safety.manager.SafetyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.supconit.honeycomb.business.authorization.entities.Role;
import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.honeycomb.business.authorization.services.RoleService;
import com.supconit.honeycomb.business.authorization.services.UserService;
import com.supconit.honeycomb.business.organization.services.PersonService;
import com.supconit.honeycomb.mvc.exceptions.EntityNotFoundException;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.entities.ExtUser;
import com.supconit.kqfx.web.xtgl.entities.ExtUserCondition;
import com.supconit.kqfx.web.xtgl.entities.Jgxx;
import com.supconit.kqfx.web.xtgl.services.ExtUserService;
import com.supconit.kqfx.web.xtgl.services.JgxxService;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;
/**
 * 修改： 增加用户密码修改
 * @author gs
 *
 */
@Controller
@RequestMapping("/xtgl/zhgl/user")
public class ExtUserController extends AbstractXssFilterController {

	private transient static final Logger	logger	= LoggerFactory.getLogger(ExtUserController.class);
	private static final String MODULE_CODE = "XTGL_ZHGL_USER";

	@Autowired
	private ExtUserService					extUserService;
	@Autowired
	private PersonService					personService;
	@Autowired
	private RoleService 		roleService;
	@Autowired
	private JgxxService			jgxxService;
	@Resource
	private HttpServletRequest request;
	@Autowired
	private SystemLogService systemLogService;
	@Autowired
	private SafetyManager safetyManager;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public <X extends ExtPerson> String list(ModelMap model) {
		try {
			Jgxx jgxxTree = this.jgxxService.getTree();
			model.put("jgxxJson", JSON.toJSONString(jgxxTree));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/user/list";
	}
	
	/**
	 * 列表页面
	 * 
	 * @param pager
	 * @param condition
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "list", "select" }, method = RequestMethod.POST)
	public Pageable<ExtUser> dolist(Pagination<ExtUser> pager, @FormBean(value = "condition") ExtUserCondition condition, ModelMap model) {
		try {
			if (null != condition.getJgbh()) {
				Jgxx jgxx = this.jgxxService.getById(condition.getJgbh());
				if (null == jgxx) 
					return pager;
				condition.setJgxx(jgxx);
			}
			this.extUserService.find(pager, condition);

			if (!pager.isEmpty() && (null == condition.getNotAssigned() || !condition.getNotAssigned())) {
				Set<Long> set = new HashSet<Long>();
				for (ExtUser u : pager) {
					if (null != u.getPersonId()){ 
						set.add(u.getPersonId());
					}
					List<ExtUser> extUserList=this.extUserService.findRoleByUserId(u);
					
					String roleName="";
					
					for(int i=0;i<extUserList.size();i++){
						if(extUserList.get(i) !=null){
							roleName+=extUserList.get(i).getRoleName()+",";
						}
						
						
					}
					u.setRoleName(roleName);
					
					if(!StringUtil.isEmpty(u.getUsername()))
					{
						u.setUsername(StringEscapeUtils.unescapeHtml(u.getUsername()));
					}

					if(!StringUtil.isEmpty(u.getEmail()))
					{
						u.setEmail(StringEscapeUtils.unescapeHtml(u.getEmail()));
					}

					if(!StringUtil.isEmpty(u.getDescription()))
					{
						u.setDescription(StringEscapeUtils.unescapeHtml(u.getDescription()));
					}
					
				}
				if (!set.isEmpty()) {
					List<ExtPerson> persons = personService.findByIds(set);
					if (!persons.isEmpty()) {
						Map<Long, ExtPerson> personMap = new HashMap<Long, ExtPerson>(persons.size());
						for (ExtPerson p : persons) {
							personMap.put(p.getId(), p);
						}
						for (ExtUser u : pager) {
							if (null != u.getPersonId()) {
								u.setPerson(personMap.get(u.getPersonId()));
							}
						}
					}
				}
			}
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"用户列表查询", request.getRemoteAddr());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}
	
	/**
	 * 进入修改页面
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "add", "edit" }, method = RequestMethod.GET)
	public String edit(@ModelAttribute("newUser") ExtUser user, ModelMap model) {
		try {
			if (null != user.getId()) {
				user = this.extUserService.getById(user.getId());
				if (null == user) { throw new EntityNotFoundException(); }
				// 获取已经有的Role
				List<Role> assignedRoles = this.roleService.findAssigned(user.getId());
				List<String> assignedRolesList=new ArrayList<String>();
				for(int i=0;i<assignedRoles.size();i++){
					assignedRolesList.add(assignedRoles.get(i).getId().toString());
					
				}
				
				
				model.put("assignedRoles", assignedRoles);
				model.put("assignedRolesList", JSON.toJSONString(assignedRolesList));
			} else {
				model.put("_type", "add");
			}
			if (null != user.getPersonId() && null == user.getPerson()) {
				user.setPerson(this.personService.getById(user.getPersonId()));
			}
			model.put("user", user);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		Role role=new Role();
		
		List<Role> list=this.roleService.find(role);
	
		model.put("json", JSON.toJSONString(list));
		return "/business/systemManage/accountManage/user/edit";
	}
	
	
	/**
	 * 查看用户详细信息
	 * 
	 * @param person
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "view" }, method = RequestMethod.GET)
	public String view(String id, ModelMap model) {
		
		ExtUser user =this.extUserService.getById(Long.parseLong(id));
		
		if (null == user) { throw new EntityNotFoundException(); }
		
		
		List<ExtUser> extUserList=this.extUserService.findRoleByUserId(user);
		
		String roleName="";
		
		for(int i=0;i<extUserList.size();i++){
			if(extUserList.get(i) != null){
				roleName+=extUserList.get(i).getRoleName()+",";
			}
		}
		user.setRoleName(roleName);
		
		
		Set<Long> set = new HashSet<Long>();
		set.add(user.getPersonId());
		
		List<ExtPerson> persons = personService.findByIds(set);
		
		if (!persons.isEmpty()) {
			Map<Long, ExtPerson> personMap = new HashMap<Long, ExtPerson>(persons.size());
			for (ExtPerson p : persons) {
				personMap.put(p.getId(), p);
			}

			user.setPerson(personMap.get(user.getPersonId()));
				
		}
		
		model.put("user", user);
		
		return "/business/systemManage/accountManage/user/view";
	}
	
	
	
	/**
	 * 跳转到用户修改密码页面
	 * @return
	 */
	@RequestMapping(value="gotoModifypwd",method=RequestMethod.GET)
	public String gotoModifypwd(ModelMap model){
		User currentUser = (User) this.safetyManager.getCurrentUser();
		model.addAttribute("user",currentUser);
		logger.debug("turn to modify pwd page.");
		return "/business/systemManage/accountManage/user/modify_password";
	}
	/**
	 * 修改用户密码
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @param id 用户id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("modifyPwd")
	@Transactional
	public AjaxMessage modifyPwd(String oldPwd,String newPwd,Long id) {
		try{
			User user = this.extUserService.getById(id);
			if(safetyManager.encryptedCredentials(oldPwd, null).equals(user.getPassword())){
				user.setPassword(safetyManager.encryptedCredentials(newPwd, null));
				this.extUserService.update((ExtUser) user);
				this.userService.save(user);
				return AjaxMessage.success();
			}else{
				return AjaxMessage.error("密码错误！");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return AjaxMessage.error("密码修改失败，请联系管理员！");
	}
	/**
	 * 通用选用户
	 * 
	 * @param muilt
	 * @param selected
	 * @param condition
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "select", method = RequestMethod.GET)
	public String select(@RequestParam(defaultValue = "false") boolean muilt,
			@RequestParam(required = false) String selected,
			ExtUserCondition condition, ModelMap model) {
		try {
			Jgxx jgxxTree = this.jgxxService.getTree();
			model.put("jgxxJson", JSON.toJSONString(jgxxTree));
			model.put("muilt", muilt);
			model.put("condition", condition);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/user/select";
	}

	
	
	
	/**
	 * 通用选用户(多选)
	 * 
	 * @param muilt
	 * @param selected
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "multSelectUserToRole", method = RequestMethod.GET)
	public <X extends ExtUser> String multSelectUserToRole(String roleId,ModelMap model) {
		try {
			Jgxx jgxxTree = this.jgxxService.getTree();
			model.put("jgxxJson", JSON.toJSONString(jgxxTree));
			model.put("roleId", roleId);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/user/multSelect";
	}
	
	
	
	
}
