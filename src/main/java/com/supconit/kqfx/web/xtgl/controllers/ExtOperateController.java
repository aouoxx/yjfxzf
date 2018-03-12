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

import com.supconit.honeycomb.business.authorization.entities.Menu;
import com.supconit.honeycomb.business.authorization.entities.Operate;
import com.supconit.honeycomb.business.authorization.services.MenuService;
import com.supconit.honeycomb.business.authorization.services.OperateService;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 菜单管理Controller
 * 
 * @author huxiaolong
 *
 */
@Controller
@RequestMapping("/xtgl/qxgl/operate")
public class ExtOperateController {
	
	private transient static final Logger	logger	= LoggerFactory.getLogger(ExtOperateController.class);
	private static final String MODULE_CODE = "XTGL_QXGL_MENU";
	@Autowired
	private MenuService menuService;
	@Autowired
	private OperateService operateService;
	@Resource
	private HttpServletRequest request;
	@Autowired
	private SystemLogService systemLogService;
	
	@ModelAttribute("newMenu")
	public Menu generateMenu() {
		return new Menu();
	}
	
	/**
    * 新增操作信息
    *
    * @param model
    * @return
    */
	@RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(@ModelAttribute("newMenu") Menu menu, ModelMap model) {
		if (null != menu.getPid() && menu.getPid() >= 0) {
			menu = this.menuService.getById(menu.getPid());
		}
		model.put("menu", menu);
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"菜单操作添加页面", request.getRemoteAddr());
        return "/business/systemManage/authManage/operate/add";
    }
	
	/**
    * 修改操作信息
    * @param id
    * @param model
    * @return
    */
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(
        @org.springframework.web.bind.annotation.RequestParam(required = true)
    Long id, ModelMap model) {
        Operate operate = operateService.getById(id);
        if (null != operate.getMenuId() && !"".equals(operate.getMenuId())) {
			Menu menu = this.menuService.getById(operate.getMenuId());
			model.put("menu", menu);
		}
        model.put("operate", operate);
        this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"菜单操作修改页面", request.getRemoteAddr());
        return "/business/systemManage/authManage/operate/edit";
    }
    
    /**
     * 展示操作信息
     *
     * @param model
     * @return
     */
 	@RequestMapping(value = "view", method = RequestMethod.GET)
 	public String view(
 	        @org.springframework.web.bind.annotation.RequestParam(required = true)
 	    Long id, ModelMap model) {
 		Operate operate = this.operateService.getById(id);
 		if (null != operate.getMenuId() && !"".equals(operate.getMenuId())) {
			Menu menu = this.menuService.getById(operate.getMenuId());
			operate.setMenu(menu);
		}
 		model.put("operate", operate);
 		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"菜单操作查看页面", request.getRemoteAddr());
 		return "/business/systemManage/authManage/operate/view";
     }
}
