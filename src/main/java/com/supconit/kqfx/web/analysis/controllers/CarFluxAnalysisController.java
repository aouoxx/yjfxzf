package com.supconit.kqfx.web.analysis.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pagination;
import hc.business.dic.domains.FlatData;
import hc.business.dic.entities.Data;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;
import hc.safety.manager.SafetyManager;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.kqfx.web.analysis.entities.DayReport;
import com.supconit.kqfx.web.analysis.entities.JgZcd;
import com.supconit.kqfx.web.analysis.services.DayReportService;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.util.DateUtil;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;


/**
 * 行车车流量统计
 * @author cjm
 *
 */
@RequestMapping("/analysis/carflux")
@Controller("analysis_carflux_controller")
public class CarFluxAnalysisController {
	
	private transient static final Logger logger = LoggerFactory.getLogger(CarFluxAnalysisController.class);
	
	private static final String MENU_CODE = "BRIDGE_DAY_ANALYSIS";
	
	private static final String DIC_OVERLOAD = "OVERLOADSTATUS";
	
	private static final String DIC_DETECT = "STATIONNAME";
	
	@Autowired
	private DayReportService	dayReportService;
	
	@Autowired
	private DataDictionaryService	dataDictionaryService;
	
	@Autowired
	private JgZcdService	jgZcdService;
	
	@Autowired
	private SafetyManager	safetyManager;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private SystemLogService	systemLogService;
	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model){
		List<JgZcd> zcdList = this.jgZcdService.getZcdListByAuth();
		
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		cal.add(Calendar.DATE, -1);
		
		model.put("begindate", DateUtil.formatDate(cal.getTime(), DateUtil.DATE_FORMAT_YYYYMMDD));
		model.put("zcdList", zcdList);
		
		Long jgid = this.jgZcdService.getJgIdByAuth();
		model.put("jgid", jgid);
		HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME",dataDictionaryService);
		for(String key:stationMap.keySet() ){
			model.put("station"+key,stationMap.get(key));
		}
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"按车流量统计列表", request.getRemoteAddr());
		return "analysis/carflux/list";
	}
	
	
	/**
	 * 获取柱状图数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getChartData", method = RequestMethod.POST)
	public AjaxMessage getChartData(@FormBean(value = "condition", modelCode = "dayReport") DayReport condition){
		try {
			//是否选择日期没有选择查看昨天数据信息
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			
			DayReport condition2 =  this.translateCondition(condition);
			List<DayReport> dayReports = this.getListByCondition(condition2);
			
			//设置legend
			FlatData legend = (FlatData) this.dataDictionaryService.getByCode(DIC_DETECT);
			Map<String,Object> resultMap = new HashMap<String,Object>();
			
			if(!CollectionUtils.isEmpty(dayReports)){
				//横轴数据
				List<String> xAisData = new ArrayList<String>();
				
				for(DayReport dayReport : dayReports){
					String xdate = format.format(dayReport.getTjDate());
					if(!xAisData.contains(xdate)){
						xAisData.add(xdate);
					}
				}
				resultMap.put("xAis", xAisData);
				resultMap.put("yAis", dayReports);
			}
				resultMap.put("legend", legend);
				this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
						"按车流量统计图表数据", request.getRemoteAddr());
				return AjaxMessage.success(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxMessage.success(e.toString());
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	 public Pagination<DayReport> list(Pagination<DayReport> pager,
			 @FormBean(value = "condition", modelCode = "dayReport") DayReport condition) {
		try {
			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
			DayReport condition2 =  this.translateCondition(condition);
			List<DayReport> dayReports = this.getListByCondition(condition2);
			
			if(!CollectionUtils.isEmpty(dayReports))
			{
				pager.setPageNo(1);
				pager.setTotal(dayReports.size());
				for (DayReport dayReport : dayReports) {
					pager.add(dayReport);
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"按车流量统计列表数据", request.getRemoteAddr());
		return pager;
	}
	
	
	
	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @param condition
	 */
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void exportAll(
			HttpServletRequest request,
			HttpServletResponse response,
			@FormBean(value = "condition", modelCode = "dayReport") DayReport condition) {
		// 是否选择日期没有选择查看昨天数据信息
		try {
			DayReport condition2 = this.translateCondition(condition);
			List<DayReport> dayReports = this.getListByCondition(condition2);
			if(!CollectionUtils.isEmpty(dayReports)){
				OutputStream out = null;
				String title = "按车流量统计记录_"+DateUtil.formatDate(new Date(), DateUtil.DB_TIME_PATTERN)+".xls";
				
				response.setHeader("Content-Disposition", "attachment; filename="
						+  new String(title.getBytes("GB2312"), "iso8859-1"));
			    response.setContentType("application/msexcel;charset=UTF-8");
			    out  =response.getOutputStream();
			    HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("按车流量统计"));
				HSSFRow top = sheet.createRow(0);
				
				HSSFRow row = sheet.createRow(1);
				HSSFCellStyle style1 = workbook.createCellStyle();
				HSSFCellStyle style2 = workbook.createCellStyle();
				HSSFCellStyle style3 = workbook.createCellStyle();		    
			    
				/** 字体font **/
				HSSFFont font1 = workbook.createFont();
				font1.setColor(HSSFColor.BLACK.index);
				font1.setFontHeightInPoints((short) 10);
				font1.setBoldweight((short) 24);
				font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				HSSFFont font2 = workbook.createFont();
				font2.setColor(HSSFColor.BLACK.index);
				font2.setFontHeightInPoints((short) 10);
				font2.setBoldweight((short) 24);
				
				style1.setFont(font1);
				style1=setHSSFCellStyle(style1);
				style1.setFillBackgroundColor(HSSFColor.AQUA.index);
				style1.setFillForegroundColor(HSSFColor.AQUA.index);
				style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				
				style2.setFont(font2);
				style2=setHSSFCellStyle(style2);
				
				style3.setFont(font1);
				style3=setHSSFCellStyle(style3);
				
				/** 字体居中 **/
				style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				
				String[] head = {"序号","日期","K110+150","K109+800","总计"};
				
				for (int i = 0; i < head.length; i++) {
					HSSFCell cell = top.createCell(i);
					cell.setCellStyle(style3);
				}
				
				//设置表头样式
				top.getSheet().addMergedRegion(new Region(0,(short)0,0,(short)(head.length-1)));
				HSSFCell celltop = top.createCell(0);
				top.setHeight((short) (200*4));
				celltop.setCellStyle(style3);
				
				//获取导出条件
				String conditionStr = "";
				//日期
				if(null!=condition.getBeginDate()||null!=condition.getEndDate()){
					if(null!=condition.getBeginDate()){
						conditionStr += "统计开始日期："+DateUtil.formatDate(condition.getBeginDate(), DateUtil.DATE_FORMAT_YYYYMMDD)+"\r\n";
					}
					if(null!=condition.getEndDate()){
						conditionStr += "统计截止日期："+DateUtil.formatDate(condition.getEndDate(), DateUtil.DATE_FORMAT_YYYYMMDD)+"\r\n";
					}
				}
				//机构ID
				if(null!=condition2.getDetects()&&condition2.getDetects().length>0)
				{
					conditionStr += "统计治超站：";
					for (int j = 0; j < condition2.getDetects().length; j++) {
						Data data = dataDictionaryService.getByDataCode(DIC_DETECT, condition2.getDetects()[j]);
						conditionStr += null!=data?data.getName():""+",";
					}
					
				}
				
				//设置表头内容
				celltop.setCellValue("按车流量统计 \r\n"+conditionStr);
				
				Integer[] colWidth = new Integer[head.length];
				
				for (int i = 0; i < head.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellValue(head[i]);
					cell.setCellStyle(style1);
					if(1<i&&i<5)
					{
						colWidth[i] = (head[i].length()+15)*256;
					}else{
						colWidth[i] = (head[i].length()+13)*256;
					}
					
				}
				for(int i=0; i<dayReports.size();i++){
					HSSFRow row1 = sheet.createRow(i+2);
					HSSFCell cell0 =row1.createCell(0);
				    cell0.setCellValue(1+i);
				    cell0.setCellStyle(style2);
				    //日期
				    HSSFCell cell1 =row1.createCell(1);
				    cell1.setCellValue(dayReports.get(i).getTjDateStr());
				    cell1.setCellStyle(style2);
				    //椒江大桥北
				    HSSFCell cell2 =row1.createCell(2);
				    cell2.setCellValue(dayReports.get(i).getDetectOne());
				    cell2.setCellStyle(style2);
				    //椒江大桥南
				    HSSFCell cell3 =row1.createCell(3);
				    cell3.setCellValue(dayReports.get(i).getDetectTwo());
				    cell3.setCellStyle(style2);
				    
				    //总计
				    HSSFCell cell4 =row1.createCell(4);
				    cell4.setCellValue(dayReports.get(i).getDetectTotal());
				    cell4.setCellStyle(style2);
				}
				
				 for (int i = 0; i < colWidth.length; i++) {
					 sheet.setColumnWidth(i,colWidth[i]);
				 }
				 workbook.write(out);
				 out.flush();
				 out.close();
			}else{
				logger.info("-------------------------数据结果为空---------------");
			}
			this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
					"按车流量统计数据导出", request.getRemoteAddr());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	HSSFCellStyle setHSSFCellStyle( HSSFCellStyle style){
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return style;
	}
	
	/**
	 * 统计分析查询条件
	 * @param condition
	 * @return
	 */
	public List<DayReport> getListByCondition(DayReport condition){
		//是否选择日期没有选择查看昨天数据信息
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		
		List<DayReport> dayReports = this.dayReportService.AnalysisCarFluxByCondition(condition);
		
		if(!CollectionUtils.isEmpty(dayReports)){
			//横轴数据
			for(DayReport dayReport : dayReports){
				dayReport.setTjDateStr(format.format(dayReport.getTjDate()));
				dayReport.setDetectTotal(dayReport.getDetectOne()+dayReport.getDetectTwo()+dayReport.getDetectThree()+dayReport.getDetectFour()+dayReport.getDetectFive());
			}
		}
		
		return !CollectionUtils.isEmpty(dayReports)?dayReports:new ArrayList<DayReport>();
	}
	
	/**
	 * 转译condition的detects
	 * @param condition
	 * @return
	 */
	public DayReport translateCondition(DayReport condition){
		
		List<JgZcd> zcdList = new ArrayList<JgZcd>();
		String[] detects = null;
		
		//根据治超站获取对应的机构治超站列表以及查询所需数组
		if(!"null".equals(condition.getDetectStation())&&!StringUtil.isEmpty(condition.getDetectStation()))
		{
			detects = condition.getDetectStation().split(",");
		}else{
			zcdList  = this.jgZcdService.getZcdListByAuth();
			detects = new String[zcdList.size()];
			for (int i = 0; i < zcdList.size(); i++) {
				detects[i] = zcdList.get(i).getDeteStation();
			}
		}
		
		condition.setDetects(detects);
		
		return condition;
	}
}
