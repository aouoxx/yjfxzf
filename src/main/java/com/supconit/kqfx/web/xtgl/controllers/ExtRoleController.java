package com.supconit.kqfx.web.xtgl.controllers;

import hc.base.domains.Pagination;
import hc.mvc.annotations.FormBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.supconit.honeycomb.business.authorization.entities.Menu;
import com.supconit.honeycomb.business.authorization.entities.Operate;
import com.supconit.honeycomb.business.authorization.entities.Role;
import com.supconit.honeycomb.business.authorization.services.MenuService;
import com.supconit.honeycomb.business.authorization.services.OperateService;
import com.supconit.honeycomb.business.authorization.services.RoleService;
import com.supconit.honeycomb.mvc.exceptions.EntityNotFoundException;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 角色管理Controller
 * 
 * @author huxiaolong
 *
 */
@Controller
@RequestMapping("/xtgl/qxgl/role")
public class ExtRoleController {
	
	private transient static final Logger	logger	= LoggerFactory.getLogger(ExtRoleController.class);
	private static final String MODULE_CODE = "XTGL_ZHGL_ROLE";
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private OperateService operateService;
	
	@Resource
	private HttpServletRequest request;
	@Autowired
	private SystemLogService systemLogService;
	
	/**
    * 新增角色信息
    *
    * @param model
    * @return
    */
	@RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(ModelMap model) {
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"角色管理添加页面", request.getRemoteAddr());
        return "/business/systemManage/authManage/role/add";
    }
	
	/**
    * 修改角色信息
    * @param id
    * @param model
    * @return
    */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(
        @RequestParam(required = true)
    Long id, ModelMap model) {
        Role role = this.roleService.getById(id);
        model.put("role", role);
        this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"角色管理修改页面", request.getRemoteAddr());
        return "/business/systemManage/authManage/role/edit";
    }
    
	@RequestMapping("list")
	public String list(ModelMap model, @FormBean(value = "condition", required = false) Role condition) {
		try {
			List<Role> roles = this.roleService.find(condition);
			model.put("condition", condition);
			model.put("roles", roles);
			model.put("roleJson", JSON.toJSONString(roles));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"角色管理列表页面", request.getRemoteAddr());
		return "/business/systemManage/authManage/role/list";
	}

	private void handleAssignMenu(Map<Long, List<Operate>> operateMap, Menu menu) {
		if (null == menu)
			return;
		List<Operate> list = operateMap.get(menu.getId());
		if (!CollectionUtils.isEmpty(list)) {
			for (Operate o : list) {
				Menu op = new Menu();
				op.setId(0L - o.getId());
				op.setCode("O_" + o.getCode());
				op.setName(o.getName());
				menu.addChild(op);
			}
		}
		if (CollectionUtils.isEmpty(menu.getChildren()))
			return;
		for (Menu m : menu.getChildren())
			handleAssignMenu(operateMap, m);
	}
	
	@RequestMapping(value = "assign-menus", method = RequestMethod.GET)
	public String assign(@RequestParam(value = "roleId", required = false) Long roleId, ModelMap model) {
		if (null == roleId)
			return "/business/systemManage/authManage/role/assign";
		Role role = this.roleService.getById(roleId);
		if (null == role)
			throw new EntityNotFoundException("Role[" + roleId + "] not find.");
		model.put("role", role);

		Pagination<Operate> operates = new Pagination<Operate>(1, Integer.MAX_VALUE);
		this.operateService.find(operates, null, false);
		Map<Long, List<Operate>> operateMap = new HashMap<Long, List<Operate>>();
		if (!operates.isEmpty()) {
			for (Operate o : operates) {
				List<Operate> list = operateMap.get(o.getMenuId());
				if (null == list) {
					list = new ArrayList<Operate>();
					operateMap.put(o.getMenuId(), list);
				}
				list.add(o);
			}
		}

		Menu menu = this.menuService.getMenuTree(Menu.CODE_ROOT);
		model.put("assignedMenuIds", "[]");
		List<Menu> assignedMenus = this.menuService.findByRole(role);
		Set<String> assignedMenuIds = new HashSet<String>();
		if (!CollectionUtils.isEmpty(assignedMenus)) {
			for (Menu m : assignedMenus) {
				assignedMenuIds.add("M_" + m.getCode());
			}
		}
		handleAssignMenu(operateMap, menu);
		List<Operate> assignedOperates = this.operateService.findByRole(role);
		if (!CollectionUtils.isEmpty(assignedOperates)) {
			for (Operate o : assignedOperates) {
				assignedMenuIds.add("O_" + o.getCode());
			}
		}
		model.put("assignedMenuIds", JSON.toJSONString(assignedMenuIds));
		model.put("menuJson", JSON.toJSONString(menu));
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"角色管理权限配置页面", request.getRemoteAddr());
		return "/business/systemManage/authManage/role/assign";
	}
	
	
	
	/**
     * 选择线路信息
     * @param id
     * @param model
     * @return
     */
     @RequestMapping(value = "select", method = RequestMethod.GET)
     public String select(
             @RequestParam(required = false)
             String dlgId, String el, Boolean multiple, ModelMap model) {
             model.put("dlgId", dlgId);
             model.put("el", el);
             model.put("multiple", multiple);
            List<Role> list=this.roleService.find(new Role());
     		model.put("roleJson", JSON.toJSONString(list));
     		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
    				"角色管理选取页面", request.getRemoteAddr());
             return "/business/systemManage/authManage/role/select";
         }
	
}
