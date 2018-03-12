package com.supconit.kqfx.web.analysis.controllers;

import hc.base.domains.AjaxMessage;
import hc.business.dic.entities.Data;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;
import hc.safety.manager.SafetyManager;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
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

import com.supconit.kqfx.web.analysis.entities.CarModel;
import com.supconit.kqfx.web.analysis.entities.JgZcd;
import com.supconit.kqfx.web.analysis.services.CarModelService;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.util.DateUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 行车车型统计
 * 
 * @author cjm
 * 
 */
@RequestMapping("/analysis/cartype")
@Controller("analysis_cartype_controller")
public class CarTypeAnalysisController {

	private transient static final Logger logger = LoggerFactory
			.getLogger(CarTypeAnalysisController.class);

	private static final String MENU_CODE = "BRIDGE_DAY_ANALYSIS";

	private static final String DIC_CAR_TYPE = "FXZF_CAR_MODEL";

	private static final String DIC_DETECT = "STATIONNAME";

	@Autowired
	private CarModelService carModelService;

	@Autowired
	private DataDictionaryService dataDictionaryService;

	@Autowired
	private JgZcdService jgZcdService;

	@Autowired
	private SafetyManager safetyManager;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private SystemLogService	systemLogService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		List<JgZcd> zcdList = this.jgZcdService.getZcdListByAuth();

		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), 0, 0, 0);
		cal.add(Calendar.DATE, -1);

		model.put("begindate", DateUtil.formatDate(cal.getTime(),
				DateUtil.DATE_FORMAT_YYYYMMDD));
		model.put("zcdList", zcdList);
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"按车型统计列表", request.getRemoteAddr());
		return "analysis/cartype/list";
	}

	/**
	 * 
	 * @param condition
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getchartdata")
	public AjaxMessage getChartData(
			@FormBean(value = "condition", modelCode = "prepareCarModel") CarModel condition) {

		try {
			CarModel condition2 = this.translateCondition(condition);
			// 是否选择日期没有选择查看昨天数据信息
			List<CarModel> carModels = this.getListByCondition(condition2);

			// 设置legend
			String[] legend = { "2轴", "3轴", "4轴", "5轴", "6轴", "6轴以上" };
			Map<String, Object> resultMap = new HashMap<String, Object>();

			resultMap.put("yAis", carModels);
			resultMap.put("legend", legend);
			this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
					"按车型统计查询图表数据", request.getRemoteAddr());
			return AjaxMessage.success(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxMessage.success(e.toString());
		}
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
			@FormBean(value = "condition", modelCode = "prepareCarModel") CarModel condition) {
		// 是否选择日期没有选择查看昨天数据信息
		try {
			CarModel condition2 = this.translateCondition(condition);
			List<CarModel> carModels = this.getListByCondition(condition2);
			if(!CollectionUtils.isEmpty(carModels)){
				OutputStream out = null;
				String title = "按车型统计记录_"+DateUtil.formatDate(new Date(), DateUtil.DB_TIME_PATTERN)+".xls";
				
				response.setHeader("Content-Disposition", "attachment; filename="
						+  new String(title.getBytes("GB2312"), "iso8859-1"));
			    response.setContentType("application/msexcel;charset=UTF-8");
			    out  =response.getOutputStream();
			    HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("按车型统计"));
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
				
				String[] head = {"轴数","车辆类型","额定核载参数","车长特征参数","百分比"};
				
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
				celltop.setCellValue("按车型统计 \r\n"+conditionStr);
				
				Integer[] colWidth = new Integer[head.length];
				
				for (int i = 0; i < head.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellValue(head[i]);
					cell.setCellStyle(style1);
					if(i==2)
					{
						colWidth[i] = (head[i].length()+40)*256;
					}else{
						colWidth[i] = (head[i].length()+13)*256;
					}
					
				}
				
				CarModel carModel = carModels.get(0);
				//2轴
				if(0!=carModel.getAxisTwo()){
					double f = ((double)carModel.getAxisTwo()/carModel.getAxisTotal())*100;
					BigDecimal  b  =  new   BigDecimal(f);  
					double result  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					HSSFRow row1 = sheet.createRow(sheet.getLastRowNum()+1);
					HSSFCell cell = row1.createCell(0);
					cell.setCellValue("2轴");
					cell.setCellStyle(style2);
					HSSFCell cell1 = row1.createCell(1);
					cell1.setCellValue("小客车或小货车");
					cell1.setCellStyle(style2);
					HSSFCell cell2 = row1.createCell(2);
					cell2.setCellValue("中小客车：额定座位≤19座；小货车：载质量≤2吨");
					cell2.setCellStyle(style2);
					HSSFCell cell3 = row1.createCell(3);
					cell3.setCellValue("车长<6m");
					cell3.setCellStyle(style2);
					HSSFCell cell4 = row1.createCell(4);
					cell4.setCellValue(result+"%");
					cell4.setCellStyle(style2);
				}
				//3轴
				if(0!=carModel.getAxisThree()){
					double f = ((double)carModel.getAxisThree()/carModel.getAxisTotal())*100;
					BigDecimal  b  =  new   BigDecimal(f);  
					double result  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
					HSSFRow row1 = sheet.createRow(sheet.getLastRowNum()+1);
					HSSFCell cell = row1.createCell(0);
					cell.setCellValue("3轴");
					cell.setCellStyle(style2);
					HSSFCell cell1 = row1.createCell(1);
					cell1.setCellValue("大货车");
					cell1.setCellStyle(style2);
					HSSFCell cell2 = row1.createCell(2);
					cell2.setCellValue("大客车：额定座位>19座；中货车：2吨<载质量≤7吨");
					cell2.setCellStyle(style2);
					HSSFCell cell3 = row1.createCell(3);
					cell3.setCellValue("车长<6m");
					cell3.setCellStyle(style2);
					HSSFCell cell4 = row1.createCell(4);
					cell4.setCellValue(result+"%");
					cell4.setCellStyle(style2);
				}
				//4轴
				if(0!=carModel.getAxisFour()){
					double f = ((double)carModel.getAxisFour()/carModel.getAxisTotal())*100;
					BigDecimal  b  =  new   BigDecimal(f);  
					double result  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
					HSSFRow row1 = sheet.createRow(sheet.getLastRowNum()+1);
					HSSFCell cell = row1.createCell(0);
					cell.setCellValue("4轴");
					cell.setCellStyle(style2);
					HSSFCell cell1 = row1.createCell(1);
					cell1.setCellValue("大货车");
					cell1.setCellStyle(style2);
					HSSFCell cell2 = row1.createCell(2);
					cell2.setCellValue("7吨<载质量≤20吨");
					cell2.setCellStyle(style2);
					HSSFCell cell3 = row1.createCell(3);
					cell3.setCellValue("6m≤车长≤12m");
					cell3.setCellStyle(style2);
					HSSFCell cell4 = row1.createCell(4);
					cell4.setCellValue(result+"%");
					cell4.setCellStyle(style2);
				}
				//5轴
				if(0!=carModel.getAxisFive()){
					double f = ((double)carModel.getAxisFive()/carModel.getAxisTotal())*100;
					BigDecimal  b  =  new   BigDecimal(f);  
					double result  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
					HSSFRow row1 = sheet.createRow(sheet.getLastRowNum()+1);
					HSSFCell cell = row1.createCell(0);
					cell.setCellValue("5轴");
					cell.setCellStyle(style2);
					HSSFCell cell1 = row1.createCell(1);
					cell1.setCellValue("特大货车");
					cell1.setCellStyle(style2);
					HSSFCell cell2 = row1.createCell(2);
					cell2.setCellValue("20吨<载质量");
					cell2.setCellStyle(style2);
					HSSFCell cell3 = row1.createCell(3);
					cell3.setCellValue("车长>12m");
					cell3.setCellStyle(style2);
					HSSFCell cell4 = row1.createCell(4);
					cell4.setCellValue(result+"%");
					cell4.setCellStyle(style2);
				}
				//6轴
				if(0!=carModel.getAxisSix()){
					double f = ((double)carModel.getAxisSix()/carModel.getAxisTotal())*100;
					BigDecimal  b  =  new   BigDecimal(f);  
					double result  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
					HSSFRow row1 = sheet.createRow(sheet.getLastRowNum()+1);
					HSSFCell cell = row1.createCell(0);
					cell.setCellValue("6轴");
					cell.setCellStyle(style2);
					HSSFCell cell1 = row1.createCell(1);
					cell1.setCellValue("特大货车");
					cell1.setCellStyle(style2);
					HSSFCell cell2 = row1.createCell(2);
					cell2.setCellValue("20吨<载质量");
					cell2.setCellStyle(style2);
					HSSFCell cell3 = row1.createCell(3);
					cell3.setCellValue("车长>12m");
					cell3.setCellStyle(style2);
					HSSFCell cell4 = row1.createCell(4);
					cell4.setCellValue(result+"%");
					cell4.setCellStyle(style2);
				}
				//6轴以上
				if(0!=carModel.getAxisSeven()){
					double f = ((double)carModel.getAxisSeven()/carModel.getAxisTotal())*100;
					BigDecimal  b  =  new   BigDecimal(f);  
					double result  =  b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					HSSFRow row1 = sheet.createRow(sheet.getLastRowNum()+1);
					HSSFCell cell = row1.createCell(0);
					cell.setCellValue("6轴以上");
					cell.setCellStyle(style2);
					HSSFCell cell1 = row1.createCell(1);
					cell1.setCellValue("特大货车");
					cell1.setCellStyle(style2);
					HSSFCell cell2 = row1.createCell(2);
					cell2.setCellValue("20吨<载质量");
					cell2.setCellStyle(style2);
					HSSFCell cell3 = row1.createCell(3);
					cell3.setCellValue("车长>12m");
					cell3.setCellStyle(style2);
					HSSFCell cell4 = row1.createCell(4);
					cell4.setCellValue(result+"%");
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
					"按车型统计数据导出", request.getRemoteAddr());
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
	 * 
	 * @param condition
	 * @return
	 */
	public List<CarModel> getListByCondition(CarModel condition) {
		
		List<CarModel> carModels = this.carModelService
				.AnalysisByCondition(condition);
		if (!CollectionUtils.isEmpty(carModels))
			for (CarModel carModel : carModels) {
				if (null != carModel)
					carModel.setAxisTotal(carModel.getAxisTwo()
							+ carModel.getAxisThree() + carModel.getAxisFour()
							+ carModel.getAxisFive() + carModel.getAxisSix()
							+ carModel.getAxisSeven());
			}

		return !CollectionUtils.isEmpty(carModels) && null != carModels.get(0) ? carModels
				: new ArrayList<CarModel>();
	}
	
	
	/**
	 * 将条件中多个治超站转译
	 * @param condition
	 * @return
	 */
	public CarModel translateCondition(CarModel condition){
		
		// 是否选择日期没有选择查看昨天数据信息
		List<JgZcd> zcdList = new ArrayList<JgZcd>();
		String[] detects = null;

		// 根据治超站获取对应的机构治超站列表以及查询所需数组
		if (!"null".equals(condition.getDetectStation())&&!StringUtil.isEmpty(condition.getDetectStation())) {
			detects = condition.getDetectStation().split(",");
		} else {
			zcdList = this.jgZcdService.getZcdListByAuth();
			detects = new String[zcdList.size()];
			for (int i = 0; i < zcdList.size(); i++) {
				detects[i] = zcdList.get(i).getDeteStation();
			}
		}

		condition.setDetects(detects);
		return condition;
	}

}

