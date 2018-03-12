package com.supconit.kqfx.web.fxzf.search.controllers;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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

import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.fxzf.search.entities.IllegalTrail;
import com.supconit.kqfx.web.fxzf.search.services.FxzfSearchService;
import com.supconit.kqfx.web.fxzf.search.services.IllegalTrailService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 违法轨迹查询
 * @author gaoshuo
 *
 */
@SuppressWarnings("deprecation")
@RequestMapping("/fxzf/illegalTrail")
@Controller("taizhou_offsite_enforcement_searchIllegalTrail_controller")
public class IllegalTrailController {
	
	
	private static final String MODULE_CODE = "ILLEGAL_TRACK";
	
	@Autowired
	private IllegalTrailService illegalTrailService;
	
	@Autowired
	private DataDictionaryService dataDictionaryService;
	
	@Autowired
	private SystemLogService systemLogService;
	
	@Autowired
	private FxzfSearchService fxzfSearchService;
	
	
	
	private transient static final Logger logger = LoggerFactory
			.getLogger(FxzfSearchController.class);
	
	@Resource
	private HttpServletRequest request;
	/**
	 * 违章车轨迹实体类
	 * @return
	 */
	@ModelAttribute("illegalTrail")
	private IllegalTrail getIllegalTrail(){
		IllegalTrail illegalTrail = new IllegalTrail();
		return illegalTrail;
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String display(ModelMap map,
			@ModelAttribute("illegalTrail") IllegalTrail illegalTrailInfo) {
		logger.info("----------------------非现数据查询列表---------------");
		return "/fxzf/illegalTrail/list";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	 public Pagination<IllegalTrail> list(Pagination<IllegalTrail> pager,
			 @FormBean(value = "condition", modelCode = "illegalTrail") IllegalTrail condition) {
		//this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
		//		"获取过车记录数据", request.getRemoteAddr());
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"过车轨迹信息列表", request.getRemoteAddr());
			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
			HashMap<String, String> plateColorMap = DictionaryUtil.dictionary("PLATECOLOR",dataDictionaryService);
			if(condition.getPlateColor()!=null){
				condition.setPlateColor(plateColorMap.get(condition.getPlateColor()));
			}
			illegalTrailService.findByPager(pager, condition);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}
	
	@RequestMapping(value = "record", method = RequestMethod.GET)
	public String trailRecod(ModelMap map,
			@FormBean(value = "condition", modelCode = "illegalTrail") IllegalTrail condition,HttpServletRequest request) {
		logger.info("----------------------非现数据查询列表---------------");
		
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(),
					"过车轨迹详情信息", request.getRemoteAddr());
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
		return "/fxzf/illegalTrail/record";
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
							String  pageNo,@FormBean(value = "condition", modelCode = "illegalTrail") IllegalTrail condition){
	     logger.info("-------------------------导出本页excel列表---------------");
	     this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
					"过车信息列表导出当前页", request.getRemoteAddr());
		 Pagination<IllegalTrail> pager = new Pagination<IllegalTrail>();
		 pager.setPageNo(Integer.valueOf(pageNo));
		 pager.setPageSize(20);
		 HashMap<String, String> plateColorMap = DictionaryUtil.dictionary("PLATECOLOR",dataDictionaryService);
		 if(condition.getPlateColor()!=null){
				condition.setPlateColor(plateColorMap.get(condition.getPlateColor()));
			}
		 Pageable<IllegalTrail> page=(Pagination<IllegalTrail>) illegalTrailService.findByPager(pager, condition);
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		 Date date = new Date();
	     String time = formatter.format(date);
		 String title = "违章过车记录查询_"+time+".xls";
		 editExcel(page,response,title);
	}
	
	
	
	@RequestMapping(value = "exportAll", method = RequestMethod.GET)
	public void exportAll(HttpServletRequest request,
							HttpServletResponse response,
							@FormBean(value = "condition", modelCode = "illegalTrail") IllegalTrail condition){
	     logger.info("-------------------------导出本页excel列表---------------");
	     this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
					"过车信息列表导出全部", request.getRemoteAddr());
		 Pagination<IllegalTrail> pager = new Pagination<IllegalTrail>();
		 pager.setPageNo(1);
		 pager.setPageSize(Integer.MAX_VALUE);
		 HashMap<String, String> plateColorMap = DictionaryUtil.dictionary("PLATECOLOR",dataDictionaryService);
		 if(condition.getPlateColor()!=null){
				condition.setPlateColor(plateColorMap.get(condition.getPlateColor()));
			}
		 Pageable<IllegalTrail> page=(Pagination<IllegalTrail>) illegalTrailService.findByPager(pager, condition);
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		 Date date = new Date();
	     String time = formatter.format(date);
		 String title = "违章过车记录查询_"+time+".xls";
		 editExcel(page,response,title);
	}
	
	void editExcel(Pageable<IllegalTrail> pager,HttpServletResponse response,String title){
		 OutputStream out=null;
		 try{
	   response.setHeader("Content-Disposition", "attachment; filename="
				+  new String(title.getBytes("GB2312"), "iso8859-1"));
	   response.setContentType("application/msexcel;charset=UTF-8");
	    out  =response.getOutputStream();
	    HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("违章车数据查询"));
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
		celltop.setCellValue("违章过车记录");
		celltop.setCellStyle(style3);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String[] head = { "序号", "车牌号", "车牌颜色", "历史超限次数", "最后违法过车时间"};
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
			   if(pager.get(i).getLicense()!=null){
				   cell3.setCellValue(pager.get(i).getOverLoadTimes());
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
