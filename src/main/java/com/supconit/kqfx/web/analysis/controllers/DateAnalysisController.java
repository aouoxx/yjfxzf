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
import com.supconit.kqfx.web.analysis.entities.TimeZone;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.analysis.services.TimeZoneService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;
/**
 * 按日期超限统计
 * @author gs
 *
 */
@SuppressWarnings("deprecation")
@RequestMapping("/analysis/overload/dateoverload")
@Controller("analysis_date_controller")
public class DateAnalysisController {

	private static final String MODULE_CODE = "DAY_ANALYSIS";
	
	@Autowired
	private TimeZoneService timeZoneService;
	
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
	
	private transient static final Logger logger = LoggerFactory.getLogger(DateAnalysisController.class);
	
	@ModelAttribute("timezone")
	private TimeZone getTimeZone(){
		TimeZone timezone = new TimeZone();
		return timezone;
	}
	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model){
		this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(),
				"按日期超限统计", request.getRemoteAddr());
		try {
			HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME",dataDictionaryService);
			List<String> stationList = new ArrayList<String>() ;
			for(String key:stationMap.keySet() ){
				stationList.add(stationMap.get(key));
			}
			User user = (User) safetyManager.getAuthenticationInfo().getUser();
			if(null!=user&&null!=user.getPerson()&&null!=user.getPersonId())
			{
				ExtPerson person = personService.getById(user.getPersonId());
				//根据JGID进行权限限制
				//若是超级管理员查询JGID = null
				//大桥的为 JGID = 133
				// 二桥为JGID=134
				model.put("jgid", person.getJgbh());
			}
			model.put("stationList", stationList);
			for(String key:stationMap.keySet() ){
				model.put("station"+key,stationMap.get(key));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return "analysis/overload/dateoverload/list";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	 public Pagination<TimeZone> list(Pagination<TimeZone> pager,
			 @FormBean(value = "condition", modelCode = "timezone") TimeZone condition) {
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"按日期超限统计", request.getRemoteAddr());
			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
			timeZoneService.findByDayPager(pager, condition);
			TimeZone zoneCnt =timeZoneService.getDateTimeCount(condition);
			for(TimeZone day:pager){
				if(day.getTimeString()!=null){
					if(day.getStationA()==null){day.setStationA(0);}else{day.setStationA(day.getStationA()/zoneCnt.getCntDays(day.getTimeString()));}
					if(day.getStationB()==null){day.setStationB(0);}else{day.setStationB(day.getStationB()/zoneCnt.getCntDays(day.getTimeString()));}
					if(day.getStationC()==null){day.setStationC(0);}else{day.setStationC(day.getStationC()/zoneCnt.getCntDays(day.getTimeString()));}
					if(day.getStationD()==null){day.setStationD(0);}else{day.setStationD(day.getStationD()/zoneCnt.getCntDays(day.getTimeString()));}
					if(day.getStationE()==null){day.setStationE(0);}else{day.setStationE(day.getStationE()/zoneCnt.getCntDays(day.getTimeString()));}
					if(condition.getJgid()==null){
						day.setTotal(day.getStationA()+day.getStationB()+day.getStationC()+
								day.getStationD()+day.getStationE());
					}else{
						if(condition.getJgid()==133){
							day.setTotal(day.getStationA()+day.getStationB());
						}else if(condition.getJgid()==134){
							day.setTotal(day.getStationC()+day.getStationD()+day.getStationE());
						}else{
							day.setTotal(day.getStationA()+day.getStationB()+day.getStationC()+
									day.getStationD()+day.getStationE());
						}
					}
				}
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
			List<String> stationList = getStations();
			return AjaxMessage.success(stationList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * 根据权限获取查询治超站信息
	 */
	public List<String> getStations(){
	HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME",dataDictionaryService);
	List<String> stationList = new ArrayList<String>() ;
	
	Boolean isAdmin = false;
	Long jgid=null;
//	根据人员来获取查询权限
	User user = (User) safetyManager.getAuthenticationInfo().getUser();
	List<Role> rolelist = this.roleService.findAssigned(user.getId());
	if(!CollectionUtils.isEmpty(rolelist)){
		for (Role role : rolelist) {
			if ("ROLE_ADMIN".equals(role.getCode())) isAdmin = true;
		}
	}
	if(!isAdmin)
	{
		ExtPerson person = personService.getById(user.getPersonId());
		jgid = null!=person?person.getJgbh():null;
//		人员部分权限
//		根据权限设置查询的治超站范围
		List<JgZcd> zcdList = jgZcdService.getByJgid(jgid);
		for(JgZcd jgZcd:zcdList){
			stationList.add(jgZcd.getDeteStation()+":"+stationMap.get(jgZcd.getDeteStation()));
		}
	}else{
//		超级管理员权限
//		所有的治超站权限
		for(String key:stationMap.keySet() ){
			stationList.add(key+":"+stationMap.get(key));
		}
		
	}
	return stationList;
	}
	/**
	 * 获取柱状图数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getChartData", method = RequestMethod.POST)
	public AjaxMessage getChartData(@FormBean(value = "condition", modelCode = "timezone") TimeZone condition){
		try {
			
			this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(),
					"查询按日期超限图表信息", request.getRemoteAddr());
			//是否选择日期没有选择查看昨天数据信息
			List<TimeZone> dateoverLoadList = new ArrayList<TimeZone>();
			TimeZone zoneCnt = new TimeZone();
			
			if(condition.getTjDate()==null){
				dateoverLoadList =timeZoneService.getByDateTime(condition);
				zoneCnt =timeZoneService.getDateTimeCount(condition);
			}else{
			//获取选择时间段内统计数据信息
				dateoverLoadList =timeZoneService.getByDateTime(condition);
				zoneCnt =timeZoneService.getDateTimeCount(condition);
			}
			if(!CollectionUtils.isEmpty(dateoverLoadList)){
//				横轴数据(1~31日期)
				List<String> xAisData = new ArrayList<String>();
				for(int i=1;i<=31;i++){
					xAisData.add(String.valueOf(i));
				}
				
//				设置legend(治超站)
				HashMap<String, String> detectionLengend = DictionaryUtil.dictionary("DETECTIONSTATION",dataDictionaryService);
				List<String> legend = getStations();
				legend.add("汇总");
				
				
//				纵轴数据
				List<TimeZone> yAisData = new ArrayList<TimeZone>();
				for(TimeZone timeZone : dateoverLoadList){
					//设置治超站名称
					timeZone.setStation(detectionLengend.get(timeZone.getDetectStation()));
					//设置日期的值
					if(timeZone.getAday()==null){timeZone.setAday(0);}else{timeZone.setAday(timeZone.getAday()/zoneCnt.getAday());}
					if(timeZone.getBday()==null){timeZone.setBday(0);}else{timeZone.setBday(timeZone.getBday()/zoneCnt.getBday());}
					if(timeZone.getCday()==null){timeZone.setCday(0);}else{timeZone.setCday(timeZone.getCday()/zoneCnt.getCday());}
					if(timeZone.getDday()==null){timeZone.setDday(0);}else{timeZone.setDday(timeZone.getDday()/zoneCnt.getDday());}
					if(timeZone.getEday()==null){timeZone.setEday(0);}else{timeZone.setEday(timeZone.getEday()/zoneCnt.getEday());}
					if(timeZone.getFday()==null){timeZone.setFday(0);}else{timeZone.setFday(timeZone.getFday()/zoneCnt.getFday());}
					if(timeZone.getGday()==null){timeZone.setGday(0);}else{timeZone.setGday(timeZone.getGday()/zoneCnt.getGday());}
					if(timeZone.getHday()==null){timeZone.setHday(0);}else{timeZone.setHday(timeZone.getHday()/zoneCnt.getHday());}
					if(timeZone.getIday()==null){timeZone.setIday(0);}else{timeZone.setIday(timeZone.getIday()/zoneCnt.getIday());}
					if(timeZone.getJday()==null){timeZone.setJday(0);}else{timeZone.setJday(timeZone.getJday()/zoneCnt.getJday());}
					if(timeZone.getKday()==null){timeZone.setKday(0);}else{timeZone.setKday(timeZone.getKday()/zoneCnt.getKday());}
					if(timeZone.getLday()==null){timeZone.setLday(0);}else{timeZone.setLday(timeZone.getLday()/zoneCnt.getLday());}
					if(timeZone.getMday()==null){timeZone.setMday(0);}else{timeZone.setMday(timeZone.getMday()/zoneCnt.getMday());}
					if(timeZone.getNday()==null){timeZone.setNday(0);}else{timeZone.setNday(timeZone.getNday()/zoneCnt.getNday());}
					if(timeZone.getOday()==null){timeZone.setOday(0);}else{timeZone.setOday(timeZone.getOday()/zoneCnt.getOday());}
					if(timeZone.getPday()==null){timeZone.setPday(0);}else{timeZone.setPday(timeZone.getPday()/zoneCnt.getPday());}
					if(timeZone.getQday()==null){timeZone.setQday(0);}else{timeZone.setQday(timeZone.getQday()/zoneCnt.getQday());}
					if(timeZone.getRday()==null){timeZone.setRday(0);}else{timeZone.setRday(timeZone.getRday()/zoneCnt.getRday());}
					if(timeZone.getSday()==null){timeZone.setSday(0);}else{timeZone.setSday(timeZone.getSday()/zoneCnt.getSday());}
					if(timeZone.getTday()==null){timeZone.setTday(0);}else{timeZone.setTday(timeZone.getTday()/zoneCnt.getTday());}
					if(timeZone.getUday()==null){timeZone.setUday(0);}else{timeZone.setUday(timeZone.getUday()/zoneCnt.getUday());}
					if(timeZone.getVday()==null){timeZone.setVday(0);}else{timeZone.setVday(timeZone.getVday()/zoneCnt.getVday());}
					if(timeZone.getWday()==null){timeZone.setWday(0);}else{timeZone.setWday(timeZone.getWday()/zoneCnt.getWday());}
					if(timeZone.getXday()==null){timeZone.setXday(0);}else{timeZone.setXday(timeZone.getXday()/zoneCnt.getXday());}
					if(timeZone.getYday()==null){timeZone.setYday(0);}else{timeZone.setYday(timeZone.getYday()/zoneCnt.getYday());}
					if(timeZone.getZday()==null){timeZone.setZday(0);}else{timeZone.setZday(timeZone.getZday()/zoneCnt.getZday());}
					if(timeZone.getAaday()==null){timeZone.setAaday(0);}else{timeZone.setAaday(timeZone.getAaday()/zoneCnt.getAaday());}
					if(timeZone.getAbday()==null){timeZone.setAbday(0);}else{timeZone.setAbday(timeZone.getAbday()/zoneCnt.getAbday());}
					if(timeZone.getAcday()==null){timeZone.setAcday(0);}else{timeZone.setAcday(timeZone.getAcday()/zoneCnt.getAcday());}
					if(timeZone.getAeday()==null){timeZone.setAeday(0);}else{timeZone.setAeday(timeZone.getAeday()/zoneCnt.getAeday());}
					if(timeZone.getAfday()==null){timeZone.setAfday(0);}else{timeZone.setAfday(timeZone.getAfday()/zoneCnt.getAfday());}
					
					timeZone.setTotal(timeZone.getAday()+timeZone.getBday()+timeZone.getCday()+timeZone.getDday()+timeZone.getEday()+timeZone.getFday()+
							timeZone.getHday()+timeZone.getIday()+timeZone.getJday()+timeZone.getKday()+timeZone.getLday()+timeZone.getMday()+timeZone.getNday()+
							timeZone.getOday()+timeZone.getPday()+timeZone.getQday()+timeZone.getRday()+timeZone.getSday()+timeZone.getTday()+timeZone.getUday()+
							timeZone.getVday()+timeZone.getWday()+timeZone.getXday()+timeZone.getYday()+timeZone.getZday()+
							timeZone.getAaday()+timeZone.getAbday()+timeZone.getAcday()+timeZone.getAeday()+timeZone.getAfday()
						);
					yAisData.add(timeZone);
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
							@FormBean(value = "condition", modelCode = "timezone") TimeZone condition){
		 logger.info("-------------------------导出全部excel列表---------------");
	     try {
	    	 this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
						"按日期超限导出全部记录", request.getRemoteAddr());
	    	 
			 Pagination<TimeZone> pager = new Pagination<TimeZone>();
			 pager.setPageNo(1);
			 pager.setPageSize(Integer.MAX_VALUE);
			 
			 timeZoneService.findByDayPager(pager, condition);
			 TimeZone zoneCnt =timeZoneService.getDateTimeCount(condition);
			 for(TimeZone day:pager){
				if(day.getTimeString()!=null){
					if(day.getStationA()==null){day.setStationA(0);}else{day.setStationA(day.getStationA()/zoneCnt.getCntDays(day.getTimeString()));}
					if(day.getStationB()==null){day.setStationB(0);}else{day.setStationB(day.getStationB()/zoneCnt.getCntDays(day.getTimeString()));}
					if(day.getStationC()==null){day.setStationC(0);}else{day.setStationC(day.getStationC()/zoneCnt.getCntDays(day.getTimeString()));}
					if(day.getStationD()==null){day.setStationD(0);}else{day.setStationD(day.getStationD()/zoneCnt.getCntDays(day.getTimeString()));}
					if(day.getStationE()==null){day.setStationE(0);}else{day.setStationE(day.getStationE()/zoneCnt.getCntDays(day.getTimeString()));}
					day.setTotal(day.getStationA()+day.getStationB()+day.getStationC()+
							day.getStationD()+day.getStationE()
							);
				}
			 }
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
		    String time = formatter.format(date);
			String title = "按日期超限统计记录_"+time+".xls";
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
	 
	 
	 void editExcel(Pageable<TimeZone> pager,HttpServletResponse response,String title,TimeZone condition){
			OutputStream out=null;
			try{
			response.setHeader("Content-Disposition", "attachment; filename="
					+  new String(title.getBytes("GB2312"), "iso8859-1"));
		    response.setContentType("application/msexcel;charset=UTF-8");
		    out  =response.getOutputStream();
		    HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("按日期超限统计记录"));
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
			if(condition.getTjDate()!=null){
				SimpleDateFormat format1  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			if(condition.getDetectStation()==null||condition.getDetectStation().equals("")||condition.getDetectStation()==""){
				if(condition.getJgid()==null){
//					String[] detects=new String[]{"1","2","3","4","5"};
					detecString=detecString+"全部治超站";
//					for(int i=0;i<detects.length;i++){
//						detecString= detecString+" "+stationMap.get(detects[i]);
//					}
				}else{
					if(condition.getJgid()==133){
						detecString= detecString+" "+stationMap.get("1");
						detecString= detecString+" "+stationMap.get("2");
					}else if (condition.getJgid()==134){
						detecString= detecString+" "+stationMap.get("3");
						detecString= detecString+" "+stationMap.get("4");
						detecString= detecString+" "+stationMap.get("5");
					}else{
						detecString=detecString+"全部治超站";
//						String[] detects=new String[]{"1","2","3","4","5"};
//						for(int i=0;i<detects.length;i++){
//							detecString= detecString+" "+stationMap.get(detects[i]);
//						}
					}
				}
			}
			else{
				detecString=stationMap.get(condition.getDetectStation());
			}
			conditionString=conditionString+"统计治超站："+detecString;
			
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
	 
private void exportExcelAll(Pageable<TimeZone> pager,String conditionString,
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
			celltop.setCellValue("按日期超限统计\r\n"+conditionString);
			

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
				   if(pager.get(i).getTimeString()!=null){
					   cell1.setCellValue(pager.get(i).getTimeString());
					   if(pager.get(i).getTimeString().length()*256>=i1){
						   i1=pager.get(i).getTimeString().length()*256+256*10;
					   } 
				   }else{
					   cell1.setCellValue("");
				   }
////			   椒江一桥北
				   HSSFCell cell2 =row.createCell(2);
				   cell2.setCellStyle(style2);
				   if(pager.get(i).getStationA()!=null){
					   cell2.setCellValue(pager.get(i).getStationA());
				   }else{
					   cell2.setCellValue("");
				   }
////			   椒江一桥南
				   HSSFCell cell3 =row.createCell(3);
				   cell3.setCellStyle(style2);
				   if(pager.get(i).getStationB()!=null){
					   cell3.setCellValue(pager.get(i).getStationB());
				   }else{
					   cell3.setCellValue("");
				   }
////			      椒江二桥北74省道 
				   HSSFCell cell4 =row.createCell(4);
				   cell4.setCellStyle(style2);
				   if(pager.get(i).getStationC()!=null){
					   cell4.setCellValue(pager.get(i).getStationC());
				   }else{
					   cell4.setCellValue("");
				   }
////			      椒江二桥北75省道 
				   HSSFCell cell5 =row.createCell(5);
				   cell5.setCellStyle(style2);
				   if(pager.get(i).getStationD()!=null){
					   cell5.setCellValue(pager.get(i).getStationD());
				   }else{
					   cell5.setCellValue("");
				   }
////			      椒江二桥南75省道 
				   HSSFCell cell6 =row.createCell(6);
				   cell6.setCellStyle(style2);
				   if(pager.get(i).getStationE()!=null){
					   cell6.setCellValue(pager.get(i).getStationE());
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


private void exportExcelJgid133(Pageable<TimeZone> pager,String conditionString,
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
	celltop.setCellValue("按日期超限统计\r\n"+conditionString);

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
//			   序号
			   HSSFCell cell0 =row.createCell(0);
			   cell0.setCellValue(1+i);
			   cell0.setCellStyle(style2);
			   if(String.valueOf(1+i).length()*256>=i0){
				   i0=String.valueOf(1+i).length()*256+256*8;
			   }
//			  统计时间
			   HSSFCell cell1 =row.createCell(1);
			   cell1.setCellStyle(style2);
			   if(pager.get(i).getTimeString()!=null){
				   cell1.setCellValue(pager.get(i).getTimeString());
				   if(pager.get(i).getTimeString().length()*256>=i1){
					   i1=pager.get(i).getTimeString().length()*256+256*10;
				   } 
			   }else{
				   cell1.setCellValue("");
			   }
////		   椒江一桥北
			   HSSFCell cell2 =row.createCell(2);
			   cell2.setCellStyle(style2);
			   if(pager.get(i).getStationA()!=null){
				   cell2.setCellValue(pager.get(i).getStationA());
			   }else{
				   cell2.setCellValue("");
			   }
////		   椒江一桥南
			   HSSFCell cell3 =row.createCell(3);
			   cell3.setCellStyle(style2);
			   if(pager.get(i).getStationB()!=null){
				   cell3.setCellValue(pager.get(i).getStationB());
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


private void exportExcelJgid134(Pageable<TimeZone> pager,String conditionString,
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
		celltop.setCellValue("按日期超限统计\r\n"+conditionString);
		

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
//			  统计时间
			   HSSFCell cell1 =row.createCell(1);
			   cell1.setCellStyle(style2);
			   if(pager.get(i).getTimeString()!=null){
				   cell1.setCellValue(pager.get(i).getTimeString());
				   if(pager.get(i).getTimeString().length()*256>=i1){
					   i1=pager.get(i).getTimeString().length()*256+256*10;
				   } 
			   }else{
				   cell1.setCellValue("");
			   }
////		      椒江二桥北74省道 
			   HSSFCell cell2 =row.createCell(2);
			   cell2.setCellStyle(style2);
			   if(pager.get(i).getStationC()!=null){
				   cell2.setCellValue(pager.get(i).getStationC());
			   }else{
				   cell2.setCellValue("");
			   }
////		      椒江二桥北75省道 
			   HSSFCell cell3 =row.createCell(3);
			   cell3.setCellStyle(style2);
			   if(pager.get(i).getStationD()!=null){
				   cell3.setCellValue(pager.get(i).getStationD());
			   }else{
				   cell3.setCellValue("");
			   }
////		      椒江二桥南75省道 
			   HSSFCell cell4 =row.createCell(4);
			   cell4.setCellStyle(style2);
			   if(pager.get(i).getStationE()!=null){
				   cell4.setCellValue(pager.get(i).getStationE());
			   }else{
				   cell4.setCellValue("");
			   }
//			      总计
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
