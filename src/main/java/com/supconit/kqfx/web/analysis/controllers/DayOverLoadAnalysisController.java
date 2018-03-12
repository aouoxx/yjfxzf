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

import com.supconit.honeycomb.business.authorization.entities.Role;
import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.honeycomb.business.authorization.services.RoleService;
import com.supconit.honeycomb.business.organization.services.DepartmentService;
import com.supconit.honeycomb.business.organization.services.PersonService;
import com.supconit.kqfx.web.analysis.entities.JgZcd;
import com.supconit.kqfx.web.analysis.entities.OverLoad;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.analysis.services.OverLoadService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 超限统计-每日超限统计
 * @author gs
 *
 */
@SuppressWarnings("deprecation")
@RequestMapping("/analysis/overload/dayoverload")
@Controller("analysis_dayoverLoad_controller")
public class DayOverLoadAnalysisController {
	
	private static final String MODULE_CODE = "DAY_OVERLOAD";
	
	@Autowired
	private OverLoadService overLoadService;
	
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
	
	@ModelAttribute("overload")
	private OverLoad getOverLoad(){
		OverLoad overLoad = new OverLoad();
		return overLoad;
	}
	
	private transient static final Logger logger = LoggerFactory.getLogger(DayOverLoadAnalysisController.class);
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model){
		this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(),
				"每日超限统计", request.getRemoteAddr());
		return "analysis/overload/dayoverload/list";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	 public Pagination<OverLoad> list(Pagination<OverLoad> pager,
			 @FormBean(value = "condition", modelCode = "overload") OverLoad condition) {
		try {
			
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"每日超限统计", request.getRemoteAddr());
			
			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
			condition=getHuiZhong(condition);
			overLoadService.findByPager(pager, condition);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			for(OverLoad overLoad : pager ){
				overLoad.setFlag(format.format(overLoad.getTjDate()));
				if(overLoad.getOver55()==null){overLoad.setOver55(0);}
				if(overLoad.getOver70()==null){overLoad.setOver70(0);}
				if(overLoad.getOver100()==null){overLoad.setOver100(0);}
				overLoad.setOverTotal(overLoad.getOver55()+overLoad.getOver70()+overLoad.getOver100());
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}
	
	/**
	 * 获取治超站数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "detectionStation", method = RequestMethod.POST)
	AjaxMessage getDetectionStation(){
		try {
			List<String> stationList = new ArrayList<String>() ;
			stationList=getDetectStations(1);
			return AjaxMessage.success(stationList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取对应的治超站序列
	 */
	public List<String> getDetectStations(int flag){
		HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME",dataDictionaryService);
		List<String> stationList = new ArrayList<String>() ;
		List<String> detectList = new ArrayList<String>() ;
		
		Boolean isAdmin = false;
		Long jgid=null;
//		根据人员来获取查询权限
		User user = (User) safetyManager.getAuthenticationInfo().getUser();
		List<Role> rolelist = this.roleService.findAssigned(user.getId());
		if(!CollectionUtils.isEmpty(rolelist)){
			for (Role role : rolelist) {
				if ("ROLE_ADMIN".equals(role.getCode())) 
					isAdmin = true;
			}
		}
		if(!isAdmin)
		{
			ExtPerson person = personService.getById(user.getPersonId());
			jgid = null!=person?person.getJgbh():null;
//			人员部分权限
			List<JgZcd> zcdList = jgZcdService.getByJgid(jgid);
			for(JgZcd jgZcd:zcdList){
				stationList.add(jgZcd.getDeteStation()+":"+stationMap.get(jgZcd.getDeteStation()));
				detectList.add(jgZcd.getDeteStation());
			}	
			
		}else{
//			超级管理员权限
			for(String key:stationMap.keySet() ){
				stationList.add(key+":"+stationMap.get(key));
				detectList.add(key);
			}
		}
		if(flag==1){
			return stationList;
		}else{
			return detectList;
		}
		
		
	}
	
	/**
	 * 设置默认情况下查询治超站汇总信息
	 */
	public OverLoad getHuiZhong(OverLoad condition){
		
		if(condition.getDetectStation()==null||condition.getDetectStation().equals("")||condition.getDetectStation()==""||condition.getDetectStation().equals("null")){
			 List<String> detectList = new ArrayList<String>() ;
			 detectList=getDetectStations(0);
			 String detectionString="";
			 for(String station: detectList){
				 detectionString=detectionString+station+",";
			 }
			 //如果是汇总查询则获取对应权限的全部的数据 
			 condition.setDetects(detectionString.substring(0, detectionString.length()-1).split(","));
			 condition.setDetectStation(null);
			 condition.setStation("total");
		}else{
			//如果选择了对应的治超站则仅获取对应的治超站数据
			condition.setStation(null);
			condition.setStation(condition.getDetectStation());
			condition.setDetectStation(null);
			condition.setDetects(condition.getStation().split(","));
		}
	
		return condition;
	}
	
	
	
	/**
	 * 获取柱状图数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getChartData", method = RequestMethod.POST)
	public AjaxMessage getChartData(@FormBean(value = "condition", modelCode = "overload") OverLoad condition){
		try {
			
			this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(),
					"查询每日超限图表信息", request.getRemoteAddr());
			
			//是否选择日期没有选择查看昨天数据信息
			List<OverLoad> overLoadList = new ArrayList<OverLoad>();
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			if(condition.getTjDate()==null){
				//时间已经默认在页面选择昨天的数据所以这里不会执行,写在这里方式后面逻辑修改,不在页面进行查询
				condition=getHuiZhong(condition);
				overLoadList =overLoadService.getByDetectionStation(condition);
			}else{
			//获取选择时间段内统计数据信息
				condition=getHuiZhong(condition);
				overLoadList =overLoadService.getByDetectionStation(condition);
			}
			
			//设置legend
			HashMap<String, String> overLoadMap = DictionaryUtil.dictionary("DAYOVERLOAD",dataDictionaryService);
			List<String> legend =new ArrayList<String>();
			Iterator<String> iterator  = overLoadMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				legend.add(overLoadMap.get(key));
			}
			legend.add("汇总");
			if(!CollectionUtils.isEmpty(overLoadList)){
//				横轴数据
				List<String> xAisData = new ArrayList<String>();
//				纵轴数据
				List<OverLoad> yAisData = new ArrayList<OverLoad>();
				for(OverLoad overLoad : overLoadList){
					String xdate = format.format(overLoad.getTjDate());
					if(!xAisData.contains(xdate)){
						xAisData.add(xdate);
					}
					overLoad.setFlag(format.format(overLoad.getTjDate()));
					if(overLoad.getOver55()==null){overLoad.setOver55(0);}
					if(overLoad.getOver70()==null){overLoad.setOver70(0);}
					if(overLoad.getOver100()==null){overLoad.setOver100(0);}
					overLoad.setOverTotal(overLoad.getOver55()+overLoad.getOver70()+overLoad.getOver100());
					yAisData.add(overLoad);
				}
				
				Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("xAis", xAisData);
				resultMap.put("legend", legend);
				resultMap.put("yAis", yAisData);
				resultMap.put("success", 200);
				return AjaxMessage.success(resultMap);
			}else{
				Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("success", 1002);
				resultMap.put("legend", legend);
				return AjaxMessage.success(resultMap);
			}
			
		} catch (Exception e) {
			logger.error("", e);
			return AjaxMessage.success("请求服务器错误");
		}
	}
	
	
	@RequestMapping(value = "exportAll", method = RequestMethod.GET)
	public void exportAll(HttpServletRequest request,
							HttpServletResponse response,
							String  total,
							@FormBean(value = "condition", modelCode = "overload") OverLoad condition){
		 logger.info("-------------------------导出全部excel列表---------------");
	     try {
	    	 
	    	 this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
						"每日超限导出全部记录", request.getRemoteAddr());
	    	 
			 Pagination<OverLoad> pager = new Pagination<OverLoad>();
			 pager.setPageNo(1);
			 pager.setPageSize(Integer.MAX_VALUE);
			 
			 condition=getHuiZhong(condition);
			overLoadService.findByPager(pager, condition);
				
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			for(OverLoad overLoad : pager ){
				overLoad.setFlag(format.format(overLoad.getTjDate()));
				if(overLoad.getOver55()==null){overLoad.setOver55(0);}
				if(overLoad.getOver70()==null){overLoad.setOver70(0);}
				if(overLoad.getOver100()==null){overLoad.setOver100(0);}
				overLoad.setOverTotal(overLoad.getOver55()+overLoad.getOver70()+overLoad.getOver100());
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
		    String time = formatter.format(date);
			String title = "每日超限统计记录_"+time+".xls";
			
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
	
	void editExcel(Pageable<OverLoad> pager,HttpServletResponse response,String title,OverLoad condition){
		OutputStream out=null;
		try{
		
		response.setHeader("Content-Disposition", "attachment; filename="
				+  new String(title.getBytes("GB2312"), "iso8859-1"));
	    response.setContentType("application/msexcel;charset=UTF-8");
	    out  =response.getOutputStream();
	    HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("过车数据查询"));
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
		for(int i=0;i<6;i++){
			HSSFCell cell = top.createCell(i);
			cell.setCellStyle(style3);
		}
		//设置表头长度
		top.getSheet().addMergedRegion(new Region(0,(short)0,0,(short)5));
		HSSFCell celltop = top.createCell(0);
		top.setHeight((short) (200*4));
		celltop.setCellStyle(style3);
		
		//获取导出条件
		String conditionString ="";
		//获取导出的时间
		if(condition.getTjDate()!=null){
			SimpleDateFormat format1  = new SimpleDateFormat("yyyy-MM-dd");
			String begin = format1.format(condition.getTjDate());
			if(condition.getUpdateTime()!=null){
				String end = format1.format(condition.getUpdateTime());
				end = format1.format(condition.getUpdateTime());
				conditionString="统计日期："+begin+" 至  "+end+"\r\n";
			}else{
				conditionString="统计日期："+begin+"开始至今为止\r\n";
			}  
		}
		//获取导出治超站
		HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME",dataDictionaryService);
		String detecString = "";
		String[] detects=condition.getDetects();
		for(int i=0;i<detects.length;i++){
			detecString= detecString+" "+stationMap.get(detects[i]);
		}
		conditionString=conditionString+"统计治超站："+detecString;
		
        //设置表头内容
		celltop.setCellValue("每日超限统计 \r\n"+conditionString);
		
		
		
		String[] head = { "序号", "统计时间", "车辆数", "大于55吨", "大于70吨", "大于100吨"};
		
		int i0 = 0,i1=0,i2=0,i3=0,i4=0,i5=0;
		
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
////			   序号
			   HSSFCell cell0 =row.createCell(0);
			   cell0.setCellValue(1+i);
			   cell0.setCellStyle(style2);
			   if(String.valueOf(1+i).length()*256>=i0){
				   i0=String.valueOf(1+i).length()*256+256*8;
			   }
////			  统计时间
			   HSSFCell cell1 =row.createCell(1);
			   cell1.setCellStyle(style2);
			   if(pager.get(i).getFlag()!=null){
				   cell1.setCellValue(pager.get(i).getFlag());
				   if(pager.get(i).getFlag().length()*256>=i1){
					   i1=pager.get(i).getFlag().length()*256+256*10;
				   } 
			   }else{
				   cell1.setCellValue("");
			   }
////		   车辆总数
			   HSSFCell cell2 =row.createCell(2);
			   cell2.setCellStyle(style2);
			   if(pager.get(i).getOverTotal()!=null){
				   cell2.setCellValue(pager.get(i).getOverTotal());
			   }else{
				   cell2.setCellValue("");
			   }
////		   大于55吨 
			   HSSFCell cell3 =row.createCell(3);
			   cell3.setCellStyle(style2);
			   if(pager.get(i).getOver55()!=null){
				   cell3.setCellValue(pager.get(i).getOver55());
			   }else{
				   cell3.setCellValue("");
			   }
////		      大于70吨 
			   HSSFCell cell4 =row.createCell(4);
			   cell4.setCellStyle(style2);
			   if(pager.get(i).getOver70()!=null){
				   cell4.setCellValue(pager.get(i).getOver70());
			   }else{
				   cell4.setCellValue("");
			   }
////		      大于100吨 
			   HSSFCell cell5 =row.createCell(5);
			   cell5.setCellStyle(style2);
			   if(pager.get(i).getOver100()!=null){
				   cell5.setCellValue(pager.get(i).getOver100());
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
		 workbook.write(out);
		 out.flush();
		 out.close();
		
	}catch(Exception e){
		
		e.printStackTrace();
	}
		
	}
	

}
