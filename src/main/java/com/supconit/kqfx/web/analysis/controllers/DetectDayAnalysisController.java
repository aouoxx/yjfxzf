package com.supconit.kqfx.web.analysis.controllers;

import hc.base.domains.AjaxMessage;
import hc.business.dic.domains.FlatData;
import hc.business.dic.entities.Data;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;
import hc.safety.manager.SafetyManager;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.kqfx.web.analysis.entities.DayReport;
import com.supconit.kqfx.web.analysis.entities.JgZcd;
import com.supconit.kqfx.web.analysis.services.DayReportService;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.util.DateUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.services.JgxxService;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 治超点行车日期统计
 * @author cjm
 *
 */
@SuppressWarnings("deprecation")
@RequestMapping("/analysis/detectday")
@Controller("analysis_detectday_controller")
public class DetectDayAnalysisController {

	private transient static final Logger logger = LoggerFactory.getLogger(DetectDayAnalysisController.class);
	
	private static final String MENU_CODE = "DETECT_DAY_ANALYSIS";
	
	private static final String DIC_OVERLOAD = "OVERLOADSTATUS";
	
	private static final String DIC_DETECT = "STATIONNAME";
	
	@Autowired
	private DayReportService	dayReportService;
	
	@Autowired
	private DataDictionaryService	dataDictionaryService;
	
	@Autowired
	private JgZcdService	jgZcdService;
	
	@Autowired
	private JgxxService		jgxxService;
	
	@Autowired
	private SafetyManager	safetyManager;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private SystemLogService	systemLogService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model){
		List<JgZcd> jgZcdList = this.jgZcdService.getZcdListByAuth();
		
		model.put("jgZcdList", jgZcdList);
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"治超站行车统计列表", request.getRemoteAddr());
		return "analysis/detectday/list";
	}
	
	/**
	 * 
	 * @param condition
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getchartdata")
	public AjaxMessage getChartData(@FormBean(value = "condition", modelCode = "prepareDayReport") DayReport condition){
		try {
			this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
					"治超站行车统计查询图表数据", request.getRemoteAddr());
			return AjaxMessage.success(this.getChartDataMap(condition));
		} catch (Exception e) {
			e.printStackTrace();
			this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
					"治超站行车统计查询图表数据", request.getRemoteAddr());
			return AjaxMessage.error(e.toString());
		}
	}
	
	
	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @param total
	 * @param condition
	 */
	@SuppressWarnings({"unchecked"})
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void exportAll(HttpServletRequest request,
							HttpServletResponse response,
							@FormBean(value = "condition", modelCode = "prepareDayReport") DayReport condition){
	     try {
			 Map<String, Object> resultMap = this.getChartDataMap(condition);
			 @SuppressWarnings("unused")
			List<JgZcd> jgZcdList = (List<JgZcd>) resultMap.get("jgZcdList");
			 FlatData dicdata = (FlatData) resultMap.get("dicdata");
			 List<DayReport> dayReportList = (List<DayReport>) resultMap.get("dayReportList");
			 if(!CollectionUtils.isEmpty(dayReportList)){
				logger.info("-------------------------导出excel列表---------------");
				OutputStream out = null;
				String title = "治超站行车统计记录_"+DateUtil.formatDate(new Date(), DateUtil.DB_TIME_PATTERN)+".xls";
				
				response.setHeader("Content-Disposition", "attachment; filename="
						+  new String(title.getBytes("GB2312"), "iso8859-1"));
			    response.setContentType("application/msexcel;charset=UTF-8");
			    out  =response.getOutputStream();
			    HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("治超站行车统计"));
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
				
				//设置表头长度
				for(int i=0;i<dicdata.size()+2;i++){
					HSSFCell cell = top.createCell(i);
					cell.setCellStyle(style3);
				}
				//设置表头样式
				top.getSheet().addMergedRegion(new Region(0,(short)0,0,(short)(dicdata.size()+1)));
				HSSFCell celltop = top.createCell(0);
				top.setHeight((short) (200*4));
				celltop.setCellStyle(style3);
				
				//获取导出条件
				String conditionStr = "";
				//日期
				if(null!=condition.getTjDate()){
					conditionStr += "统计日期："+DateUtil.formatDate(condition.getTjDate(), DateUtil.DATE_FORMAT_YYYYMMDD+"\r\n");
				}
				//机构ID
				if(!StringUtil.isEmpty(condition.getDetectStation()))
				{
					Data data = this.dataDictionaryService.getByDataCode(DIC_DETECT, condition.getDetectStation());
					conditionStr += null!=data?"治超站:"+data.getName():"";
				}
				
				//设置表头内容
				celltop.setCellValue("治超站行车统计 \r\n"+conditionStr);
				
				Integer[] colWidth = new Integer[dicdata.size()+2];
				for (int i = 0; i < dicdata.size()+2; i++) {
					HSSFCell cell = row.createCell(i);
					if(i==0)
					{
						cell.setCellValue("");
						colWidth[i] = 20*256;
					}else if(i==dicdata.size()+1){
						cell.setCellValue("汇总");
						colWidth[i] = 12*256;
					}else{
						cell.setCellValue(dicdata.get(i-1).getName());
						colWidth[i] = (dicdata.get(i-1).getName().length()+10)*256;
					}
					cell.setCellStyle(style1);
				}
				
				Integer count0 = 0;
				Integer count1 = 0;
				Integer count2 = 0;
				Integer count3 = 0;
				Integer count4 = 0;
				Integer count5 = 0;
				Integer count6 = 0;
				
				for(int i=0; i<dayReportList.size();i++){
					HSSFRow row1 = sheet.createRow(i+2);
					HSSFCell cell0 =row1.createCell(0);
				    cell0.setCellValue(dayReportList.get(i).getDetectStationName());
				    cell0.setCellStyle(style2);
				    //正常
				    HSSFCell cell1 =row1.createCell(1);
				    cell1.setCellValue(dayReportList.get(i).getOverLoadNormal());
				    cell1.setCellStyle(style2);
				    //轻微
				    HSSFCell cell2 =row1.createCell(2);
				    cell2.setCellValue(dayReportList.get(i).getOverLoadQingWei());
				    cell2.setCellStyle(style2);
				    //一般
				    HSSFCell cell3 =row1.createCell(3);
				    cell3.setCellValue(dayReportList.get(i).getOverLoadYiBan());
				    cell3.setCellStyle(style2);
				    //较重
				    HSSFCell cell4 =row1.createCell(4);
				    cell4.setCellValue(dayReportList.get(i).getOverLoadJiaoZhong());
				    cell4.setCellStyle(style2);
				    //严重
				    HSSFCell cell5 =row1.createCell(5);
				    cell5.setCellValue(dayReportList.get(i).getOverLoadYanzhong());
				    cell5.setCellStyle(style2);
				    //特别严重
				    HSSFCell cell6 =row1.createCell(6);
				    cell6.setCellValue(dayReportList.get(i).getOverLoadTebieYanZhong());
				    cell6.setCellStyle(style2);
				    //汇总
				    HSSFCell cell7 =row1.createCell(7);
				    cell7.setCellValue(dayReportList.get(i).getOverloadTotal());
				    cell7.setCellStyle(style2);
				    
				    count0 += dayReportList.get(i).getOverLoadNormal();
				    count1 += dayReportList.get(i).getOverLoadQingWei();
				    count2 += dayReportList.get(i).getOverLoadYiBan();
				    count3 += dayReportList.get(i).getOverLoadJiaoZhong();
				    count4 += dayReportList.get(i).getOverLoadYanzhong();
				    count5 += dayReportList.get(i).getOverLoadTebieYanZhong();
				    count6 += dayReportList.get(i).getOverloadTotal();
				}
				
				HSSFRow row1 = sheet.createRow(sheet.getLastRowNum()+1);
				HSSFCell cell0 =row1.createCell(0);
			    cell0.setCellValue("汇总");
			    cell0.setCellStyle(style2);
			    //正常
			    HSSFCell cell1 =row1.createCell(1);
			    cell1.setCellValue(count0);
			    cell1.setCellStyle(style2);
			    //轻微
			    HSSFCell cell2 =row1.createCell(2);
			    cell2.setCellValue(count1);
			    cell2.setCellStyle(style2);
			    //一般
			    HSSFCell cell3 =row1.createCell(3);
			    cell3.setCellValue(count2);
			    cell3.setCellStyle(style2);
			    //较重
			    HSSFCell cell4 =row1.createCell(4);
			    cell4.setCellValue(count3);
			    cell4.setCellStyle(style2);
			    //严重
			    HSSFCell cell5 =row1.createCell(5);
			    cell5.setCellValue(count4);
			    cell5.setCellStyle(style2);
			    //特别严重
			    HSSFCell cell6 =row1.createCell(6);
			    cell6.setCellValue(count5);
			    cell6.setCellStyle(style2);
			    //汇总
			    HSSFCell cell7 =row1.createCell(7);
			    cell7.setCellValue(count6);
			    cell7.setCellStyle(style2);
			    
				 for (int i = 0; i < colWidth.length; i++) {
					 sheet.setColumnWidth(i,colWidth[i]);
				 }
				 workbook.write(out);
				 out.flush();
				 out.close();
				}else{
					logger.info("-------------------------数据结果为空---------------");
				}
			} catch (Exception e) {
				 e.printStackTrace();
				 logger.info("-------------------------导出全部excel列表---------------");
			}
	     this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
					"治超站行车统计数据导出", request.getRemoteAddr());
	}
	
	HSSFCellStyle setHSSFCellStyle( HSSFCellStyle style){
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return style;
	}
	
	
	public Map<String,Object> getChartDataMap(DayReport condition){
		
		//统计数据
		List<DayReport> dayReportList = this.dayReportService.CountDetectByCondition(this.translateCondition(condition));
		
		if(!CollectionUtils.isEmpty(dayReportList))
		{
			for (DayReport dayReport : dayReportList) {
				if(null!=dayReport){
					dayReport.setOverloadTotal(dayReport.getOverLoadNormal()
							+ dayReport.getOverLoadQingWei()
							+ dayReport.getOverLoadYiBan()
							+ dayReport.getOverLoadJiaoZhong()
							+ dayReport.getOverLoadYanzhong()
							+ dayReport.getOverLoadTebieYanZhong());
					Data data = this.dataDictionaryService.getByDataCode(DIC_DETECT,dayReport.getDetectStation());
					dayReport.setDetectStationName(null!=data?data.getName():"");
				}
			}
		}
		
		//机构信息
		List<JgZcd> jgZcdList = this.jgZcdService.getZcdListByAuth();
		
		//数据字典项
		FlatData dicdata = (FlatData) this.dataDictionaryService.getByCode(DIC_OVERLOAD);
		
		if(0==condition.getChart()&&null!=condition.getDetectStation()){
			if(!CollectionUtils.isEmpty(jgZcdList)){
				for (int i = 0; i < jgZcdList.size(); i++) {
					if(!jgZcdList.get(i).getDeteStation().equals(condition.getDetectStation()))
						jgZcdList.remove(i);	
				}
			}
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("jgZcdList", jgZcdList);
		resultMap.put("dicdata", dicdata);
		resultMap.put("dayReportList", dayReportList);
		
		return resultMap;
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
			detects = new String[1];
			detects[0] = condition.getDetectStation();
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
