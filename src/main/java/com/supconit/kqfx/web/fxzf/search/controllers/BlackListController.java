package com.supconit.kqfx.web.fxzf.search.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.fxzf.search.entities.IllegalTrail;
import com.supconit.kqfx.web.fxzf.search.services.FxzfSearchService;
import com.supconit.kqfx.web.fxzf.search.services.IllegalTrailService;
import com.supconit.kqfx.web.fxzf.warn.entities.Config;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.list.entities.BlackList;
import com.supconit.kqfx.web.list.services.BlackListService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.SysConstants;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 黑名单管理
 * @author gs
 *
 */
@SuppressWarnings("deprecation")
@RequestMapping("/fxzf/blackList")
@Controller("taizhou_offsite_enforcement_searchBlackList_controller")
public class BlackListController {
	private static final String MODULE_CODE = "BLACK_LIST";
	
	@Autowired
	private BlackListService blackListService;
	
	@Autowired 
	private IllegalTrailService illegalTrailService;
	
	@Autowired
	private DataDictionaryService dataDictionaryService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private FxzfSearchService fxzfSearchService;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private WriteRedisService writeRedisServices;
	
	
	private transient static final Logger logger = LoggerFactory
			.getLogger(FxzfSearchController.class);
	
	/**
	 * 创建BlackList实体类
	 * @return
	 */
	@ModelAttribute("blackList")
	private BlackList getBlackList(){
		BlackList blackList = new BlackList();
		return blackList;
	}
	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String display(ModelMap map,
			@ModelAttribute("blackList") BlackList blackListInfo) {
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"黑名单管理列表", request.getRemoteAddr());
		return "/fxzf/blackList/list";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	 public Pagination<BlackList> list(Pagination<BlackList> pager,
			 @FormBean(value = "condition", modelCode = "backList") BlackList condition) {
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"黑名单列表", request.getRemoteAddr());
			
			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
			HashMap<String, String> plateColorMap = DictionaryUtil.dictionary("PLATECOLOR",dataDictionaryService);
			if(condition.getPlateColor()!=null){
				condition.setPlateColor(plateColorMap.get(condition.getPlateColor()));
			}
			blackListService.findByPager(pager, condition);
			//如果下一页的删除完后，返回上一页的内容
			if(pager.getPageSize()==pager.getTotal()){
				Pagination<BlackList> page = new Pagination<BlackList>();
				page.setPageSize(pager.getPageSize());
				page.setPageNo(pager.getPageNo()-1);
				if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
					return pager;
				blackListService.findByPager(page, condition);
				return page;
			}
			return pager;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"获取非现数据", request.getRemoteAddr());
		return pager;
		
	}
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addBlackList(ModelMap map,
			@ModelAttribute("blackList") BlackList blackListInfo){
		return "/fxzf/blackList/add";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	 public AjaxMessage add(Pagination<BlackList> pager,
			 @FormBean(value = "condition", modelCode = "backList") BlackList condition) {
		logger.info("--------------------加入黑名单--------------------");
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.insert.getCode(),
					"黑名单列表", request.getRemoteAddr());
//			获取超限次数
			Fxzf fxzf = new Fxzf();
			fxzf.setLicense(condition.getLicense());
			fxzf.setLicenseColor(condition.getPlateColor());
			HashMap<String, String> plateMap = DictionaryUtil.dictionary("PLATECOLOR",dataDictionaryService);
			condition.setPlateColor(plateMap.get(condition.getPlateColor()));
//			查看黑名单中是否已经含有对应的数据
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int count = blackListService.findByPlate(condition);
			if(count==0){
				Date date = new Date();
				format.format(date);
				condition.setId(IDGenerator.idGenerator());
				condition.setUpdateTime(date);
				condition.setOperateTime(date);
//				添加设置删除标志位=0, 删除设置标志位=1
				condition.setDeleted(0);
//				手动添加设置标志位=1
				condition.setAddByOperatorFlag(1);
				
//				到违法过车轨迹中查找有无该车信息
				IllegalTrail illegalTrail = illegalTrailService.findByLicenseAndColor(condition.getLicense(), condition.getPlateColor());
				if(illegalTrail!=null){
					//有该过车记录 获取违法次数
					condition.setOverloadTimes(Long.valueOf(illegalTrail.getOverLoadTimes()));
				}else{
					//无过车记录设置违法次数为0
					condition.setOverloadTimes((long) 0);
				}
				//添加到数据库
				blackListService.insert(condition);
				//添加到redis内存序列中 , 黑名单flag=0;
				writeRedisServices.AddListToRedis(condition.getId(), condition.getLicense(), condition.getPlateColor(), 0);
				
				return AjaxMessage.success("200");
			}else{
				return AjaxMessage.success("1002");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxMessage.error("黑名单添加失败");
		}
	}
	
	
	
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String editBlackList(ModelMap map,
			@ModelAttribute("blackList") BlackList blackListInfo){
		 String BLACK_LIST_OVERLOADS ="BLACK_LIST_OVERLOADS";
		 try{
			 Config config = configService.getByCode(BLACK_LIST_OVERLOADS);
			 if(config!=null){
				 map.put("thredhold", config.getValue());
			 }else{
				 map.put("thredhold", "暂未设置");
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		return "/fxzf/blackList/edit";
	}
	
	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	 public AjaxMessage edit(Pagination<BlackList> pager,
			 @FormBean(value = "condition", modelCode = "backList") BlackList condition) {
		logger.info("--------------------刷新黑名单--------------------");
		try {
			logger.info(String.valueOf(condition.getOverloadTimes()));
			
			this.systemLogService.log(MODULE_CODE, OperateType.update.getCode(),
					"黑名单阈值", request.getRemoteAddr());
			
			//从过车轨迹中查出所有大于黑名单法制的违章车的记录
			List<IllegalTrail> datas = illegalTrailService.findByOverLoadTimes(condition.getOverloadTimes());
			//从黑名单中删除自动添加的记录
			blackListService.deleteAllNoHandler();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			format.format(date);
			for(IllegalTrail illegalTrail:datas){
				//查看数据库是否有该记录
				BlackList obj = new BlackList();
				obj.setLicense(illegalTrail.getLicense());
				obj.setPlateColor(illegalTrail.getPlateColor());
				obj.setOverloadTimes(Long.valueOf(illegalTrail.getOverLoadTimes()));
				int flag = blackListService.findByPlate(obj);
				//有该记录更新overLoadTimes
				//否则插入该记录
				if(flag>0){
					blackListService.updateHandlerOverTimes(obj);
				}else{
					obj.setId(IDGenerator.idGenerator());
					obj.setUpdateTime(date);
					obj.setOperateTime(date);
					//添加设置删除标志位=0, 删除设置标志位=1
					obj.setDeleted(0);
					//自动添加设置标志位=0
					obj.setAddByOperatorFlag(0);
					blackListService.insert(obj);
				}
			}
			
			//重新填写redis中的黑名单信息
			writeRedisServices.RedisInitList(0);	
				
				
			//检查T_CONFIG中是否含有该值
			String code = SysConstants.BLACK_LIST_OVERLOADS;
			Config config = configService.getByCode(code);
			if(config!=null){
			 //更新告警配置数据库
			config.setValue(Double.valueOf(String.valueOf(condition.getOverloadTimes())));
			configService.update(config);
				}else{
					//插入告警配置数据库
					Config conditionConfig = new Config();
					conditionConfig.setName("加入黑名单超限次数");
					conditionConfig.setCode("code");
					conditionConfig.setId(IDGenerator.idGenerator());
					configService.insert(config);
				}
			return AjaxMessage.success("黑名单添加成功");
		} catch (Exception e) {
			return AjaxMessage.error("黑名单添加失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "deleteById", method = RequestMethod.POST)
	 public AjaxMessage deleteById(String id) {
		logger.info("--------------------删除黑名单--------------------");
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.delete.getCode(),
					"黑名单列表", request.getRemoteAddr());
			//获取要删除的黑名单信息
			BlackList condition=blackListService.getById(id);
			//从数据库中删除黑名单，将标志位delete设为1
			blackListService.deleteById(id);
			//删除Redis中的数据,黑名为flag=0;
			writeRedisServices.DeleteListToRedis(id, condition.getLicense(), condition.getPlateColor(), 0);
			return AjaxMessage.success("黑名单删除成功");
		} catch (Exception e) {
			return AjaxMessage.error("黑名单删除失败");
		}
	}
	
	
	@RequestMapping(value = "record", method = RequestMethod.GET)
	public String trailRecod(ModelMap map,
			@FormBean(value = "condition", modelCode = "blackList") BlackList condition,HttpServletRequest request) {
		logger.info("----------------------非现数据查询列表---------------");
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
				"查看黑名单车辆过车轨迹信息", request.getRemoteAddr());
		try {
			String license = java.net.URLDecoder.decode(condition.getLicense(), "utf-8");
			String plateColor = java.net.URLDecoder.decode(condition.getPlateColor(), "utf-8");
			Fxzf fxzf = new Fxzf();
			fxzf.setLicense(license);
			fxzf.setLicenseColor(plateColor);
			
//			获取正常童车数目
				fxzf.setOverLoadStatus(0);
				int normal = fxzfSearchService.getOverLoadStatusCount( fxzf);
//			获取各个超限状态的数目
				fxzf.setOverLoadStatus(1);
				int qingwei = fxzfSearchService.getOverLoadStatusCount(fxzf);
				fxzf.setOverLoadStatus(2);
				int yiban = fxzfSearchService.getOverLoadStatusCount(fxzf);
				fxzf.setOverLoadStatus(3);
				int jiaozhong = fxzfSearchService.getOverLoadStatusCount(fxzf);
				fxzf.setOverLoadStatus(4);
				int yanzhong = fxzfSearchService.getOverLoadStatusCount(fxzf);
				fxzf.setOverLoadStatus(5);
				int tebieyanzhong = fxzfSearchService.getOverLoadStatusCount(fxzf);
				
				int total = qingwei + yiban + jiaozhong + yanzhong + tebieyanzhong;
				
				map.put("normal", normal);
				map.put("qingwei", qingwei);
				map.put("yiban", yiban);
				map.put("jiaozhong", jiaozhong);
				map.put("yanzhong", yanzhong);
				map.put("tebieyanzhong", tebieyanzhong);
				map.put("total", total);
				map.put("license", license);
				map.put("color", plateColor);
				String imageServer = (String) request.getSession().getAttribute("imageServer");
				map.put("imageServerAddr", imageServer); 
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "/fxzf/blackList/record";
	}
	

	/**
	 * 导出一页
	 * @param request
	 * @param response
	 * @param pageNo
	 */
	@RequestMapping(value = "exportPage", method = RequestMethod.GET)
	public void exportExcel(HttpServletRequest request,
							HttpServletResponse response,
							String  pageNo,@FormBean(value = "condition", modelCode = "blackList") BlackList condition){
	     logger.info("-------------------------导出本页excel列表---------------");
	     this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
					"导出黑名单列表当前页", request.getRemoteAddr());
		 try {
			 Pagination<BlackList> pager = new Pagination<BlackList>();
			 pager.setPageNo(Integer.valueOf(pageNo));
			 pager.setPageSize(20);
			 if(condition.getLicense()!=null){
				 condition.setLicense(java.net.URLDecoder.decode(condition.getLicense(), "utf-8"));
			 }
			 HashMap<String, String> plateColorMap = DictionaryUtil.dictionary("PLATECOLOR",dataDictionaryService);
				if(condition.getPlateColor()!=null){
					condition.setPlateColor(plateColorMap.get(condition.getPlateColor()));
				}
			 Pageable<BlackList> page=(Pagination<BlackList>) blackListService.findByPager(pager, condition);
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			 Date date = new Date();
		     String time = formatter.format(date);
			 String title = "黑名单记录_"+time+".xls";
			 editExcel(page,response,title);
		} catch (Exception e) {
			logger.error("---------------------导出excel列表失败--------------------");
			e.printStackTrace();
		}
		
	}
	
	
	@RequestMapping(value = "exportAll", method = RequestMethod.GET)
	public void exportAll(HttpServletRequest request,
							HttpServletResponse response,
							@FormBean(value = "condition", modelCode = "blackList") BlackList condition){
	     logger.info("-------------------------导出本页excel列表---------------");
	     try {
	    	 this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
						"导出全部黑名单列表", request.getRemoteAddr());
	    	 Pagination<BlackList> pager = new Pagination<BlackList>();
			 pager.setPageNo(1);
			 pager.setPageSize(Integer.MAX_VALUE);
			 if(condition.getLicense()!=null){
				 condition.setLicense(java.net.URLDecoder.decode(condition.getLicense(), "utf-8"));
			 }
			 HashMap<String, String> plateColorMap = DictionaryUtil.dictionary("PLATECOLOR",dataDictionaryService);
				if(condition.getPlateColor()!=null){
					condition.setPlateColor(plateColorMap.get(condition.getPlateColor()));
				}
			 Pageable<BlackList> page=(Pagination<BlackList>) blackListService.findByPager(pager, condition);
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			 Date date = new Date();
		     String time = formatter.format(date);
			 String title = "黑名单记录_"+time+".xls";
			 editExcel(page,response,title);
		} catch (Exception e) {
			logger.error("---------------导出全部excel列表出错--------------");
			e.printStackTrace();
		}
		 
		 
	}
	
	void editExcel(Pageable<BlackList> pager,HttpServletResponse response,String title){
		 OutputStream out=null;
		 try{
	   response.setHeader("Content-Disposition", "attachment; filename="
				+  new String(title.getBytes("GB2312"), "iso8859-1"));
	   response.setContentType("application/msexcel;charset=UTF-8");
	    out  =response.getOutputStream();
	    HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("黑名单记录"));
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
		
		for(int i=0;i<5;i++){
			HSSFCell cell = top.createCell(i);
			cell.setCellStyle(style3);
		}
		top.getSheet().addMergedRegion(new Region(0,(short)0,0,(short)4));
		HSSFCell celltop = top.createCell(0);
		celltop.setCellValue("黑名单信息");
		celltop.setCellStyle(style3);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String[] head = { "序号", "车牌号", "车牌颜色", "历史超限次数", "加入黑名单时间"};
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
//			  车牌号
			   HSSFCell cell1 =row.createCell(1);
			   cell1.setCellStyle(style2);
			   if(pager.get(i).getLicense()!=null){
				   cell1.setCellValue(pager.get(i).getLicense());
				   if(pager.get(i).getLicense().length()*256>=i1){
					   i1=pager.get(i).getLicense().length()*256+256*10;
				   } 
			   }else{
				   cell1.setCellValue("");
			   }
//			   车牌颜色
			   HSSFCell cell2 =row.createCell(2);
			   cell2.setCellStyle(style2);
			   if(pager.get(i).getPlateColor()!=null){
				   cell2.setCellValue(pager.get(i).getPlateColor());
				   if(pager.get(i).getPlateColor().length()*256>=i2){
					   i2=pager.get(i).getPlateColor().length()*256+256*8;
				   } 
			   }else{
				   cell2.setCellValue("");
			   }
			   
////		   历史草在次数
			   HSSFCell cell3 =row.createCell(3);
			   cell3.setCellStyle(style2);
			   if(pager.get(i).getOverloadTimes()!=null){
				   cell3.setCellValue(pager.get(i).getOverloadTimes());
			   }else{
				   cell3.setCellValue("");
			   }
			   
////			   过车时间
			   HSSFCell cell4 =row.createCell(4);
			   cell4.setCellStyle(style2);
			   if(pager.get(i).getUpdateTime()!=null){
				   cell4.setCellValue(format.format(pager.get(i).getUpdateTime()));
				   if(format.format(pager.get(i).getUpdateTime()).length()*256>=i2){
					   i4=format.format(pager.get(i).getUpdateTime()).length()*256+256*10;
				   } 
			   }else{
				   cell4.setCellValue("");
			   }

		  }
		
		 sheet.setColumnWidth(0,i0);
		 sheet.setColumnWidth(1,i1);
		 sheet.setColumnWidth(2,i2);
		 sheet.setColumnWidth(3,i3);
		 sheet.setColumnWidth(4,i4);
		 
		 workbook.write(out);
		 out.flush();
		 out.close();
	}
	catch(Exception e){
		 logger.error("---------------数据导出失败--------");
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
}
