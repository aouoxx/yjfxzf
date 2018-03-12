package com.supconit.kqfx.web.xtgl.controllers;

import hc.base.constants.OrderSort;
import hc.base.domains.AjaxMessage;
import hc.base.domains.OrderPart;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.modelextend.ExtendedModelProvider;
import hc.mvc.annotations.FormBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.runtime.directive.Evaluate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.supconit.honeycomb.mvc.exceptions.EntityNotFoundException;
import com.supconit.kqfx.web.util.GisUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.entities.Jgxx;
import com.supconit.kqfx.web.xtgl.services.JgxxService;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;
import com.supconit.kqfx.web.xtgl.services.XzqhService;

/**
 * 机构管理Controller
 * 
 * @author shenxinfeng
 * 
 */
@Controller
@RequestMapping("/xtgl/zhgl/jgxx")
public class JgxxController extends AbstractJgxxTreeController {

	private transient static final Logger logger = LoggerFactory
			.getLogger(Jgxx.class);
	private static final String MODULE_CODE = "XTGL_ZHGL_JGXX";
	@Autowired
	private JgxxService jgxxService;
	@Autowired
	private ExtendedModelProvider extendedModelProvider;
	@Autowired
	private SystemLogService systemLogService;
	@Resource
	private HttpServletRequest request;
	@Autowired
	private XzqhService	xzqhService;

	@ModelAttribute("newJgxx")
	public <X extends Jgxx> X generateJgxx() {
		X jgxx = extendedModelProvider.createBean(
				JgxxService.EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class);
		return jgxx;
	}

	@ModelAttribute("newPagination")
	public <X extends Jgxx> Pagination<X> generatePager() {
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

		Jgxx jgxxTree = this.jgxxService.getTree();
		model.put("jgxxJson", JSON.toJSONString(jgxxTree));

		return "/business/systemManage/accountManage/jgxx/list";
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
	@RequestMapping(value = "querylist", method = RequestMethod.POST)
	public <X extends Jgxx> Pageable<X> querylist(
			@ModelAttribute("newPagination") Pagination<X> pager,
			@FormBean(value = "condition", modelCode = "newJgxx") X condition,
			ModelMap model) {
		if (null == condition.getOrderParts()) {
			condition.setOrderParts(new OrderPart[] { new OrderPart("LFT",
					OrderSort.ASC) });
		}
		condition.setDeleted(0);
		this.jgxxService.find(pager, condition, true);
		if (!pager.isEmpty()) {
			Set<Long> set = new HashSet<Long>();
			for (X jgxx : pager) {
				set.add(jgxx.getPid());
				if(!StringUtil.isEmpty(jgxx.getJgmc()))
				{
					jgxx.setJgmc(StringEscapeUtils.unescapeHtml(jgxx.getJgmc()));
				}
				if(!StringUtil.isEmpty(jgxx.getJgjc()))
				{
					jgxx.setJgjc(StringEscapeUtils.unescapeHtml(jgxx.getJgjc()));
				}
				if(!StringUtil.isEmpty(jgxx.getFzr()))
				{
					jgxx.setFzr(StringEscapeUtils.unescapeHtml(jgxx.getFzr()));
				}
				if(!StringUtil.isEmpty(jgxx.getXxdz()))
				{
					jgxx.setXxdz(StringEscapeUtils.unescapeHtml(jgxx.getXxdz()));
				}
				if(!StringUtil.isEmpty(jgxx.getBz()))
				{
					jgxx.setBz(StringEscapeUtils.unescapeHtml(jgxx.getBz()));
				}
			}

			List<X> parents = this.jgxxService.findByIds(set);
			if (!CollectionUtils.isEmpty(parents)) {
				Map<Long, X> parentMap = new HashMap<Long, X>();
				for (X d : parents) {
					parentMap.put(d.getId(), d);
				}
				for (X d : pager) {
					if (null != d.getPid()) {
						d.setParent(parentMap.get(d.getPid()));
						
						if(parentMap.get(d.getPid()) !=null){
							d.setParentJgmc(parentMap.get(d.getPid()).getJgmc());
						}
						
					}

				}
			}
		}
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"机构信息列表查询", request.getRemoteAddr());

		return pager;
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
	@RequestMapping(value = "queryTreelist", method = RequestMethod.POST)
	public <X extends Jgxx> List<X> queryTreelist(
			@FormBean(value = "condition", modelCode = "newJgxx") X condition,
			ModelMap model) {
		
		condition.setDeleted(0);
		List<X> list=(List<X>) this.jgxxService.selectAllJgxx(condition);

        return list;
	}

	/**
	 * 进入修改页面
	 * 
	 * @param jgxx
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "add", "edit" }, method = RequestMethod.GET)
	public <X extends Jgxx> String edit(@ModelAttribute("newJgxx") X jgxx,
			ModelMap model) {
		if (null != jgxx.getId()) {
			jgxx = this.jgxxService.getById(jgxx.getId());
			if (null == jgxx)
				throw new EntityNotFoundException();
		} else {
			model.put("_type", "add");
		}
		if (null != jgxx && null != jgxx.getPid()) {
			Jgxx parentJgxx=this.jgxxService.getById(jgxx.getPid());
			
			jgxx.setParent(parentJgxx);
			jgxx.setParentJgmc(parentJgxx.getJgmc());
			jgxx.setParentJgId(parentJgxx.getId()+"");
			
		}
		model.put("jgxx", jgxx);
		Jgxx jgxxTree = this.jgxxService.getTree();
		model.put("jgxxJson", JSON.toJSONString(jgxxTree));
		return "/business/systemManage/accountManage/jgxx/edit";
	}

	/**
	 * 进入查看详细页面
	 * 
	 * @param jgxx
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "view" }, method = RequestMethod.GET)
	public <X extends Jgxx> String view(@ModelAttribute("newJgxx") X jgxx,
			ModelMap model) {
		try {
			if (null != jgxx.getId()) {
				jgxx = this.jgxxService.getById(jgxx.getId());
				if (null == jgxx)
					throw new EntityNotFoundException();
			}
			if (null != jgxx && null != jgxx.getPid()) {
				jgxx.setParent(this.jgxxService.getById(jgxx.getPid()));
				jgxx.setXzqhmc(this.xzqhService.getByXzqhdm(jgxx.getXzqhdm()).getDqqc());
			}
			model.put("jgxx", jgxx);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"查看机构信息，机构ID=" + (jgxx != null ? jgxx.getId() : ""),
				request.getRemoteAddr());
		return "/business/systemManage/accountManage/jgxx/view";
	}

	/**
	 * 保存机构信息
	 * 
	 * @param jgxx
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "add", "edit" }, method = RequestMethod.POST)
	public <X extends Jgxx> AjaxMessage save(
			@FormBean(value = "jgxx", modelCode = "newJgxx") X jgxx) {
		try {
			if (null == jgxx)
				return AjaxMessage.error("参数错误！");
			if (null != jgxx.getId()) {
				Jgxx old = this.jgxxService.getById(jgxx.getId());
				if (null == old)
					return AjaxMessage.error("错误的参数");
				if (Jgxx.ROOT_ID.equals(old.getId()))
					return AjaxMessage.error("机构树根节点禁止修改！");
			}
			if (null != jgxx.getParent() && null != jgxx.getParent().getId()) {
				jgxx.setPid(jgxx.getParent().getId());
				if(null!=jgxx.getId()){
					Jgxx child = this.jgxxService.getById(jgxx.getId());
					Jgxx parent = this.jgxxService.getById(jgxx.getParent().getId());
					if(null!=child.getLft()&&null!=child.getRgt()&&null!=parent){
						if(parent.getLft().longValue()>=child.getLft().longValue()&&parent.getRgt().longValue()<=child.getRgt().longValue())
						{
							return AjaxMessage.error("上级机构不能为自己或子节点！");
						}
					}
				}
			} else {
				return AjaxMessage.error("上级机构未指定！");
			}
			Jgxx root = this.jgxxService.getById(Jgxx.ROOT_ID);
			if (root.getJgmc().equalsIgnoreCase(jgxx.getJgmc())) {
				return AjaxMessage.error("机构名称已存在请修改");
			}
			List<Jgxx> jgxxList = this.jgxxService.findAllWithoutVitualRoot();
			for (Jgxx jg : jgxxList) {
				if (jg.getJgmc().equalsIgnoreCase(jgxx.getJgmc())) {
					if (null == jgxx.getId()
							|| !jg.getId().equals(jgxx.getId())) {
						return AjaxMessage.error("机构名称已存在请修改");
					}
				}
			}
			jgxxService.save(jgxx);
			if (jgxx.getId() == null) {
				this.systemLogService
						.log(MODULE_CODE,
								OperateType.insert.getCode(),
								"新增机构，机构ID=" + jgxx.getId() + "，机构名称="
										+ jgxx.getJgmc(),
								request.getRemoteAddr());
			} else {
				this.systemLogService
						.log(MODULE_CODE,
								OperateType.update.getCode(),
								"编辑机构，机构ID=" + jgxx.getId() + "，机构名称="
										+ jgxx.getJgmc(),
								request.getRemoteAddr());
			}
			return AjaxMessage.success();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}

	/**
	 * 删除机构
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public AjaxMessage delete(@RequestParam(required = true) Long id) {
		try {
			Jgxx old = this.jgxxService.getById(id);
			if (null == old)
				return AjaxMessage.error("错误的参数");
			if (Jgxx.ROOT_ID.equals(old.getId()))
				return AjaxMessage.error("机构树根节点禁止删除！");
			// 先检查有没有下级节点，如果有的话不允许删除
			if (jgxxService.countByPid(id) > 0) {
				return AjaxMessage.error("有下级机构，不允许删除！");
			}
			jgxxService.deleteById(id);
			this.systemLogService.log(MODULE_CODE,
					OperateType.delete.getCode(), "删除机构，机构ID=" + id,
					request.getRemoteAddr());
			return AjaxMessage.success();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
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
             String dlgId, String el, Boolean multiple,String jgdj,ModelMap model) {
            
             Jgxx jgxxTree = new Jgxx();
             if(!StringUtils.isEmpty(jgdj))
             {
            	 String[] jgdjs = jgdj.split(",");
            	 List<String> jgdjList = new ArrayList<String>();
            	 for (int i = 0; i < jgdjs.length; i++) 
            	 {
            		 jgdjList.add(jgdjs[i]);
				 }
                 jgxxTree = this.jgxxService.getTreeByJgdj(jgdjList);
             }else{
            	 jgxxTree = this.jgxxService.getTree();
             }
             
             model.put("dlgId", dlgId);
             model.put("el", el);
             model.put("multiple", multiple);
     		 model.put("jgxxJson", JSON.toJSONString(jgxxTree));

             return "/business/systemManage/accountManage/jgxx/select";
         }
	/**
	 * 根据机构分类获取机构列表
	 * 
	 * @param jgdjs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getOrgListByJgdj", method = RequestMethod.POST)
	public AjaxMessage getOrgListByJgdj(
			@RequestParam(required = false) String jgdjs) {
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"根据机构等级获取机构列表，机构等级=" + jgdjs, request.getRemoteAddr());
			if (StringUtil.isNotBlank(jgdjs)) {
				String[] split = jgdjs.split(",");
				List<String> jgdjList = new ArrayList<String>();
				for (String dj : split) {
					jgdjList.add(dj);
				}
				List<Jgxx> orgList = this.jgxxService.findByJgdjs(jgdjList);
				for (Jgxx jgxx : orgList) {
					if (jgxx.getId().equals(Jgxx.ROOT_ID)) {
						orgList.remove(jgxx);
						break;
					}
				}
				return AjaxMessage.success(orgList);
			} else {
				return AjaxMessage.success();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}

	/**
	 * 查看机构信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "viewForMap", method = RequestMethod.GET)
	public String viewForMap(@RequestParam(required = true) Long id,
			ModelMap model) {
		try {
			Jgxx jgxx = null;
			if (null != id) {
				jgxx = this.jgxxService.getById(id);
				if (null == jgxx)
					throw new EntityNotFoundException();
			}
			if (null != jgxx) {
				if (null != jgxx.getPid()) {
					jgxx.setParent(this.jgxxService.getById(jgxx.getPid()));
				}
				model.put("xzqh", xzqhService.getByXzqhdm(jgxx.getXzqhdm()));
				
				//如果是公路站图层需添加评定情况、巡查情况、物资、设备
//				if("5".equals(jgxx.getJgdj())){
//					//巡查情况查询
//					Pageable<CheckRecord> pager = new Pagination<CheckRecord>();
//					pager.setPageNo(1);
//					pager.setPageSize(Integer.MAX_VALUE);
//					CheckRecord condition = new CheckRecord();
//					condition.setStationName(jgxx.getJgmc().substring(0, 2));
//					pager = this.checkRecordService.findRoadCheckList(pager, condition); 
//					
//					List<Evaluate> evaluateList = this.evaluateService.getByWzcbzId(id);
//					List<Yjwzgl> wzglList = this.wzglService.getByWzcbzId(id);
//					List<Yjsbgl> sbglList = this.sbglService.getByWzcbzId(id);
//					model.put("evaluateCount", evaluateList.size());
//					model.put("checkRecordCount", pager.size());
//					model.put("wzglCount", wzglList.size());
//					model.put("sbglCount", sbglList.size());
//				}
			}
			model.put("jgxx", jgxx);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"查看机构信息，机构ID=" + id, request.getRemoteAddr());
		return "/business/systemManage/accountManage/jgxx/viewForMap";
	}

	/**
	 * 根据机构id获取机构信息
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findJgxx", method = RequestMethod.POST)
	public AjaxMessage findJgxx(@RequestParam(required = true) Long id) {
		try {
			Jgxx jgxx = this.jgxxService.getById(id);
			return AjaxMessage.success(jgxx);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	/**
	 * 刷新节点树。
	 * 
	 * @param equipment
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "refreshTree" }, method = RequestMethod.POST)
	public String refreshTree() {
		
		Jgxx jgxxTree = this.jgxxService.getTree();
		return JSON.toJSONString(jgxxTree);

	}
	
	
	
	
	/**
	 * 列表页面。
	 * 
	 * @param pager
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "lzzd", method = RequestMethod.GET)
	public <X extends Jgxx> String lzzd(ModelMap model) {


		return "/business/basedata/lzzd/list";
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
	@RequestMapping(value = "lzzdList", method = RequestMethod.POST)
	public <X extends Jgxx> Pageable<X> lzzdList(
			@ModelAttribute("newPagination") Pagination<X> pager,
			@FormBean(value = "condition", modelCode = "newJgxx") X condition,
			ModelMap model) {
		if (null == condition.getOrderParts()) {
			condition.setOrderParts(new OrderPart[] { new OrderPart("LFT",
					OrderSort.ASC) });
		}
		condition.setDeleted(0);
		if(condition.getJgmc() ==null || condition.getJgmc() ==""){
			condition.setJgmc("路政");
		}
		this.jgxxService.find(pager, condition, true);
		if (!pager.isEmpty()) {
			Set<Long> set = new HashSet<Long>();
			for (X jgxx : pager) {
				set.add(jgxx.getPid());
			}

			List<X> parents = this.jgxxService.findByIds(set);
			if (!CollectionUtils.isEmpty(parents)) {
				Map<Long, X> parentMap = new HashMap<Long, X>();
				for (X d : parents) {
					parentMap.put(d.getId(), d);
				}
				for (X d : pager) {
					if (null != d.getPid()) {
						d.setParent(parentMap.get(d.getPid()));
						
						if(parentMap.get(d.getPid()) !=null){
							d.setParentJgmc(parentMap.get(d.getPid()).getJgmc());
						}
						
					}

				}
			}
		}
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"机构信息列表查询", request.getRemoteAddr());

		return pager;
	}
	
//	/**
//	 * 应急指挥周边查询
//	 * @param jd 事件点经度
//	 * @param wd 事件点纬度
//	 * @return
//	 */
//	@ResponseBody
// 	@RequestMapping(value = "findForNear", method = RequestMethod.POST)
// 	public AjaxMessage findForNear(Double jd, Double wd){
// 		try {
// 			Jgxx condition = new Jgxx();
// 			condition.setJgdj("5");
// 			condition.setDeleted(0);
// 			Pagination<Jgxx> pager = new Pagination<Jgxx>();
// 			pager.setPageNo(1);
// 			pager.setPageSize(1000);
// 			this.jgxxService.find(pager, condition, true);
// 			List<Jgxx> nearList = new ArrayList<Jgxx>();
// 			Map<Double, Jgxx> map = new TreeMap<Double, Jgxx>();
// 			List<Jgxx> tempList = new ArrayList<Jgxx>();
// 			for (Jgxx jgxx : pager) {
// 				//经纬度为空的不显示
// 				if (jgxx.getJd() != null && jgxx.getWd() != null) {
// 					double space = GisUtil.lineSpace(jd, wd, jgxx.getJd().doubleValue(), jgxx.getWd().doubleValue());
// 					if (space > 0 && space <= zbcxSpace) {
//						if (space > 1000) {
//							jgxx.setBz(String.valueOf((new BigDecimal(space/1000)).setScale(2,BigDecimal.ROUND_DOWN))+"千米");
//							jgxx.setCz(String.valueOf(space));
//						}else {
//							jgxx.setBz(String.valueOf((new BigDecimal(space)).setScale(0,BigDecimal.ROUND_DOWN)) + "米");
//							jgxx.setCz(String.valueOf(space));
//						}
//						tempList.add(jgxx);
//						map.put(space, jgxx);
//					}
// 				}
//			}
// 			//将排序结果放入list
// 			for (Double key : map.keySet()) {
// 				for (Jgxx jgxx : tempList) {
//					if (key.equals(Double.valueOf(jgxx.getCz()))) {
//						nearList.add(jgxx);
//					}
//				}
//			}
// 			return AjaxMessage.success(nearList);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return AjaxMessage.error(e.getMessage());
//		}
// 	}
}
