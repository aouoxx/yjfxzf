package com.supconit.kqfx.web.xtgl.controllers;

import hc.base.constants.OrderSort;
import hc.base.domains.AjaxMessage;
import hc.base.domains.OrderPart;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.modelextend.ExtendedModelProvider;
import hc.mvc.annotations.FormBean;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

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

import com.alibaba.fastjson.JSON;
import com.supconit.honeycomb.mvc.exceptions.EntityNotFoundException;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.entities.ExtDepartment;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.entities.Jgxx;
import com.supconit.kqfx.web.xtgl.services.ExtDepartmentService;
import com.supconit.kqfx.web.xtgl.services.ExtPersonService;
import com.supconit.kqfx.web.xtgl.services.JgxxService;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 人员管理Controller
 * 
 * @author shenxinfeng
 *
 */
@Controller
@RequestMapping("/xtgl/zhgl/person")
public class ExtPersonController {
	
	private transient static final Logger	logger	= LoggerFactory.getLogger(ExtPersonController.class);
	private static final String MODULE_CODE = "XTGL_ZHGL_PERSON";
	
	@Autowired
	private ExtPersonService extPersonService;
	@Autowired
	private ExtDepartmentService extDepartmentService;
	@Autowired
	private JgxxService jgxxService;
	@Autowired
	private ExtendedModelProvider extendedModelProvider;
	@Resource
	private HttpServletRequest request;
	@Autowired
	private SystemLogService systemLogService;
	

	@ModelAttribute("newPerson")
	public <X extends ExtPerson> X generatePerson() {
		X person = extendedModelProvider.createBean(ExtPersonService.EXTEND_MODEL_PERSON_CODE, ExtPerson.class);
		return person;
	}

	@ModelAttribute("newPagination")
	public <X extends ExtPerson> Pagination<X> generatePager() {
		Pagination<X> pager = new Pagination<X>();
		return pager;
	}
	
	/**
	 * 进入列表页面
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
		return "/business/systemManage/accountManage/person/list";
	}
	
	/**
	 * 列表页面
	 * 
	 * @param pager
	 * @param condition
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"list", "select-list"}, method = RequestMethod.POST)
	public <X extends ExtPerson> Pageable<X> dolist(@ModelAttribute("newPagination") Pagination<X> pager, @FormBean(value = "condition", modelCode = "newPerson") X condition) {
		try {
			if (null == condition.getOrderParts() || condition.getOrderParts().length == 0) {
				condition.setOrderParts(new OrderPart[] { new OrderPart("ID", OrderSort.DESC) });
			}
			this.extPersonService.find(pager, condition);
			if (!pager.isEmpty()) {
				pager = this.extPersonService.personSetJgxx(pager);
				for(ExtPerson temp:pager){
					temp.setJgxxJgmc(temp.getJgxx().getJgmc());
					if(null != temp.getDepartmentId()){
						
						
						String departmentName="";
						if(this.extDepartmentService.getById(temp.getDepartmentId()) !=null){
							departmentName=this.extDepartmentService.getById(temp.getDepartmentId()).getName();
						}
						
						temp.setDepartmentName(departmentName);
						
					}
					
					
				}
				
			}
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"人员列表查询", request.getRemoteAddr());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}
	
	/**
	 * 进入编辑页面
	 * 
	 * @param person
	 * @param id
	 * @param jgbh	所属机构
	 * @param pdlg_id	打开此页面的父页面id
	 * @param dlg_id	此页面id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "edit", "add" }, method = RequestMethod.GET)
	public <X extends ExtPerson> String edit(@FormBean(value = "person", modelCode = "newPerson") X person,String id, String jgbh,String pdlg_id, String dlg_id, ModelMap model) {
		try {
			if (null != id) {
				person = this.extPersonService.getById(Long.parseLong(id));
				if (null == person) { 
					throw new EntityNotFoundException(); 
				}
			} else {
				model.put("_type", "add");
			}
			model.put("pdlg_id", pdlg_id);
			model.put("dlg_id", dlg_id);
			//根据带来的机构ID显示所属机构
			if(StringUtil.isNotEmpty(jgbh)){
				person.setJgxx(this.jgxxService.getById(Long.parseLong(jgbh)));
				List<ExtDepartment> departments = this.extDepartmentService.findByJgbh(Long.parseLong(jgbh));
				model.put("departments", departments);
			}
			model.put("person", person);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/person/edit";
	}

	/**
	 * 保存用户信息
	 * 
	 * @param person
	 * @return
	 */
	@RequestMapping(value = { "add", "edit" }, method = RequestMethod.POST)
	@ResponseBody
	public <X extends ExtPerson> AjaxMessage save(@FormBean(value = "person", modelCode = "newPerson") X person) {
		try {
			if (null != person.getId()) {
				X oldPerson = this.extPersonService.getById(person.getId());
				if (null == oldPerson) 
					return AjaxMessage.error("错误的参数.");
//				person.setCode(oldPerson.getCode());
			}
			if(StringUtil.isNotBlank(person.getCode())){
				person.setCode(person.getCode().trim().toUpperCase());
				X sample = extPersonService.getByCode(person.getCode());
				if (null != sample && (null == person.getId() || !person.getId().equals(sample.getId()))) 
					return AjaxMessage.error(person.getCode() + "编码已经存在！");
			}
			
			//所属机构
			if (null != person.getJgxx() && null != person.getJgxx().getId()) {
				person.setJgbh(person.getJgxx().getId());
			}
			this.extPersonService.save(person);
			if (person.getId() == null) {
				this.systemLogService.log(MODULE_CODE,
						OperateType.insert.getCode(),
						"新增人员，人员姓名=" + person.getName(),
						request.getRemoteAddr());
			} else {
				this.systemLogService.log(MODULE_CODE,
						OperateType.insert.getCode(),
						"编辑人员，人员ID=" + person.getId(), request.getRemoteAddr());
			}
			return AjaxMessage.success("保存成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}

	/**
	 * 删除人员
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage delete(Long id) {
		try {
			extPersonService.delete(new ExtPerson(id));
			this.systemLogService.log(MODULE_CODE,
					OperateType.delete.getCode(), "删除人员，人员ID=" + id,
					request.getRemoteAddr());
			return AjaxMessage.success();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	/**
	 * 查看用户详细信息
	 * 
	 * @param person
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "view" }, method = RequestMethod.GET)
	public <X extends ExtPerson> String view(@ModelAttribute("newPerson") X person, ModelMap model) {
		try {
			if (null != person.getId()) {
				person = this.extPersonService.getById(person.getId());
				if (null == person) throw new EntityNotFoundException();
			}

			if (null != person ){
				if(null != person.getJgbh()){
					person.setJgxx(this.jgxxService.getById(person.getJgbh()));
				}
				if(null != person.getDepartmentId()){
					person.setDepartment(this.extDepartmentService.getById(person.getDepartmentId()));
				}
			}
			model.put("person", person);
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"查看人员详细信息，人员ID=" + person.getId(), request.getRemoteAddr());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/person/view";
	}
	
	
	/**
	 * 选择人员信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "select", method = RequestMethod.GET)
	public String select(
			@RequestParam(required = false) String dlgId,
			@RequestParam(required = false) String el,Boolean multiple, ModelMap model) {
		Jgxx jgxxTree = this.jgxxService.getTree();
		model.put("jgxxJson", JSON.toJSONString(jgxxTree));
		model.put("dlgId", dlgId);
		model.put("el", el);
		model.put("multiple", multiple);
		return "/business/systemManage/accountManage/person/select";
	}
	
	/**
	 * 通用选用户(多选)
	 * 
	 * @param muilt
	 * @param selected
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "selectSendMessageMultPerson", method = RequestMethod.GET)
	public <X extends ExtPerson> String selectSendMessageMultPerson(
			String input_id,String pdlg_id, String dlg_id,ModelMap model) {
		try {
			Jgxx jgxxTree = this.jgxxService.getTree();
			model.put("jgxxJson", JSON.toJSONString(jgxxTree));
			model.put("input_id", input_id);
			model.put("pdlg_id", pdlg_id);
			model.put("dlg_id", dlg_id);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/person/selectSendMessageMultPerson";
	}
	
	/**
	 * 通用选用户(多选) for 短信分组
	 * 
	 * @param muilt
	 * @param selected
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "selectPersonForGroup", method = RequestMethod.GET)
	public <X extends ExtPerson> String selectPersonForGroup(
			String input_id,String pdlg_id,String dlg_id,ModelMap model) {
		try {
			Jgxx jgxxTree = this.jgxxService.getTree();
			model.put("jgxxJson", JSON.toJSONString(jgxxTree));
			model.put("groupId", input_id);
			model.put("pdlg_id", pdlg_id);
			model.put("dlg_id", dlg_id);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/person/selectPersonForGroup";
	}
	
}
