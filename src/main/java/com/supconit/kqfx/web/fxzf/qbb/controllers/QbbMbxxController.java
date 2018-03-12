package com.supconit.kqfx.web.fxzf.qbb.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pagination;
import hc.business.dic.entities.Data;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.kqfx.web.fxzf.qbb.entities.QbbMbxx;
import com.supconit.kqfx.web.fxzf.qbb.services.QbbMbxxService;
import com.supconit.kqfx.web.util.EncodeTransforUtil;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

@RequestMapping("/fxzf/qbb/qbbmbxx")
@Controller("fxzf_qbb_qbbmbxx_controller")
public class QbbMbxxController {
	
	private transient static final Logger logger = LoggerFactory.getLogger(QbbMbxxController.class);
	private static final String MENU_CODE = "FXZF_QBB_TEMPLET";
	private static final String DIC_CODE_CZFS = "QBB_WAY";
	private static final String DIC_CODE_ZT = "QBB_FONT";
	private static final String DIC_CODE_YS = "QBB_COLOR";
	
	@Autowired
	private QbbMbxxService	qbbMbxxService;
	
	@Autowired
	private DataDictionaryService	dataDictionaryService;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private SystemLogService	systemLogService;
	/**
	 * 准备实体对象。
	 * 
	 * @return
	 */	
	@ModelAttribute("prepareQbbMbxx")
	public QbbMbxx prepareQbbMbxx() {
		return new QbbMbxx();
	}
	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model){
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"情报板模板信息列表", request.getRemoteAddr());
		return "fxzf/qbb/qbbmbxx/list";
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
	public Pagination<QbbMbxx> list(Pagination<QbbMbxx> pager, @FormBean(value = "condition", modelCode = "prepareQbbxxmb") QbbMbxx condition) {
		/* Validate Pager Parameters */
		if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE) return pager;
				
		this.qbbMbxxService.find(pager, condition); //获取分页查询结果
		if(!pager.isEmpty()){
			for (QbbMbxx qbbmbxx : pager) {
				if(null != qbbmbxx.getCzfs() && !qbbmbxx.getCzfs().isEmpty()){
					Data d = dataDictionaryService.getByDataCode(DIC_CODE_CZFS, qbbmbxx.getCzfs());
					if(d != null){
						qbbmbxx.setCzfsStr(d.getName());
					}
				}
				if(null != qbbmbxx.getFont() && !qbbmbxx.getFont().isEmpty()){
					Data d = dataDictionaryService.getByDataCode(DIC_CODE_ZT, qbbmbxx.getFont());
					if(d != null){
						qbbmbxx.setFontStr(d.getName());
					}
				}
				if(null != qbbmbxx.getColor() && !qbbmbxx.getColor().isEmpty()){
					Data d = dataDictionaryService.getByDataCode(DIC_CODE_YS, qbbmbxx.getColor());
					if(d != null){
						qbbmbxx.setColorStr(d.getName());
					}
					qbbmbxx.setXxnrHtml("<span style='color:#"+qbbmbxx.getColor()+"'>"+qbbmbxx.getXxnr()+"</span>");
				}else{
					qbbmbxx.setXxnrHtml(qbbmbxx.getXxnr());
				}
			}
		}
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"情报板模板信息列表查询", request.getRemoteAddr());
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
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"情报板模板信息添加页面", request.getRemoteAddr());
		return "fxzf/qbb/qbbmbxx/add";
	}
	
	/**
	 * 编辑展示。
	 * 
	 * @param id
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(required = true) String id,  ModelMap model) {
		model.put("QbbMbxx", this.qbbMbxxService.getById(id));
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"情报板模板信息修改页面", request.getRemoteAddr());
		return "fxzf/qbb/qbbmbxx/edit";
	}

	/**
	 * 保存编辑内容。
	 * 
	 * @param equipment
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"add", "edit"}, method = RequestMethod.POST)
	public AjaxMessage doEdit(@FormBean(value = "QbbMbxx", modelCode = "prepareQbbxxmb") QbbMbxx QbbMbxx) {
		try {
			if(EncodeTransforUtil.getGbkCodeingLength(QbbMbxx.getXxnr())>50){
				return AjaxMessage.error("信息内容字节长度已超过最大长度50,保存失败");
			}else{
				QbbMbxx.setTime(new Date());
				if(null == QbbMbxx.getId()){
					QbbMbxx.setDeleted(0);
					QbbMbxx.setId(IDGenerator.idGenerator());
					this.qbbMbxxService.insert(QbbMbxx);
					this.systemLogService.log(MENU_CODE, OperateType.insert.getCode(),
							"情报板模板信息添加", request.getRemoteAddr());
				}else{
					this.qbbMbxxService.update(QbbMbxx);
					this.systemLogService.log(MENU_CODE, OperateType.update.getCode(),
							"情报板模板信息修改", request.getRemoteAddr());
				}
				return AjaxMessage.success(QbbMbxx.getId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.systemLogService.log(MENU_CODE, OperateType.update.getCode(),
					"情报板模板信息修改", request.getRemoteAddr());
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	/**
	 * 删除一条记录。
	 * 
	 * @param QbbMbxx
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public AjaxMessage doDelete(QbbMbxx QbbMbxx) {
		try {
			if (null == QbbMbxx || null == QbbMbxx.getId()) return AjaxMessage.error("错误的参数。");
			this.qbbMbxxService.delete(QbbMbxx);
			this.systemLogService.log(MENU_CODE, OperateType.delete.getCode(),
					"情报板模板信息删除", request.getRemoteAddr());
			return AjaxMessage.success(QbbMbxx.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.systemLogService.log(MENU_CODE, OperateType.delete.getCode(),
					"情报板模板信息删除", request.getRemoteAddr());
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
	public String view(@RequestParam(required = true) String id,  ModelMap model) {
		model.put("QbbMbxx", this.qbbMbxxService.getById(id));
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"情报板模板信息查看", request.getRemoteAddr());
		return "fxzf/qbb/qbbmbxx/view";
	}
	
	/**
	 * 信息模板选择窗
	 * 
	 * @param model
	 * @param dlg_id
	 * @return
	 */
	@RequestMapping(value = "select", method = RequestMethod.GET)
	public String select(ModelMap model, String dlg_id,String count,Integer flag) {
		model.put("dlg_id", dlg_id);
		model.put("count", count);
		model.put("flag", flag);
		return "fxzf/qbb/qbbmbxx/select";
	}
	
	
	/**
	 * 信息模板选择窗
	 * 
	 * @param model
	 * @param dlg_id
	 * @return
	 */
	@RequestMapping(value = "app-select", method = RequestMethod.GET)
	public String appSelect(ModelMap model,String count,String content,String dlg_id) {
		try {
			model.put("count", count);
			model.put("content", URLDecoder.decode(content, "utf-8").replaceAll("-", "+"));
			model.put("dlg_id", dlg_id);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"情报板模板信息选取", request.getRemoteAddr());
		return "fxzf/warn/warninfo/mbxx";
	}
	
}
