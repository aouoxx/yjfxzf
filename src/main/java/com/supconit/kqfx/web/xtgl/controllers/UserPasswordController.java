package com.supconit.kqfx.web.xtgl.controllers;

import hc.base.domains.AjaxMessage;
import hc.mvc.annotations.FormBean;
import hc.safety.manager.SafetyManager;

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

import com.supconit.honeycomb.business.authorization.entities.AuthenticationUser;
import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.honeycomb.business.authorization.services.UserService;
import com.supconit.kqfx.web.xtgl.entities.Jgxx;

@Controller
@RequestMapping("/xtgl/user")
public class UserPasswordController {
	
	private transient static final Logger	logger	= LoggerFactory.getLogger(UserPasswordController.class);
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private SafetyManager safetyManager;
	
	@Autowired
	private UserService	userService;


	@ModelAttribute("newAuthenticationUser")
	public User newAuthenticationUser() {
		return new AuthenticationUser();
	}
	
	@RequestMapping(value = "password", method = RequestMethod.GET)
	public <X extends Jgxx> String list(ModelMap model) {
		try {
			User user = (User) safetyManager.getAuthenticationInfo().getUser();
			model.put("username", user.getUsername());
			model.put("id", user.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/user/passwordedit";
	}
	
	@ResponseBody
	@RequestMapping(value = "passwordchange", method = RequestMethod.POST)
	public AjaxMessage doChange(@FormBean(value = "user", modelCode = "newAuthenticationUser") AuthenticationUser user){
		try {
			User newUser=userService.getById(user.getId());
			/**
			 * 验证密码
			 */
			if(!newUser.getPassword().equals(this.safetyManager.encryptedCredentials(user.getEmail(), null))){
				return AjaxMessage.success(1002);
			}
			String newPassword = user.getPassword();
			if(newUser != null){
				newUser.setPassword(this.safetyManager.encryptedCredentials(newPassword, null));
				}
			userService.update(newUser);
			return AjaxMessage.success(200);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
}
