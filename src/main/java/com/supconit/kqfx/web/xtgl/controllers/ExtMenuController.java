package com.supconit.kqfx.web.xtgl.controllers;

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

import com.alibaba.fastjson.JSON;
import com.supconit.honeycomb.business.authorization.entities.Menu;
import com.supconit.honeycomb.business.authorization.services.MenuService;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 菜单管理Controller
 * 
 * @author huxiaolong
 *
 */
@Controller
@RequestMapping("/xtgl/qxgl/menu")
public class ExtMenuController {
	
	private transient static final Logger	logger	= LoggerFactory.getLogger(ExtMenuController.class);
	private static final String MODULE_CODE = "XTGL_QXGL_MENU";
	@Autowired
	private MenuService menuService;
	@Resource
	private HttpServletRequest request;
	@Autowired
	private SystemLogService systemLogService;
	
	@ModelAttribute("newMenu")
	public Menu generateMenu() {
		return new Menu();
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		try {
			Menu root = this.menuService.getMenuTree(null);
			model.put("menuJson", JSON.toJSONString(root));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"系统菜单页面", request.getRemoteAddr());
		return "/business/systemManage/authManage/menu/list";
	}
	
	/**
    * 新增菜单信息
    *
    * @param model
    * @return
    */
	@RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(@ModelAttribute("newMenu") Menu menu, ModelMap model) {
		if (null != menu.getPid() && menu.getPid() >= 0) {
			Menu parent = this.menuService.getById(menu.getPid());
			menu.setParent(parent);
		}
		model.put("menu", menu);
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"系统菜单添加页面", request.getRemoteAddr());
        return "/business/systemManage/authManage/menu/add";
    }
	
	/**
    * 修改菜单信息
    * @param id
    * @param model
    * @return
    */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(
        @org.springframework.web.bind.annotation.RequestParam(required = true)
    Long id, ModelMap model) {
        Menu menu = this.menuService.getById(id);
        if (null != menu.getPid() && menu.getPid() >= 0) {
			Menu parent = this.menuService.getById(menu.getPid());
			menu.setParent(parent);
		}
        model.put("menu", menu);
        this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"系统菜单修改页面", request.getRemoteAddr());
        return "/business/systemManage/authManage/menu/edit";
    }
    
    /**
     * 展示菜单信息
     *
     * @param model
     * @return
     */
 	@RequestMapping(value = "view", method = RequestMethod.GET)
 	public String view(
 	        @org.springframework.web.bind.annotation.RequestParam(required = true)
 	    Long id, ModelMap model) {
 		Menu menu = this.menuService.getById(id);
 		if(null != menu.getPid() && !"".equals(menu.getPid())){
 			Menu parent = this.menuService.getById(menu.getPid());
 			menu.setParent(parent);
 		}
 		model.put("menu", menu);
 		return "/business/systemManage/authManage/menu/view";
     }
 	
    /**
     * 选择菜单信息
     * @param id
     * @param model
     * @return
     */
     @RequestMapping(value = "select", method = RequestMethod.GET)
     public String select(
             @org.springframework.web.bind.annotation.RequestParam(required = true)
             String dlgId, String el, Boolean multiple, ModelMap model) {
    	 model.put("dlgId", dlgId);
         model.put("el", el);
         model.put("multiple", multiple);
         Menu root = this.menuService.getMenuTree(null);
 		 model.put("menuJson", JSON.toJSONString(root));
 		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"系统菜单选取页面", request.getRemoteAddr());
         return "/business/systemManage/authManage/menu/select";
     }
}
