package com.supconit.kqfx.web.fxzf.warn.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.supconit.kqfx.web.fxzf.search.controllers.FxzfSearchController;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnHistory;
import com.supconit.kqfx.web.fxzf.warn.services.WarnHistoryService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

@SuppressWarnings("deprecation")
@RequestMapping("/fxzf/warn/history")
@Controller("taizhou_offsite_enforcement_warnHistory_controller")
public class WarnHistoryController {

    private static final String MODULE_CODE = "FXZF_WARN_HISTORY";

    @Autowired
    private WarnHistoryService warnHistoryService;

    @Autowired
    private SystemLogService systemLogService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Resource
    private HttpServletRequest request;

    private transient static final Logger logger = LoggerFactory.getLogger(FxzfSearchController.class);

    /**
     * 创建告警历史实体类
     */
    @ModelAttribute("history")
    private WarnHistory getHistory() {
        WarnHistory history = new WarnHistory();
        return history;
    }


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String display(ModelMap map, @ModelAttribute("history") WarnHistory historyInfo) {
        logger.info("----------------------非现数据查询列表---------------");
        return "/fxzf/warn/history/list";
    }


    /**
     * 查看告警详情
     *
     * @return
     */
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String view(ModelMap map, @ModelAttribute("history") WarnHistory historyInfo, HttpServletRequest request) {
        logger.info("----------------------查看告警---------------");
        this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(), "查看详细告警信息", request.getRemoteAddr());
        historyInfo = warnHistoryService.getById(historyInfo.getId().trim().trim());

        HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME", dataDictionaryService);
        HashMap<String, String> overLoadMap = DictionaryUtil.dictionary("OVERLOADSTATUS", dataDictionaryService);
        HashMap<String, String> punishMap = DictionaryUtil.dictionary("OVERLOADPUNISH", dataDictionaryService);

        //转换字典项内容
        historyInfo.getFxzf().setDetectStation(stationMap.get(historyInfo.getFxzf().getDetectStation()));
        historyInfo.getFxzf().setOverStatus(overLoadMap.get(String.valueOf(historyInfo.getFxzf().getOverLoadStatus())));
        historyInfo.getFxzf().setOverPunish(punishMap.get(String.valueOf(historyInfo.getFxzf().getOverLoadPunish())));
        if (historyInfo.getFxzf().getPunishReason() != null) {
            HashMap<String, String> reasonMap = DictionaryUtil.dictionary("PUNISHREASON", dataDictionaryService);
            historyInfo.getFxzf().setPunishReason(reasonMap.get(historyInfo.getFxzf().getPunishReason()));
        }

        if (historyInfo.getFxzf().getLength() == -1) {
            historyInfo.getFxzf().setLength(null);
        }
        if (historyInfo.getFxzf().getWidth() == -1) {
            historyInfo.getFxzf().setWidth(null);
        }
        if (historyInfo.getFxzf().getHeight() == -1) {
            historyInfo.getFxzf().setHeight(null);
        }

        map.put("condition", historyInfo);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (historyInfo.getFxzf().getCaptureTime() != null) {
            String warnTime = format.format(historyInfo.getFxzf().getCaptureTime());
            map.put("date", warnTime);
        } else {
            map.put("date", "暂无过车时间");
        }

        String imageServer = (String) request.getSession().getAttribute("imageServer");
        map.put("imageServerAddr", imageServer);
        return "/fxzf/warn/history/view";
    }

    /**
     * 获取治超站数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "warnType", method = RequestMethod.POST)
    AjaxMessage getwarnType() {
        try {
            HashMap<String, String> stationMap = DictionaryUtil.dictionary("WARN_TYPR", dataDictionaryService);
            List<String> stationList = new ArrayList<String>();
            for (String key : stationMap.keySet()) {
                stationList.add(key + ":" + stationMap.get(key));
            }
            return AjaxMessage.success(stationList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Pagination<WarnHistory> list(Pagination<WarnHistory> pager,
                                        @FormBean(value = "condition", modelCode = "history") WarnHistory condition) {
        try {
            if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
                return pager;
            if (condition.getOverType() != null && !(condition.getOverType().equals("null"))) {
                String status[] = condition.getOverType().split(",");
                Integer[] data = new Integer[status.length];
                for (int i = 0; i < status.length; i++) {
                    data[i] = Integer.valueOf(status[i]);
                }
                condition.setType(data);
            } else {
                condition.setOverType(null);
            }
            HashMap<String, String> plateColorMap = DictionaryUtil.dictionary("PLATECOLOR", dataDictionaryService);
            if (condition.getPlateColor() != null) {
                condition.setPlateColor(plateColorMap.get(condition.getPlateColor()));
            }
            warnHistoryService.findByPager(pager, condition);
            //设置打开框的大小

            for (WarnHistory warn : pager) {
                if (warn.getFxzf().getOverLoadPunish() == 2 || warn.getFxzf().getOverLoadPunish() == 4) {
                    warn.setFlag("1");
                } else {
                    warn.setFlag("0");
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return pager;

    }


    /**
     * 导出一页
     *
     * @param request
     * @param response
     * @param pageNo
     */
    @RequestMapping(value = "exportPage", method = RequestMethod.GET)
    public void exportExcel(HttpServletRequest request,
                            HttpServletResponse response,
                            String pageNo, @FormBean(value = "condition", modelCode = "history") WarnHistory condition) {
        logger.info("-------------------------导出本页excel列表---------------");

        try {
            this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
                    "告警信息列表导出当前页", request.getRemoteAddr());
            Pagination<WarnHistory> pager = new Pagination<WarnHistory>();
            pager.setPageNo(Integer.valueOf(pageNo));
            pager.setPageSize(20);
            if (condition.getLicense() != null) {
                condition.setLicense(java.net.URLDecoder.decode(condition.getLicense(), "utf-8"));
            }
            HashMap<String, String> plateColorMap = DictionaryUtil.dictionary("PLATECOLOR", dataDictionaryService);
            if (condition.getPlateColor() != null) {
                condition.setPlateColor(plateColorMap.get(condition.getPlateColor()));
            }
            if (condition.getOverType() != null && !(condition.getOverType().equals("null"))) {
                String status[] = condition.getOverType().split(",");
                Integer[] data = new Integer[status.length];
                for (int i = 0; i < status.length; i++) {
                    data[i] = Integer.valueOf(status[i]);
                }
                condition.setType(data);
            } else {
                condition.setOverType(null);
            }
            Pageable<WarnHistory> page = (Pagination<WarnHistory>) warnHistoryService.findByPager(pager, condition);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String time = formatter.format(date);
            String title = "告警历史记录_" + time + ".xls";
            editExcel(page, response, title);
        } catch (Exception e) {
            logger.error("---------------------导出excel列表失败--------------------");
            e.printStackTrace();
        }

    }


    @RequestMapping(value = "exportAll", method = RequestMethod.GET)
    public void exportAll(HttpServletRequest request,
                          HttpServletResponse response,
                          @FormBean(value = "condition", modelCode = "history") WarnHistory condition) {
        logger.info("-------------------------导出本页excel列表---------------");
        try {
            this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
                    "告警信息列表导出全部记录", request.getRemoteAddr());
            Pagination<WarnHistory> pager = new Pagination<WarnHistory>();
            pager.setPageNo(1);
            pager.setPageSize(Integer.MAX_VALUE);
            if (condition.getLicense() != null) {
                condition.setLicense(java.net.URLDecoder.decode(condition.getLicense(), "utf-8"));
            }
            HashMap<String, String> plateColorMap = DictionaryUtil.dictionary("PLATECOLOR", dataDictionaryService);
            if (condition.getPlateColor() != null) {
                condition.setPlateColor(plateColorMap.get(condition.getPlateColor()));
            }
            if (condition.getOverType() != null && !(condition.getOverType().equals("null"))) {
                String status[] = condition.getOverType().split(",");
                Integer[] data = new Integer[status.length];
                for (int i = 0; i < status.length; i++) {
                    data[i] = Integer.valueOf(status[i]);
                }
                condition.setType(data);
            } else {
                condition.setOverType(null);
            }
            Pageable<WarnHistory> page = (Pagination<WarnHistory>) warnHistoryService.findByPagerExport(pager, condition);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String time = formatter.format(date);
            String title = "告警历史记录_" + time + ".xls";
            editExcel(page, response, title);
        } catch (Exception e) {
            logger.error("---------------导出全部excel列表出错--------------");
            e.printStackTrace();
        }


    }

    void editExcel(Pageable<WarnHistory> pager, HttpServletResponse response, String title) {
        OutputStream out = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(title.getBytes("GB2312"), "iso8859-1"));
            response.setContentType("application/msexcel;charset=UTF-8");
            out = response.getOutputStream();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("告警历史记录"));
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
            style1 = setHSSFCellStyle(style1);
            style1.setFillBackgroundColor(HSSFColor.AQUA.index);
            style1.setFillForegroundColor(HSSFColor.AQUA.index);
            style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            style2.setFont(font2);
            style2 = setHSSFCellStyle(style2);

            style3.setFont(font1);
            style3 = setHSSFCellStyle(style3);

            /** 字体居中 **/
            style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            for (int i = 0; i < 6; i++) {
                HSSFCell cell = top.createCell(i);
                cell.setCellStyle(style3);
            }
            top.getSheet().addMergedRegion(new Region(0, (short) 0, 0, (short) 5));
            HSSFCell celltop = top.createCell(0);
            celltop.setCellValue("告警历史信息");
            celltop.setCellStyle(style3);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String[] head = {"序号", "告警内容", "车牌号", "车牌颜色", "告警时间", "告警类型"};
            int i0 = 0, i1 = 0, i2 = 0, i3 = 0, i4 = 0, i5 = 0;
            for (int i = 0; i < head.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(head[i]);
                cell.setCellStyle(style1);
                if (i == 0) {
                    i0 = head[i].length() * 256 + 256 * 10;
                }
                if (i == 1) {
                    i1 = head[i].length() * 256 + 256 * 10;
                }
                if (i == 2) {
                    i2 = head[i].length() * 256 + 256 * 10;
                }
                if (i == 3) {
                    i3 = head[i].length() * 256 + 256 * 10;
                }
                if (i == 4) {
                    i4 = head[i].length() * 256 + 256 * 10;
                }
                if (i == 5) {
                    i5 = head[i].length() * 256 + 256 * 10;
                }

            }

            HashMap<String, String> stationMap = DictionaryUtil.dictionary("WARN_TYPR", dataDictionaryService);

            for (int i = 0; i < pager.size(); i++) {
                row = sheet.createRow(i + 2);
//			   序号
                HSSFCell cell0 = row.createCell(0);
                cell0.setCellValue(1 + i);
                cell0.setCellStyle(style2);
                if (String.valueOf(1 + i).length() * 256 >= i0) {
                    i0 = String.valueOf(1 + i).length() * 256 + 256 * 8;
                }
//			  告警内容
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellStyle(style2);
                if (pager.get(i).getWarnInfo() != null) {
                    cell1.setCellValue(pager.get(i).getWarnInfo());
                    if (pager.get(i).getWarnInfo().length() * 256 >= i1) {
                        i1 = pager.get(i).getWarnInfo().length() * 256 + 256 * 10;
                    }
                } else {
                    cell1.setCellValue("");
                }
//			  车牌号
                HSSFCell cell2 = row.createCell(2);
                cell2.setCellStyle(style2);
                if (pager.get(i).getLicense() != null) {
                    cell2.setCellValue(pager.get(i).getLicense());
                    if (pager.get(i).getLicense().length() * 256 >= i2) {
                        i2 = pager.get(i).getLicense().length() * 256 + 256 * 10;
                    }
                } else {
                    cell2.setCellValue("");
                }
//			   车牌颜色
                HSSFCell cell3 = row.createCell(3);
                cell3.setCellStyle(style2);
                if (pager.get(i).getPlateColor() != null) {
                    cell3.setCellValue(pager.get(i).getPlateColor());
                    if (pager.get(i).getPlateColor().length() * 256 >= i3) {
                        i3 = pager.get(i).getPlateColor().length() * 256 + 256 * 8;
                    }
                } else {
                    cell3.setCellValue("");
                }

////	  	告警时间
                HSSFCell cell4 = row.createCell(4);
                cell4.setCellStyle(style2);
                if (pager.get(i).getWarnTime() != null) {
                    cell4.setCellValue(format.format(pager.get(i).getWarnTime()));
                    if (format.format(pager.get(i).getWarnTime()).length() * 256 >= i4) {
                        i4 = format.format(pager.get(i).getWarnTime()).length() * 256 + 256 * 10;
                    }
                } else {
                    cell4.setCellValue("");
                }

//			   告警类型
                HSSFCell cell5 = row.createCell(5);
                cell5.setCellStyle(style2);
                if (pager.get(i).getWarnType() != null) {
                    cell5.setCellValue(stationMap.get(String.valueOf(pager.get(i).getWarnType())));
                    if (stationMap.get(String.valueOf(pager.get(i).getWarnType())).length() * 256 >= i5) {
                        i5 = stationMap.get(String.valueOf(pager.get(i).getWarnType())).length() * 256 + 256 * 10;
                    }
                } else {
                    cell5.setCellValue("");
                }

            }

            sheet.setColumnWidth(0, i0);
            sheet.setColumnWidth(1, i1);
            sheet.setColumnWidth(2, i2);
            sheet.setColumnWidth(3, i3);
            sheet.setColumnWidth(4, i4);
            sheet.setColumnWidth(5, i5);
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("---------------数据导出失败--------");
            e.printStackTrace();
        }
    }

    HSSFCellStyle setHSSFCellStyle(HSSFCellStyle style) {
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return style;
    }

}
