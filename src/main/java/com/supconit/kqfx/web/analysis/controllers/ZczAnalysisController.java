package com.supconit.kqfx.web.analysis.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;
import hc.safety.manager.SafetyManager;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.honeycomb.business.authorization.services.RoleService;
import com.supconit.honeycomb.business.organization.services.DepartmentService;
import com.supconit.honeycomb.business.organization.services.PersonService;
import com.supconit.kqfx.web.analysis.entities.DayReport;
import com.supconit.kqfx.web.analysis.entities.JgZcd;
import com.supconit.kqfx.web.analysis.services.DayReportService;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 按治超站违法统计
 * @author gs
 *
 */
@SuppressWarnings("deprecation")
@RequestMapping("/analysis/overload/zczoverload")
@Controller("analysis_zczoverLoad_controller")
public class ZczAnalysisController {
	
	private static final String MODULE_CODE = "ZCZ_ANALYSIS";
	
	@Autowired
	private DayReportService dayReportService;
	
	@Autowired
	private DataDictionaryService	dataDictionaryService;
	
	@Autowired
	private SafetyManager safetyManager;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private JgZcdService jgZcdService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Resource
	private HttpServletRequest request;
	
	@ModelAttribute("dayReport")
	private DayReport getDayReport(){
		DayReport dayReport = new DayReport();
		return dayReport;
	}
	
	private transient static final Logger logger = LoggerFactory.getLogger(ZczAnalysisController.class);
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model){
		this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(),
				"按治超站超限统计", request.getRemoteAddr());
		User user = (User) safetyManager.getAuthenticationInfo().getUser();
		if(null!=user&&null!=user.getPerson()&&null!=user.getPersonId())
		{
			ExtPerson person = personService.getById(user.getPersonId());
			//根据JGID进行权限限制
			//若是超级管理员查询JGID = null
			//大桥的为 JGID=133
			// 二桥为JGID=134
			model.put("jgid", person.getJgbh());
		}
		HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME",dataDictionaryService);
		for(String key:stationMap.keySet() ){
			model.put("station"+key,stationMap.get(key));
		}
		return "analysis/overload/zczoverload/list";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	 public Pagination<DayReport> list(Pagination<DayReport> pager,
			 @FormBean(value = "condition", modelCode = "dayReport") DayReport condition) {
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"按治超站超限统计", request.getRemoteAddr());
			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
			condition = setDayReport(condition);
			dayReportService.findZczByPager(pager, condition);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
			for(DayReport dayReport : pager){
				dayReport.setTjDateStr(formatter.format(dayReport.getTjDate()));
				if(dayReport.getDetectOne()==null){dayReport.setDetectOne(0);}
				if(dayReport.getDetectTwo()==null){dayReport.setDetectTwo(0);}
				if(dayReport.getDetectThree()==null){dayReport.setDetectThree(0);}
				if(dayReport.getDetectFour()==null){dayReport.setDetectFour(0);}
				if(dayReport.getDetectFive()==null){dayReport.setDetectFive(0);}
				if(condition.getJgid()==null){
					dayReport.setTotal(dayReport.getDetectOne()+dayReport.getDetectTwo()+dayReport.getDetectThree()+
							dayReport.getDetectFour()+dayReport.getDetectFive());
				}else{
					if(condition.getJgid()==133){
						dayReport.setTotal(dayReport.getDetectOne()+dayReport.getDetectTwo());
					}else if(condition.getJgid()==134){
						dayReport.setTotal(dayReport.getDetectThree()+
								dayReport.getDetectFour()+dayReport.getDetectFive());
					}else{
						dayReport.setTotal(dayReport.getDetectOne()+dayReport.getDetectTwo()+dayReport.getDetectThree()+
								dayReport.getDetectFour()+dayReport.getDetectFive());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}
	
	/**
	 * 设置默认循环治超站的权限
	 */
	public DayReport setDayReport(DayReport condition){
		if(condition.getDetectStation()!=null){
			condition.setDetects(condition.getDetectStation().split(","));
		}else{
//			根据jgid获取能够查看治超站的权限
			if(condition.getJgid()==null){
//				无jgid的默认为所有治超站权限
				condition.setDetects(null);
			}else{
//				含有jgid设置对应的权限
				List<JgZcd> zcdList = jgZcdService.getByJgid(condition.getJgid());
				String detectStations="";
				for(JgZcd jgZcd:zcdList){
					detectStations=detectStations+jgZcd.getDeteStation()+",";
				}
				condition.setDetects(detectStations.substring(0, detectStations.length()-1).split(","));
			}
		}
		if(condition.getOverloadStatusStr()==null||condition.getOverloadStatusStr().equals("null")){
			condition.setIllegals(null);
		}else{
			String[] illegals = condition.getOverloadStatusStr().split(",");
			Integer[] datas = new Integer[illegals.length];
			for(int j = 0 ;j<illegals.length;j++){
				datas[j]=Integer.valueOf(illegals[j]);
			}
			condition.setIllegals(datas);
		}
		
		return condition;
	}
	
	
	/**
	 * 获取违法程度数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "overLoadStatus", method = RequestMethod.POST)
	AjaxMessage getIlleagls(){
		try {
			HashMap<String, String> illeagalMap = DictionaryUtil.dictionary("OVERLOADSTATUS",dataDictionaryService);
			List<String> stationList = new ArrayList<String>() ;
			for(String key:illeagalMap.keySet() ){
				stationList.add(key+":"+illeagalMap.get(key));
			}
			return AjaxMessage.success(stationList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * 获取图标数据
	 */
	@ResponseBody
	@RequestMapping(value = "getChartData", method = RequestMethod.POST)
	public AjaxMessage getChartData(@FormBean(value = "condition", modelCode = "dayReport") DayReport condition){
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(),
					"查询按治超站超限图表信息", request.getRemoteAddr());
			
//			是否选择日期没有选择查看昨天数据信息,昨天的时间日期是在页面中进行设置的
			if(condition.getOverloadStatusStr()!=null&&!condition.getOverloadStatusStr().equals("")){
				String[] nos=condition.getOverloadStatusStr().split(",");
				Integer ills[]=new Integer[nos.length];
				for(int i=0;i<nos.length;i++){
					ills[i]=Integer.valueOf(nos[i]);
				}
				condition.setIllegals(ills);
			}
			List<DayReport> zczoverLoadList = new ArrayList<DayReport>();
			condition=setDayReport(condition);
			zczoverLoadList =dayReportService.AnalysisZczByCondition(condition);
			if(!CollectionUtils.isEmpty(zczoverLoadList)){
//				设置X轴数据
				List<String> xAisData = new ArrayList<String>();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				for(DayReport dayReport : zczoverLoadList){
					String date = formatter.format(dayReport.getTjDate());
					if(!xAisData.contains(date)){
						xAisData.add(date);
					}
				}
//				设置legend(治超站)
				HashMap<String, String> detectionLengend = DictionaryUtil.dictionary("STATIONNAME",dataDictionaryService);
				List<String> legend =new ArrayList<String>();
				Iterator<String> iterator  = detectionLengend.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					legend.add(detectionLengend.get(key));
				}
				legend.add("汇总");
				
//				设置y轴数据
				List<DayReport> yAisData = new ArrayList<DayReport>();
				for(DayReport day:zczoverLoadList){
					if(day.getDetectOne()==null){day.setDetectOne(0);}
					if(day.getDetectTwo()==null){day.setDetectTwo(0);}
					if(day.getDetectThree()==null){day.setDetectThree(0);}
					if(day.getDetectFour()==null){day.setDetectFour(0);}
					if(day.getDetectFive()==null){day.setDetectFive(0);}
					day.setTotal(day.getDetectOne()+day.getDetectTwo()+day.getDetectThree()+
								day.getDetectFour()+day.getDetectFive());
					
					yAisData.add(day);
				}
				
				//标志查询结果不为空
				Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("xAis", xAisData);
				resultMap.put("legend", legend);
				resultMap.put("yAis", yAisData);
				resultMap.put("success", 200);
				return AjaxMessage.success(resultMap);
			}else{
				//标志查询结果为空
				Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("success", 1002);
				return AjaxMessage.success(resultMap);
			}
		} catch (Exception e) {
			//标志查询结果不为空
			logger.error("", e);
			return AjaxMessage.success("请求服务器错误");
		}
	}


	@RequestMapping(value = "exportAll", method = RequestMethod.GET)
	public void exportAll(HttpServletRequest request,
							HttpServletResponse response,
							String  total,
							@FormBean(value = "condition", modelCode = "dayReport") DayReport condition){
		 logger.info("-------------------------导出全部excel列表---------------");
	     try {
	    	 this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
						"导出按违法程度统计全部记录", request.getRemoteAddr());
			 Pagination<DayReport> pager = new Pagination<DayReport>();
			 pager.setPageNo(1);
			 pager.setPageSize(Integer.MAX_VALUE);
			 
			 condition = setDayReport(condition);
			 dayReportService.findZczByPager(pager, condition);
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			 for(DayReport dayReport : pager){
				dayReport.setTjDateStr(formatter.format(dayReport.getTjDate()));
				if(dayReport.getDetectOne()==null){dayReport.setDetectOne(0);}
				if(dayReport.getDetectTwo()==null){dayReport.setDetectTwo(0);}
				if(dayReport.getDetectThree()==null){dayReport.setDetectThree(0);}
				if(dayReport.getDetectFour()==null){dayReport.setDetectFour(0);}
				if(dayReport.getDetectFive()==null){dayReport.setDetectFive(0);}
				dayReport.setTotal(dayReport.getDetectOne()+dayReport.getDetectTwo()+dayReport.getDetectThree()+
						dayReport.getDetectFour()+dayReport.getDetectFive());
				
			 }
			Date date = new Date();
		    String time = formatter.format(date);
			String title = "治超站超限统计记录_"+time+".xls";
			editExcel(pager,response,title,condition);
			} catch (Exception e) {
				 logger.info("-------------------------导出全部excel列表---------------");
			}
	}
	
	 HSSFCellStyle setHSSFCellStyle( HSSFCellStyle style){
	 		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	 		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	 		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	 		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	 		return style;
	 	}
	 
	 
	 void editExcel(Pageable<DayReport> pager,HttpServletResponse response,String title, DayReport condition){
			OutputStream out=null;
			try{
			response.setHeader("Content-Disposition", "attachment; filename="
					+  new String(title.getBytes("GB2312"), "iso8859-1"));
		    response.setContentType("application/msexcel;charset=UTF-8");
		    out  =response.getOutputStream();
		    HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("治超站超限统计记录"));
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
			
			
			//获取导出条件
			String conditionString ="";
			//获取导出的时间
			if(condition.getBeginDate()!=null){
				SimpleDateFormat format1  = new SimpleDateFormat("yyyy-MM-dd");
				String begin = format1.format(condition.getBeginDate());
				if(condition.getEndDate()!=null){
					String end = format1.format(condition.getEndDate());
					end = format1.format(condition.getEndDate());
					conditionString="统计日期："+begin+" 至  "+end+"\r\n";
				}else{
					conditionString="统计日期："+begin+"开始至今为止\r\n";
				}
			}
			//获取违法程度
			HashMap<String, String> overLoadMap = DictionaryUtil.dictionary("OVERLOADSTATUS",dataDictionaryService);
			String overString = "";
			if(condition.getOverloadStatusStr()==null||condition.getOverloadStatusStr().equals("")||condition.getOverloadStatusStr()==""||condition.getOverloadStatusStr().equals("null")){
				String[] detects=new String[]{"0","1","2","3","4","5"};
				for(int i=0;i<detects.length;i++){
					overString= overString+" "+overLoadMap.get(detects[i]);
				}
			}else{
				String[] detects = condition.getOverloadStatusStr().split(",");
				for(int i=0;i<detects.length;i++){
					overString= overString+" "+overLoadMap.get(detects[i]);
				}
			}
			conditionString=conditionString+"统计违法程度："+overString;
			
			//获取用户权限,根据权限导出数据
			User user = (User) safetyManager.getAuthenticationInfo().getUser();
			if(null!=user&&null!=user.getPerson()&&null!=user.getPersonId())
			{
				ExtPerson person = personService.getById(user.getPersonId());
				condition.setJgid(person.getJgbh());
			}
			if(condition.getJgid()==null){
				//导出全部
				exportExcelAll(pager,conditionString,style1,style2,style3,row,top,sheet);
			}else{
				if(condition.getJgid()==133){
					//导出JGBH133
					exportExcelJgid133(pager,conditionString,style1,style2,style3,row,top,sheet);
				}else if(condition.getJgid()==134){
					//导出JGBH134
					exportExcelJgid134(pager,conditionString,style1,style2,style3,row,top,sheet);
				}
			}
			workbook.write(out);
			out.flush();
			out.close();
			
			}
			catch (Exception e){
				e.printStackTrace();
			}
	 }
	 
private void exportExcelAll(Pageable<DayReport> pager,String conditionString,
			 HSSFCellStyle style1,HSSFCellStyle style2,HSSFCellStyle style3,
			 HSSFRow row,HSSFRow top,HSSFSheet sheet) 
	 {
		//设置表头长度
			for(int i=0;i<8;i++){
				HSSFCell cell = top.createCell(i);
				cell.setCellStyle(style3);
			}
		//设置表头长度
			top.getSheet().addMergedRegion(new Region(0,(short)0,0,(short)7));
		//设置表头样式	
			HSSFCell celltop = top.createCell(0);
			top.setHeight((short) (200*4));
			celltop.setCellStyle(style3);
			//设置表头内容：
			celltop.setCellValue("治超站超限统计\r\n"+conditionString);
			

			String[] head = { "序号", "统计日期", "椒江一桥北", "椒江一桥南", "椒江二桥北74省道", "椒江二桥北75省道","椒江二桥南75省道","总计"};
			
			int i0=0,i1=0,i2=0,i3=0,i4=0,i5=0,i6=0,i7=0;
			
			for (int i = 0; i < head.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(head[i]);
				cell.setCellStyle(style1);
				if(i==0){ i0= head[i].length()*256+256*10;}
				if(i==1){ i1= head[i].length()*256+256*10;}
				if(i==2){ i2= head[i].length()*256+256*10;}
				if(i==3){ i3= head[i].length()*256+256*10;}
				if(i==4){ i4= head[i].length()*256+256*10;}
				if(i==5){ i5= head[i].length()*256+256*10;}
				if(i==6){ i6= head[i].length()*256+256*10;}
				if(i==7){ i7= head[i].length()*256+256*10;}
			}
			for(int i=0; i<pager.size();i++){
				  row = sheet.createRow(i+2);
//				   序号
				   HSSFCell cell0 =row.createCell(0);
				   cell0.setCellValue(1+i);
				   cell0.setCellStyle(style2);
				   if(String.valueOf(1+i).length()*256>=i0){
					   i0=String.valueOf(1+i).length()*256+256*8;
				   }
//				  统计时间
				   HSSFCell cell1 =row.createCell(1);
				   cell1.setCellStyle(style2);
				   if(pager.get(i).getTjDateStr()!=null){
					   cell1.setCellValue(pager.get(i).getTjDateStr());
					   if(pager.get(i).getTjDateStr().length()*256>=i1){
						   i1=pager.get(i).getTjDateStr().length()*256+256*10;
					   } 
				   }else{
					   cell1.setCellValue("");
				   }
////			   椒江一桥北
				   HSSFCell cell2 =row.createCell(2);
				   cell2.setCellStyle(style2);
				   if(pager.get(i).getDetectOne()!=null){
					   cell2.setCellValue(pager.get(i).getDetectOne());
				   }else{
					   cell2.setCellValue("");
				   }
////			   椒江一桥南
				   HSSFCell cell3 =row.createCell(3);
				   cell3.setCellStyle(style2);
				   if(pager.get(i).getDetectTwo()!=null){
					   cell3.setCellValue(pager.get(i).getDetectTwo());
				   }else{
					   cell3.setCellValue("");
				   }
////			      椒江二桥北74省道 
				   HSSFCell cell4 =row.createCell(4);
				   cell4.setCellStyle(style2);
				   if(pager.get(i).getDetectThree()!=null){
					   cell4.setCellValue(pager.get(i).getDetectThree());
				   }else{
					   cell4.setCellValue("");
				   }
////			      椒江二桥北75省道 
				   HSSFCell cell5 =row.createCell(5);
				   cell5.setCellStyle(style2);
				   if(pager.get(i).getDetectFour()!=null){
					   cell5.setCellValue(pager.get(i).getDetectFour());
				   }else{
					   cell5.setCellValue("");
				   }
////			      椒江二桥南75省道 
				   HSSFCell cell6 =row.createCell(6);
				   cell6.setCellStyle(style2);
				   if(pager.get(i).getDetectFive()!=null){
					   cell6.setCellValue(pager.get(i).getDetectFive());
				   }else{
					   cell6.setCellValue("");
				   }
//				      总计
				   HSSFCell cell7 =row.createCell(7);
				   cell7.setCellStyle(style2);
				   if(pager.get(i).getTotal()!=null){
					   cell7.setCellValue(pager.get(i).getTotal());
				   }else{
					   cell7.setCellValue("");
				   }
			}
			sheet.setColumnWidth(0,i0);
			sheet.setColumnWidth(1,i1);
			sheet.setColumnWidth(2,i2);
			sheet.setColumnWidth(3,i3);
			sheet.setColumnWidth(4,i4);
			sheet.setColumnWidth(5,i5);
			sheet.setColumnWidth(6,i6);
			sheet.setColumnWidth(7,i7);
	 }


private void exportExcelJgid133(Pageable<DayReport> pager,String conditionString,
		HSSFCellStyle style1,HSSFCellStyle style2,HSSFCellStyle style3,
		HSSFRow row,HSSFRow top,HSSFSheet sheet) {
	 
	//设置表头长度
	for(int i=0;i<5;i++){
		HSSFCell cell = top.createCell(i);
		cell.setCellStyle(style3);
	}
	//设置表头长度
	top.getSheet().addMergedRegion(new Region(0,(short)0,0,(short)4));
	//设置表头样式	
	HSSFCell celltop = top.createCell(0);
	top.setHeight((short) (200*4));
	celltop.setCellStyle(style3);
	//设置表头内容：
	celltop.setCellValue("治超站超限统计\r\n"+conditionString);

		String[] head = { "序号", "统计日期", "K110+150", "K109+800","总计"};
		
		int i0 = 0,i1=0,i2=0,i3=0,i4=0;
		
		for (int i = 0; i < head.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(head[i]);
			cell.setCellStyle(style1);
			if(i==0){ i0= head[i].length()*256+256*10;}
			if(i==1){ i1= head[i].length()*256+256*10;}
			if(i==2){ i2= head[i].length()*256+256*10;}
			if(i==3){ i3= head[i].length()*256+256*10;}
			if(i==4){ i4= head[i].length()*256+256*10;}
		}
		for(int i=0; i<pager.size();i++){
			  row = sheet.createRow(i+2);
//			     序号
			   HSSFCell cell0 =row.createCell(0);
			   cell0.setCellValue(1+i);
			   cell0.setCellStyle(style2);
			   if(String.valueOf(1+i).length()*256>=i0){
				   i0=String.valueOf(1+i).length()*256+256*8;
			   }
//			  统计时间
			   HSSFCell cell1 =row.createCell(1);
			   cell1.setCellStyle(style2);
			   if(pager.get(i).getTjDateStr()!=null){
				   cell1.setCellValue(pager.get(i).getTjDateStr());
				   if(pager.get(i).getTjDateStr().length()*256>=i1){
					   i1=pager.get(i).getTjDateStr().length()*256+256*10;
				   } 
			   }else{
				   cell1.setCellValue("");
			   }
////		   椒江一桥北
			   HSSFCell cell2 =row.createCell(2);
			   cell2.setCellStyle(style2);
			   if(pager.get(i).getDetectOne()!=null){
				   cell2.setCellValue(pager.get(i).getDetectOne());
			   }else{
				   cell2.setCellValue("");
			   }
////		   椒江一桥南
			   HSSFCell cell3 =row.createCell(3);
			   cell3.setCellStyle(style2);
			   if(pager.get(i).getDetectTwo()!=null){
				   cell3.setCellValue(pager.get(i).getDetectTwo());
			   }else{
				   cell3.setCellValue("");
			   }
////	 	     总计
			   HSSFCell cell4 =row.createCell(4);
			   cell4.setCellStyle(style2);
			   if(pager.get(i).getTotal()!=null){
				   cell4.setCellValue(pager.get(i).getTotal());
			   }else{
				   cell4.setCellValue("");
			   }
		}
		sheet.setColumnWidth(0,i0);
		sheet.setColumnWidth(1,i1);
		sheet.setColumnWidth(2,i2);
		sheet.setColumnWidth(3,i3);
		sheet.setColumnWidth(4,i4);

	}


private void exportExcelJgid134(Pageable<DayReport> pager,String conditionString,
		 HSSFCellStyle style1,HSSFCellStyle style2,HSSFCellStyle style3,
		 HSSFRow row,HSSFRow top,HSSFSheet sheet) 
{
	//设置表头长度
		for(int i=0;i<6;i++){
			HSSFCell cell = top.createCell(i);
			cell.setCellStyle(style3);
		}
	//设置表头长度
		top.getSheet().addMergedRegion(new Region(0,(short)0,0,(short)5));
	//设置表头样式	
		HSSFCell celltop = top.createCell(0);
		top.setHeight((short) (200*4));
		celltop.setCellStyle(style3);
	//设置表头内容：
		celltop.setCellValue("治超站超限统计\r\n"+conditionString);
		

		String[] head = { "序号", "统计日期", "椒江二桥北74省道", "椒江二桥北75省道","椒江二桥南75省道","总计"};
		
		int i0=0,i1=0,i2=0,i3=0,i4=0,i5=0;
		
		for (int i = 0; i < head.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(head[i]);
			cell.setCellStyle(style1);
			if(i==0){ i0= head[i].length()*256+256*10;}
			if(i==1){ i1= head[i].length()*256+256*10;}
			if(i==2){ i2= head[i].length()*256+256*10;}
			if(i==3){ i3= head[i].length()*256+256*10;}
			if(i==4){ i4= head[i].length()*256+256*10;}
			if(i==5){ i5= head[i].length()*256+256*10;}

		}
		for(int i=0; i<pager.size();i++){
			  row = sheet.createRow(i+2);
//			   序号
			   HSSFCell cell0 =row.createCell(0);
			   cell0.setCellValue(1+i);
			   cell0.setCellStyle(style2);
			   if(String.valueOf(1+i).length()*256>=i0){
				   i0=String.valueOf(1+i).length()*256+256*8;
			   }
//			       统计时间
			   HSSFCell cell1 =row.createCell(1);
			   cell1.setCellStyle(style2);
			   if(pager.get(i).getTjDateStr()!=null){
				   cell1.setCellValue(pager.get(i).getTjDateStr());
				   if(pager.get(i).getTjDateStr().length()*256>=i1){
					   i1=pager.get(i).getTjDateStr().length()*256+256*10;
				   } 
			   }else{
				   cell1.setCellValue("");
			   }
////			椒江二桥北74省道 
			   HSSFCell cell2 =row.createCell(2);
			   cell2.setCellStyle(style2);
			   if(pager.get(i).getDetectThree()!=null){
				   cell2.setCellValue(pager.get(i).getDetectThree());
			   }else{
				   cell2.setCellValue("");
			   }
////			椒江二桥北75省道 
			   HSSFCell cell3 =row.createCell(3);
			   cell3.setCellStyle(style2);
			   if(pager.get(i).getDetectFour()!=null){
				   cell3.setCellValue(pager.get(i).getDetectFour());
			   }else{
				   cell3.setCellValue("");
			   }
////			 椒江二桥南75省道 
			   HSSFCell cell4 =row.createCell(4);
			   cell4.setCellStyle(style2);
			   if(pager.get(i).getDetectFive()!=null){
				   cell4.setCellValue(pager.get(i).getDetectFive());
			   }else{
				   cell4.setCellValue("");
			   }
//				 总计
			   HSSFCell cell5 =row.createCell(5);
			   cell5.setCellStyle(style2);
			   if(pager.get(i).getTotal()!=null){
				   cell5.setCellValue(pager.get(i).getTotal());
			   }else{
				   cell5.setCellValue("");
			   }
		}
		sheet.setColumnWidth(0,i0);
		sheet.setColumnWidth(1,i1);
		sheet.setColumnWidth(2,i2);
		sheet.setColumnWidth(3,i3);
		sheet.setColumnWidth(4,i4);
		sheet.setColumnWidth(5,i5);
}
	 
}
