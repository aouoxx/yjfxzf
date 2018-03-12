package com.supconit.kqfx.web.xtgl.controllers;

import hc.base.constants.OrderSort;
import hc.base.domains.AjaxMessage;
import hc.base.domains.OrderPart;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.modelextend.ExtendedModelProvider;
import hc.mvc.annotations.FormBean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.supconit.honeycomb.mvc.exceptions.EntityNotFoundException;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.entities.ExtDepartment;
import com.supconit.kqfx.web.xtgl.entities.Jgxx;
import com.supconit.kqfx.web.xtgl.services.ExtDepartmentService;
import com.supconit.kqfx.web.xtgl.services.JgxxService;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

@Controller
@RequestMapping("/xtgl/zhgl/department")
public class ExtDepartmentController {

	private transient static final Logger	logger	= LoggerFactory.getLogger(ExtDepartmentController.class);
	private static final String MODULE_CODE = "XTGL_ZHGL_DEPT";
	
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
	
	@ModelAttribute("newDepartment")
	public <X extends ExtDepartment> X generateDepartment() {
		X department = extendedModelProvider.createBean(ExtDepartmentService.EXTEND_MODEL_DEPARTMENT_CODE, ExtDepartment.class);
		return department;
	}

	@ModelAttribute("newPagination")
	public <X extends ExtDepartment> Pagination<X> generatePager() {
		Pagination<X> pager = new Pagination<X>();
		return pager;
	}
	
	/**
	 * 列表页面。
	 * 
	 * @param pager
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public <X extends Jgxx> String list(ModelMap model) {
		try {
			Jgxx jgxxTree = this.jgxxService.getTree();
			model.put("jgxxJson", JSON.toJSONString(jgxxTree));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/dept/list";
	}
	
	/**
	 * 获取列表数据
	 * 
	 * @param pager
	 * @param condition
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public <X extends ExtDepartment> Pageable<X> dolist(@ModelAttribute("newPagination") Pagination<X> pager, @FormBean(value = "condition", modelCode = "newDepartment") X condition,
			ModelMap model) {
		try {
			if (null == condition.getOrderParts()) {
				condition.setOrderParts(new OrderPart[] { new OrderPart("LFT", OrderSort.ASC), new OrderPart("CODE", OrderSort.ASC) });
			}
			this.extDepartmentService.find(pager, condition);
//			this.extDepartmentService.find(pager, condition, true);
			if (!pager.isEmpty()) {
				Set<Long> set = new HashSet<Long>();
				for (X department : pager) {
					set.add(department.getJgbh());
				}

				List<Jgxx> jgxxs = this.jgxxService.findByIds(set);
				if (!CollectionUtils.isEmpty(jgxxs)) {
					Map<Long, Jgxx> jgxxMap = new HashMap<Long, Jgxx>();
					for (Jgxx j : jgxxs) {
						jgxxMap.put(j.getId(), j);
					}
					for (X d : pager) {
						if (null != d.getJgbh()) {
							d.setJgxx(jgxxMap.get(d.getJgbh()));
							if(jgxxMap.get(d.getJgbh()) !=null){
								d.setJgxxJgmc(d.getJgxx().getJgmc());
								d.setParentJgmc(jgxxMap.get(d.getJgbh()).getJgmc());
							}
							
						}
						
						
					}
				}
			}
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"部门列表查询", request.getRemoteAddr());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}
	
	/**
	 * 进入修改页面
	 * 
	 * @param department
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "add", "edit" }, method = RequestMethod.GET)
	public <X extends ExtDepartment> String edit(@ModelAttribute("newDepartment") X department, ModelMap model) {
		try {
			if (null != department.getId()) {
				department = this.extDepartmentService.getById(department.getId());
				if (null == department) throw new EntityNotFoundException();
			} else {
				model.put("_type", "add");
			}
			//所属机构	
			if (null != department && null != department.getJgbh()) {
				
				Jgxx parentJgxx=this.jgxxService.getById(department.getJgbh());
				
				department.setJgxx(parentJgxx);
				department.setParentJgmc(parentJgxx.getJgmc());
				department.setParentJgId(parentJgxx.getId()+"");
				
			}
			model.put("department", department);
			Jgxx jgxxTree = this.jgxxService.getTree();
			model.put("jgxxJson", JSON.toJSONString(jgxxTree));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/dept/edit";
	}
	
	/**
	 * 
	 * @param department
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "view" }, method = RequestMethod.GET)
	public <X extends ExtDepartment> String view(@ModelAttribute("newDepartment") X department, ModelMap model) {
		try {
			if (null != department.getId()) {
				department = this.extDepartmentService.getById(department.getId());
				if (null == department) throw new EntityNotFoundException();
			}

			if (null != department && null != department.getJgbh()){
				department.setJgxx(this.jgxxService.getById(department.getJgbh()));
			}
			model.put("department", department);
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"查看部门信息，部门ID=" + department.getId(),
					request.getRemoteAddr());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "/business/systemManage/accountManage/dept/view";
	}
	
	/**
	 * 保存部门信息
	 * 
	 * @param department
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "add", "edit" }, method = RequestMethod.POST)
	public <X extends ExtDepartment> AjaxMessage save(@FormBean(value = "department", modelCode = "newDepartment") X department) {
		try {
			if (null == department) 
				return AjaxMessage.error("参数错误！");
//			if (StringUtil.isBlank(department.getCode())) { 
//				return AjaxMessage.error("编码必须填写"); }

			
			if(StringUtil.isNotBlank(department.getCode())){
				department.setCode(department.getCode().trim().toUpperCase());
				ExtDepartment sample = extDepartmentService.getByCode(department.getCode());
				if (null != sample && (null == department.getId() || !department.getId().equals(sample.getId()))) 
					return AjaxMessage.error(department.getCode() + "编码已经存在！");
			}
			//所属机构
			if (null != department.getJgxx() && null != department.getJgxx().getId()) {
				department.setJgbh(department.getJgxx().getId());
			} else {
				return AjaxMessage.error("所属机构未指定！");
			}
			extDepartmentService.save(department);
			if(department.getId() == null){
				this.systemLogService.log(MODULE_CODE,
						OperateType.insert.getCode(),
						"新增部门，部门名称=" + department.getName(), request.getRemoteAddr());
			}else{
				this.systemLogService.log(MODULE_CODE,
						OperateType.update.getCode(),
						"编辑部门，部门ID=" + department.getId(), request.getRemoteAddr());
			}
			return AjaxMessage.success();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
		
	}
	
	/**
	 * 删除部门
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public AjaxMessage delete(@RequestParam(required = true) Long id) {
		try {
			ExtDepartment old = this.extDepartmentService.getById(id);
			if (null == old) return AjaxMessage.error("错误的参数");
			extDepartmentService.delete(new ExtDepartment(id));
			this.systemLogService.log(MODULE_CODE,
					OperateType.delete.getCode(), "删除部门，部门ID=" + id,
					request.getRemoteAddr());
			return AjaxMessage.success();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	/**
	 * 根据机构编号获取部门列表
	 * 
	 * @param jgbh
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getDeptsByJgbh" ,method = RequestMethod.POST)
	public AjaxMessage getDeptsByJgbh(@RequestParam(required = true) Long jgbh) {
		try{
			return AjaxMessage.success(extDepartmentService.findByJgbh(jgbh));
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
}
