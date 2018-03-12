package com.supconit.kqfx.web.xtgl.controllers;


import hc.base.domains.Pagination;
import hc.mvc.annotations.FormBean;

import java.util.HashMap;

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
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.entities.OnLineUser;
import com.supconit.kqfx.web.xtgl.listener.OnLineUserListener;
import com.supconit.kqfx.web.xtgl.services.OnLineUserService;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;



/** ========================== 自定义区域结束 ========================== **/
/**
 * 在线用户控制层。
 * 
 * @author 
 * @create 2014-05-06 15:21:36 
 * @since 
 * 
 */
@Controller("wzzhgl_xtgl_onLineUser_controller")
@RequestMapping("/xtgl/onLineUser")
public class OnLineUserController{

	/**
	 * 日志服务。
	 */
	private transient static final Logger	logger	= LoggerFactory.getLogger(OnLineUserController.class);
	private static final String MODULE_CODE = "XTGL_XTJS_ONLINE_USER";
	/**
	 * 注入服务。
	 */
	@Autowired
	private OnLineUserService						onLineUserService;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private SystemLogService systemLogService;
	
	/**
	 * 准备实体对象。
	 * 
	 * @return
	 */	
	@ModelAttribute("prepareOnLineUser")
	public OnLineUser prepareOnLineUser() {
		return new OnLineUser();
	}
	
	/**
	 * 列表展现。
	 * 
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"在线用户列表页面", request.getRemoteAddr());
		return "/business/systemManage/onLineUser/list";
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
	public Pagination<OnLineUser> list(Pagination<OnLineUser> pager, @FormBean(value = "condition", modelCode = "prepareOnLineUser") OnLineUser condition) {
		
		if (pager.getPageNo() < 1 || pager.getPageSize() < 1
				|| pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
			return pager;
		
		HashMap<String,User> onLineUserListMap=new HashMap<String, User>();
		onLineUserListMap=OnLineUserListener.getOnLineUserMap();
		condition.setOnLineUserMap(onLineUserListMap);
		
		this.onLineUserService.find(pager, condition);
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"在线用户列表查询", request.getRemoteAddr());
		return pager;
	}
	


	
	
		
	/** ========================== 自定义区域开始 ========================== **/
	/************************* 自定义区域内容不会被覆盖 *************************/
	
	
	
	/** ========================== 自定义区域结束 ========================== **/
}