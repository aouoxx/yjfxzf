package com.supconit.kqfx.web.fxzf.search.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
import com.supconit.kqfx.web.list.entities.WhiteList;
import com.supconit.kqfx.web.list.services.WhiteListService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 白名单管理
 * @author gaoshuo
 *
 */
@SuppressWarnings("deprecation")
@RequestMapping("/fxzf/whiteList")
@Controller("taizhou_offsite_enforcement_searchwhiteList_controller")
public class WhiteListController {
	private static final String MODULE_CODE = "WAITE_LIST";
	@Autowired
	private WhiteListService whiteListService;
	
	@Autowired
	private DataDictionaryService dataDictionaryService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private WriteRedisService writeRedisServices;
	
	private transient static final Logger logger = LoggerFactory
			.getLogger(WhiteListController.class);
	
	/**
	 * 创建WhiltList实体类
	 * @return
	 */
	@ModelAttribute("whiteList")
	private WhiteList getwhiteList(){
		WhiteList whiteList = new WhiteList();
		return whiteList;
	}
	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String display(ModelMap map,
			@ModelAttribute("whiteList") WhiteList whiteList) {
		return "/fxzf/whiteList/list";
	}
	
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	 public Pagination<WhiteList> list(Pagination<WhiteList> pager,
			 @FormBean(value = "condition", modelCode = "whiteList") WhiteList condition) {
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"白名单列表", request.getRemoteAddr());
			
			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
			whiteListService.findByPager(pager, condition);
			//如果下一页的删除完后，返回上一页的内容
			if(pager.getPageSize()==pager.getTotal()){
				Pagination<WhiteList> page = new Pagination<WhiteList>();
				page.setPageSize(pager.getPageSize());
				page.setPageNo(pager.getPageNo()-1);
				if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
				whiteListService.findByPager(page, condition);
				return page;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
		
	}
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addwhiteList(ModelMap map,
			@ModelAttribute("whiteList") WhiteList whiteListInfo){
		
		
		return "/fxzf/whiteList/add";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	 public AjaxMessage add(Pagination<WhiteList> pager,
			 @FormBean(value = "condition", modelCode = "whiteList") WhiteList condition) {
		logger.info("--------------------加入白名单--------------------");
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.insert.getCode(),
					"白名单列表", request.getRemoteAddr());
			
			HashMap<String, String> plateMap = DictionaryUtil.dictionary("PLATECOLOR",dataDictionaryService);
			condition.setPlateColor(plateMap.get(condition.getPlateColor()));

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//查看白名单中是否已经含有对应的数据,如果有则不能添加,否则可以添加
			int count = whiteListService.findByPlate(condition);
			if(count==0){
				Date date = new Date();
				format.format(date);
				condition.setId(IDGenerator.idGenerator());
				condition.setUpdateTime(date);
				condition.setOperateTime(date);
				condition.setDeleted(0);
				
				//添加到白名单数据库中
				whiteListService.insert(condition);
				//添加到redis内存序列中,白名单为1
				writeRedisServices.AddListToRedis(condition.getId(), condition.getLicense(), condition.getPlateColor(), 1);
				
				return AjaxMessage.success("200");
			}else{
				return AjaxMessage.success("1002");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxMessage.error("白名单添加失败");
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "deleteById", method = RequestMethod.POST)
	 public AjaxMessage deleteById(String id) {
		logger.info("--------------------删除白名单--------------------");
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.delete.getCode(),
					"白名单列表", request.getRemoteAddr());
			//获取需要删除的白名单信息
			WhiteList whiteList = whiteListService.getById(id);
			
			//删除删除名单设置，将标志位delete设为1,进行逻辑删除
			whiteListService.deleteById(id);
			
			//从redis中删除对应的信息, 1为白名单
			writeRedisServices.DeleteListToRedis(id, whiteList.getLicense(), whiteList.getPlateColor(), 1);
			
			return AjaxMessage.success("白名单添加成功");
		} catch (Exception e) {
			return AjaxMessage.error("白名单添加失败");
		}
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
							String  pageNo,@FormBean(value = "condition", modelCode = "whiteList") WhiteList condition){
	     logger.info("-------------------------导出本页excel列表---------------");
	     this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
					"导出白名单列表当前页", request.getRemoteAddr());
		 try {
			 Pagination<WhiteList> pager = new Pagination<WhiteList>();
			 pager.setPageNo(Integer.valueOf(pageNo));
			 pager.setPageSize(20);
			 if(condition.getLicense()!=null){
				 condition.setLicense(java.net.URLDecoder.decode(condition.getLicense(), "utf-8"));
			 }
			 Pageable<WhiteList> page=(Pagination<WhiteList>) whiteListService.findByPager(pager, condition);
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			 Date date = new Date();
		     String time = formatter.format(date);
			 String title = "白名单记录_"+time+".xls";
			 editExcel(page,response,title);
		} catch (Exception e) {
			logger.error("---------------------导出excel列表失败--------------------");
			e.printStackTrace();
		}
		
	}
	
	
	@RequestMapping(value = "exportAll", method = RequestMethod.GET)
	public void exportAll(HttpServletRequest request,
							HttpServletResponse response,
							@FormBean(value = "condition", modelCode = "whiteList") WhiteList condition){
	     logger.info("-------------------------导出本页excel列表---------------");
	     try {
	    	 this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
						"导出全部白名单列表", request.getRemoteAddr());
	    	 Pagination<WhiteList> pager = new Pagination<WhiteList>();
			 pager.setPageNo(1);
			 pager.setPageSize(Integer.MAX_VALUE);
			 if(condition.getLicense()!=null){
				 condition.setLicense(java.net.URLDecoder.decode(condition.getLicense(), "utf-8"));
			 }
			 Pageable<WhiteList> page=(Pagination<WhiteList>) whiteListService.findByPager(pager, condition);
			 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			 Date date = new Date();
		     String time = formatter.format(date);
			 String title = "白名单记录_"+time+".xls";
			 editExcel(page,response,title);
		} catch (Exception e) {
			logger.error("---------------导出全部excel列表出错--------------");
			e.printStackTrace();
		}
		 
		 
	}
	
	void editExcel(Pageable<WhiteList> pager,HttpServletResponse response,String title){
		 OutputStream out=null;
		 try{
	   response.setHeader("Content-Disposition", "attachment; filename="
				+  new String(title.getBytes("GB2312"), "iso8859-1"));
	   response.setContentType("application/msexcel;charset=UTF-8");
	    out  =response.getOutputStream();
	    HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("白名单记录"));
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
		
		for(int i=0;i<4;i++){
			HSSFCell cell = top.createCell(i);
			cell.setCellStyle(style3);
		}
		top.getSheet().addMergedRegion(new Region(0,(short)0,0,(short)3));
		HSSFCell celltop = top.createCell(0);
		celltop.setCellValue("白名单信息");
		celltop.setCellStyle(style3);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String[] head = { "序号", "车牌号", "车牌颜色" , "加入白名单时间"};
		int i0 = 0,i1=0,i2=0,i3=0;
		for (int i = 0; i < head.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(head[i]);
			cell.setCellStyle(style1);
			if(i==0){ i0= head[i].length()*256+256*10;}
			if(i==1){ i1= head[i].length()*256+256*10;}
			if(i==2){ i2= head[i].length()*256+256*10;}
			if(i==3){ i3= head[i].length()*256+256*10;}
		
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
			   
////		      加入白名单时间
			   HSSFCell cell3 =row.createCell(3);
			   cell3.setCellStyle(style2);
			   if(pager.get(i).getUpdateTime()!=null){
				   cell3.setCellValue(format.format(pager.get(i).getUpdateTime()));
				   if(format.format(pager.get(i).getUpdateTime()).length()*256>=i2){
					   i3=format.format(pager.get(i).getUpdateTime()).length()*256+256*10;
				   } 
			   }else{
				   cell3.setCellValue("");
			   }

		  }
		
		 sheet.setColumnWidth(0,i0);
		 sheet.setColumnWidth(1,i1);
		 sheet.setColumnWidth(2,i2);
		 sheet.setColumnWidth(3,i3);
		 
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
