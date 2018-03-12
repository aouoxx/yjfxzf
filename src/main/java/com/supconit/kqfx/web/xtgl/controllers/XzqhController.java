/**
 * 
 */
package com.supconit.kqfx.web.xtgl.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pagination;
import hc.mvc.annotations.FormBean;

import java.util.List;

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

import com.supconit.kqfx.web.xtgl.entities.Xzqh;
import com.supconit.kqfx.web.xtgl.services.XzqhService;



/** ========================== 自定义区域结束 ========================== **/
/**
 * 行政区划控制层。
 * 
 * @author 
 * @create 2014-03-17 16:03:40 
 * @since 
 * 
 */
@Controller("xtgl_xzqh_controller")
@RequestMapping("/xtgl/xzqh")
public class XzqhController {

	/**
	 * 日志服务。
	 */
	private transient static final Logger	logger	= LoggerFactory.getLogger(XzqhController.class);

	/**
	 * 注入服务。
	 */
	@Autowired
	private XzqhService						xzqhService;

	/**
	 * 准备实体对象。
	 * 
	 * @return
	 */	
	@ModelAttribute("prepareXzqh")
	public Xzqh prepareXzqh() {
		return new Xzqh();
	}
	
	/**
	 * 列表展现。
	 * 
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		return "xtgl/xzqh/list";
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
	public Pagination<Xzqh> list(Pagination<Xzqh> pager, @FormBean(value = "condition", modelCode = "prepareXzqh") Xzqh condition) {
		/* Validate Pager Parameters */
		if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE) return pager;
				
		this.xzqhService.find(pager, condition); //获取分页查询结果
		
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
		return "xtgl/xzqh/add";
	}
	
	/**
	 * 编辑展示。
	 * 
	 * @param id
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(required = true) String xzqhm,  ModelMap model) {
		model.put("xzqh", this.xzqhService.getById(xzqhm));
		return "xtgl/xzqh/edit";
	}

	/**
	 * 保存编辑内容。
	 * 
	 * @param equipment
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"add", "edit"}, method = RequestMethod.POST)
	public AjaxMessage doEdit(@FormBean(value = "xzqh", modelCode = "prepareXzqh") Xzqh xzqh) {
		// TODO Validate
		try {
			this.xzqhService.save(xzqh);
			return AjaxMessage.success(xzqh.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	/**
	 * 删除一条记录。
	 * 
	 * @param xzqh
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public AjaxMessage doDelete(Xzqh xzqh) {
		try {
			if (null == xzqh || null == xzqh.getXzqhdm()) return AjaxMessage.error("错误的参数。");
			this.xzqhService.delete(xzqh);
			return AjaxMessage.success(xzqh.getXzqhdm());
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
	public String view(@RequestParam(required = true) String xzqhdm,  ModelMap model) {
		model.put("xzqh", this.xzqhService.getById(xzqhdm));
		return "xtgl/xzqh/view";
	}
	
	/**
	 *  获取市列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findCity", method = RequestMethod.POST)
	public AjaxMessage findCity(){
		try{
			Xzqh xzqh = new Xzqh();
			xzqh.setXzqhdm("%00000");
			return AjaxMessage.success(this.xzqhService.findList(xzqh));
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	/**
	 * 获取区、县列表
	 * 
	 * @param xzqhdm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findCounty", method = RequestMethod.POST)
	public AjaxMessage findCounty(@RequestParam(required = true) String xzqhdm){
		try{
			Xzqh xzqh = new Xzqh();
			xzqh.setXzqhdm(xzqhdm.substring(0,4)+"%000");
			List<Xzqh> xzqhs = this.xzqhService.findList(xzqh);
			xzqhs.remove(0);
			return AjaxMessage.success(xzqhs);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	/**
	 * 获取乡、镇列表
	 * 
	 * @param xzqhdm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findVillage", method = RequestMethod.POST)
	public AjaxMessage findVillage(@RequestParam(required = true) String xzqhdm){
		try{
			Xzqh xzqh = new Xzqh();
			xzqh.setXzqhdm(xzqhdm.substring(0,6)+"%");
			List<Xzqh> xzqhs = this.xzqhService.findList(xzqh);
			xzqhs.remove(0);
			return AjaxMessage.success(xzqhs);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	/**
	 * 根据行政区划代码查询行政区划信息
	 * 
	 * @param xzqhdm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findRegion", method = RequestMethod.POST)
	public AjaxMessage findRegion(@RequestParam(required = true) String xzqhdm) {
		try {
			Xzqh xzqh = new Xzqh();
			xzqh.setXzqhdm(xzqhdm);
			xzqh = this.xzqhService.getByXzqhdm(xzqhdm);
			return AjaxMessage.success(xzqh);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
		
	/** ========================== 自定义区域开始 ========================== **/
	/************************* 自定义区域内容不会被覆盖 *************************/
	
	
	
	/** ========================== 自定义区域结束 ========================== **/
}