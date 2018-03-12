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
import com.supconit.kqfx.web.analysis.entities.TimeZone;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.analysis.services.TimeZoneService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 超限统计-按小时超限统计
 *
 * @author zongkai
 */
@SuppressWarnings("deprecation")
@RequestMapping("/analysis/overload/houroverload")
@Controller("analysis_hour_controller")
public class HourAnalysisController {

    private static final String MODULE_CODE = "HOUR_ANALYSIS";

    @Autowired
    private TimeZoneService timeZoneService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

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

    @ModelAttribute("timezone")
    private TimeZone getTimeZone() {
        TimeZone timezone = new TimeZone();
        return timezone;
    }

    private transient static final Logger logger = LoggerFactory.getLogger(HourAnalysisController.class);

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(ModelMap model) {
        this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(), "按小时超限统计", request.getRemoteAddr());
        try {
            User user = (User) safetyManager.getAuthenticationInfo().getUser();
            if (null != user && null != user.getPerson() && null != user.getPersonId()) {
                ExtPerson person = personService.getById(user.getPersonId());
                //根据JGID进行权限限制
                //若是超级管理员查询JGID = null
                //大桥的为 JGID = 133
                // 二桥为JGID=134
                model.put("jgid", person.getJgbh());
            }
            HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME", dataDictionaryService);
            for (String key : stationMap.keySet()) {
                model.put("station" + key, stationMap.get(key));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "analysis/overload/houroverload/list";
    }


    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Pagination<TimeZone> list(Pagination<TimeZone> pager, @FormBean(value = "condition", modelCode = "timezone") TimeZone condition) {
        try {
            this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(), "按小时超限统计", request.getRemoteAddr());
            if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
                return pager;
            timeZoneService.findByHourPager(pager, condition);
            TimeZone hourCnt = timeZoneService.getPartTimeCount(condition);
            for (TimeZone hour : pager) {
                if (hour.getTimeString() != null) {
                    hour.setTimeString(hour.getTimeString().substring(0, 2));
                    int time1 = Integer.valueOf(hour.getTimeString());
                    int time2 = time1 + 1;
                    hour.setTimeString(String.valueOf(time1) + "~" + String.valueOf(time2) + "点");
                    if (hour.getStationA() == null) {
                        hour.setStationA(0);
                    } else {
                        hour.setStationA(hour.getStationA() / hourCnt.getCntHours(hour.getTimeString()));
                    }
                    if (hour.getStationB() == null) {
                        hour.setStationB(0);
                    } else {
                        hour.setStationB(hour.getStationB() / hourCnt.getCntHours(hour.getTimeString()));
                    }
                    if (hour.getStationC() == null) {
                        hour.setStationC(0);
                    } else {
                        hour.setStationC(hour.getStationC() / hourCnt.getCntHours(hour.getTimeString()));
                    }
                    if (hour.getStationD() == null) {
                        hour.setStationD(0);
                    } else {
                        hour.setStationD(hour.getStationD() / hourCnt.getCntHours(hour.getTimeString()));
                    }
                    if (hour.getStationE() == null) {
                        hour.setStationE(0);
                    } else {
                        hour.setStationE(hour.getStationE() / hourCnt.getCntHours(hour.getTimeString()));
                    }
                    if (condition.getJgid() == null) {
                        hour.setTotal(hour.getStationA() + hour.getStationB() + hour.getStationC() + hour.getStationD() + hour.getStationE());
                    } else {
                        if (condition.getJgid() == 133) {
                            hour.setTotal(hour.getStationA() + hour.getStationB());
                        } else if (condition.getJgid() == 134) {
                            hour.setTotal(hour.getStationC() + hour.getStationD() + hour.getStationE());
                        } else {
                            hour.setTotal(hour.getStationA() + hour.getStationB() + hour.getStationC() + hour.getStationD() + hour.getStationE());
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
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "detectionStation", method = RequestMethod.POST)
    AjaxMessage getDetectionStation() {
        try {
            HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME", dataDictionaryService);
            List<String> stationList = new ArrayList<String>();
            Boolean isAdmin = false;
            Long jgid = null;
//			根据人员来获取查询权限
            User user = (User) safetyManager.getAuthenticationInfo().getUser();
            List<Role> rolelist = this.roleService.findAssigned(user.getId());
            if (!CollectionUtils.isEmpty(rolelist)) {
                for (Role role : rolelist) {
                    if ("ROLE_ADMIN".equals(role.getCode())) isAdmin = true;
                }
            }
            if (!isAdmin) {
                ExtPerson person = personService.getById(user.getPersonId());
                jgid = null != person ? person.getJgbh() : null;
//				人员部分权限
//				根据权限设置查询的治超站范围
                List<JgZcd> zcdList = jgZcdService.getByJgid(jgid);
                for (JgZcd jgZcd : zcdList) {
                    stationList.add(jgZcd.getDeteStation() + ":" + stationMap.get(jgZcd.getDeteStation()));
                }
            } else {
//				超级管理员权限
//				所有的治超站权限
                for (String key : stationMap.keySet()) {
                    stationList.add(key + ":" + stationMap.get(key));
                }
            }
            return AjaxMessage.success(stationList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 获取柱状图数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getChartData", method = RequestMethod.POST)
    public AjaxMessage getChartData(@FormBean(value = "condition", modelCode = "timezone") TimeZone condition) {
        try {
            this.systemLogService.log(MODULE_CODE, OperateType.view.getCode(),
                    "查询按小时超限图表信息", request.getRemoteAddr());

            TimeZone hourCnt = new TimeZone();
            //是否选择日期没有选择查看昨天数据信息
            List<TimeZone> houroverLoadList = new ArrayList<TimeZone>();
            if (condition.getTjDate() == null) {
                houroverLoadList = timeZoneService.getByPartTime(condition);
                hourCnt = timeZoneService.getPartTimeCount(condition);
            } else {
                //获取选择时间段内统计数据信息
                houroverLoadList = timeZoneService.getByPartTime(condition);
                hourCnt = timeZoneService.getPartTimeCount(condition);
            }
            if (!CollectionUtils.isEmpty(houroverLoadList)) {
//				横轴数据(1~24小时)
                List<String> xAisData = new ArrayList<String>();
                for (int i = 0; i < 24; i++) {
                    xAisData.add(String.valueOf(i) + "~" + String.valueOf(i + 1) + "点");
                }

//				设置legend(治超站)
                HashMap<String, String> detectionLengend = DictionaryUtil.dictionary("STATIONNAME", dataDictionaryService);
                List<String> legend = new ArrayList<String>();
                if (condition.getJgid() == null) {
                    Iterator<String> iterator = detectionLengend.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        legend.add(detectionLengend.get(key));
                    }
                } else {
                    if (condition.getJgid() == 133) {
                        legend.add(detectionLengend.get("1"));
                        legend.add(detectionLengend.get("2"));
                    } else if (condition.getJgid() == 134) {
                        legend.add(detectionLengend.get("3"));
                        legend.add(detectionLengend.get("4"));
                        legend.add(detectionLengend.get("5"));
                    } else {
                        Iterator<String> iterator = detectionLengend.keySet().iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            legend.add(detectionLengend.get(key));
                        }
                    }
                }
                legend.add("汇总");

//				纵轴数据
                List<TimeZone> yAisData = new ArrayList<TimeZone>();
                for (TimeZone timeZone : houroverLoadList) {
                    //设置治超站名称
                    timeZone.setStation(detectionLengend.get(timeZone.getDetectStation()));
                    //设置24小时的值
                    if (timeZone.getAh() == null) {
                        timeZone.setAh(0);
                    } else {
                        timeZone.setAh(timeZone.getAh() / hourCnt.getAh());
                    }
                    if (timeZone.getBh() == null) {
                        timeZone.setBh(0);
                    } else {
                        timeZone.setBh(timeZone.getBh() / hourCnt.getBh());
                    }
                    if (timeZone.getCh() == null) {
                        timeZone.setCh(0);
                    } else {
                        timeZone.setCh(timeZone.getCh() / hourCnt.getCh());
                    }
                    if (timeZone.getDh() == null) {
                        timeZone.setDh(0);
                    } else {
                        timeZone.setDh(timeZone.getDh() / hourCnt.getDh());
                    }
                    if (timeZone.getEh() == null) {
                        timeZone.setEh(0);
                    } else {
                        timeZone.setEh(timeZone.getEh() / hourCnt.getEh());
                    }
                    if (timeZone.getFh() == null) {
                        timeZone.setFh(0);
                    } else {
                        timeZone.setFh(timeZone.getFh() / hourCnt.getFh());
                    }
                    if (timeZone.getGh() == null) {
                        timeZone.setGh(0);
                    } else {
                        timeZone.setGh(timeZone.getGh() / hourCnt.getGh());
                    }
                    if (timeZone.getHh() == null) {
                        timeZone.setHh(0);
                    } else {
                        timeZone.setHh(timeZone.getHh() / hourCnt.getHh());
                    }
                    if (timeZone.getIh() == null) {
                        timeZone.setIh(0);
                    } else {
                        timeZone.setIh(timeZone.getIh() / hourCnt.getIh());
                    }
                    if (timeZone.getJh() == null) {
                        timeZone.setJh(0);
                    } else {
                        timeZone.setJh(timeZone.getJh() / hourCnt.getJh());
                    }
                    if (timeZone.getKh() == null) {
                        timeZone.setKh(0);
                    } else {
                        timeZone.setKh(timeZone.getKh() / hourCnt.getKh());
                    }
                    if (timeZone.getLh() == null) {
                        timeZone.setLh(0);
                    } else {
                        timeZone.setLh(timeZone.getLh() / hourCnt.getLh());
                    }
                    if (timeZone.getMh() == null) {
                        timeZone.setMh(0);
                    } else {
                        timeZone.setMh(timeZone.getMh() / hourCnt.getMh());
                    }
                    if (timeZone.getNh() == null) {
                        timeZone.setNh(0);
                    } else {
                        timeZone.setNh(timeZone.getNh() / hourCnt.getNh());
                    }
                    if (timeZone.getOh() == null) {
                        timeZone.setOh(0);
                    } else {
                        timeZone.setOh(timeZone.getOh() / hourCnt.getOh());
                    }
                    if (timeZone.getPh() == null) {
                        timeZone.setPh(0);
                    } else {
                        timeZone.setPh(timeZone.getPh() / hourCnt.getPh());
                    }
                    if (timeZone.getQh() == null) {
                        timeZone.setQh(0);
                    } else {
                        timeZone.setQh(timeZone.getQh() / hourCnt.getQh());
                    }
                    if (timeZone.getRh() == null) {
                        timeZone.setRh(0);
                    } else {
                        timeZone.setRh(timeZone.getRh() / hourCnt.getRh());
                    }
                    if (timeZone.getSh() == null) {
                        timeZone.setSh(0);
                    } else {
                        timeZone.setSh(timeZone.getSh() / hourCnt.getSh());
                    }
                    if (timeZone.getTh() == null) {
                        timeZone.setTh(0);
                    } else {
                        timeZone.setTh(timeZone.getTh() / hourCnt.getTh());
                    }
                    if (timeZone.getUh() == null) {
                        timeZone.setUh(0);
                    } else {
                        timeZone.setUh(timeZone.getUh() / hourCnt.getUh());
                    }
                    if (timeZone.getVh() == null) {
                        timeZone.setVh(0);
                    } else {
                        timeZone.setVh(timeZone.getVh() / hourCnt.getVh());
                    }
                    if (timeZone.getWh() == null) {
                        timeZone.setWh(0);
                    } else {
                        timeZone.setWh(timeZone.getWh() / hourCnt.getWh());
                    }
                    if (timeZone.getXh() == null) {
                        timeZone.setXh(0);
                    } else {
                        timeZone.setXh(timeZone.getXh() / hourCnt.getXh());
                    }

                    timeZone.setTotal(timeZone.getAh() + timeZone.getBh() + timeZone.getCh() + timeZone.getDh() + timeZone.getEh() + timeZone.getFh() + timeZone.getGh() +
                            timeZone.getHh() + timeZone.getIh() + timeZone.getJh() + timeZone.getKh() + timeZone.getLh() + timeZone.getMh() + timeZone.getNh() +
                            timeZone.getOh() + timeZone.getPh() + timeZone.getQh() + timeZone.getRh() + timeZone.getSh() + timeZone.getTh() + timeZone.getUh() +
                            timeZone.getVh() + timeZone.getWh() + timeZone.getXh()
                    );
                    yAisData.add(timeZone);
                }

                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("xAis", xAisData);
                resultMap.put("legend", legend);
                resultMap.put("yAis", yAisData);
                //标志查询结果是否为空
                resultMap.put("success", 200);
                return AjaxMessage.success(resultMap);
            } else {
                Map<String, Object> resultMap = new HashMap<String, Object>();
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
                          String total,
                          @FormBean(value = "condition", modelCode = "timezone") TimeZone condition) {
        logger.info("-------------------------导出全部excel列表---------------");
        try {

            this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(),
                    "按小时超限导出全部记录", request.getRemoteAddr());

            Pagination<TimeZone> pager = new Pagination<TimeZone>();
            pager.setPageNo(1);
            pager.setPageSize(Integer.MAX_VALUE);

            timeZoneService.findByHourPager(pager, condition);
            TimeZone hourCnt = timeZoneService.getPartTimeCount(condition);
            for (TimeZone hour : pager) {
                if (hour.getTimeString() != null) {
                    hour.setTimeString(hour.getTimeString().substring(0, 2));
                    int time1 = Integer.valueOf(hour.getTimeString());
                    int time2 = time1 + 1;
                    hour.setTimeString(String.valueOf(time1) + "~" + String.valueOf(time2) + "点");
                    if (hour.getStationA() == null) {
                        hour.setStationA(0);
                    } else {
                        hour.setStationA(hour.getStationA() / hourCnt.getCntHours(hour.getTimeString()));
                    }
                    if (hour.getStationB() == null) {
                        hour.setStationB(0);
                    } else {
                        hour.setStationB(hour.getStationB() / hourCnt.getCntHours(hour.getTimeString()));
                    }
                    if (hour.getStationC() == null) {
                        hour.setStationC(0);
                    } else {
                        hour.setStationC(hour.getStationC() / hourCnt.getCntHours(hour.getTimeString()));
                    }
                    if (hour.getStationD() == null) {
                        hour.setStationD(0);
                    } else {
                        hour.setStationD(hour.getStationD() / hourCnt.getCntHours(hour.getTimeString()));
                    }
                    if (hour.getStationE() == null) {
                        hour.setStationE(0);
                    } else {
                        hour.setStationE(hour.getStationE() / hourCnt.getCntHours(hour.getTimeString()));
                    }
                    hour.setTotal(hour.getStationA() + hour.getStationB() + hour.getStationC() +
                            hour.getStationD() + hour.getStationE()
                    );
                }
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String time = formatter.format(date);
            String title = "按小时超限统计记录_" + time + ".xls";
            editExcel(pager, response, title, condition);
        } catch (Exception e) {
            logger.info("-------------------------导出全部excel列表---------------");
        }
    }

    HSSFCellStyle setHSSFCellStyle(HSSFCellStyle style) {
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return style;
    }

    void editExcel(Pageable<TimeZone> pager, HttpServletResponse response, String title, TimeZone condition) {
        OutputStream out = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(title.getBytes("GB2312"), "iso8859-1"));
            response.setContentType("application/msexcel;charset=UTF-8");
            out = response.getOutputStream();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("按小时超限统计记录"));
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

            //获取导出条件
            String conditionString = "";
            //获取导出的时间
            if (condition.getTjDate() != null) {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String begin = format1.format(condition.getTjDate());
                if (condition.getUpdateTime() != null) {
                    String end = format1.format(condition.getUpdateTime());
                    end = format1.format(condition.getUpdateTime());
                    conditionString = "统计日期：" + begin + " 至  " + end + "\r\n";
                } else {
                    conditionString = "统计日期：" + begin + "开始至今为止\r\n";
                }
            }
            //获取导出治超站
            HashMap<String, String> stationMap = DictionaryUtil.dictionary("STATIONNAME", dataDictionaryService);
            String detecString = "";
            if (condition.getDetectStation() == null || condition.getDetectStation().equals("") || condition.getDetectStation() == "") {
                if (condition.getJgid() == null) {
                    detecString = detecString + "全部治超站";
                } else {
                    if (condition.getJgid() == 133) {
                        detecString = detecString + " " + stationMap.get("1");
                        detecString = detecString + " " + stationMap.get("2");
                    } else if (condition.getJgid() == 134) {
                        detecString = detecString + " " + stationMap.get("3");
                        detecString = detecString + " " + stationMap.get("4");
                        detecString = detecString + " " + stationMap.get("5");
                    } else {
                        detecString = detecString + "全部治超站";
                    }
                }
            } else {
                detecString = stationMap.get(condition.getDetectStation());
            }


            conditionString = conditionString + "统计治超站：" + detecString;


            //获取用户权限,根据权限导出数据
            User user = (User) safetyManager.getAuthenticationInfo().getUser();
            if (null != user && null != user.getPerson() && null != user.getPersonId()) {
                ExtPerson person = personService.getById(user.getPersonId());
                condition.setJgid(person.getJgbh());
            }
            if (condition.getJgid() == null) {
                //导出全部
                exportExcelAll(pager, conditionString, style1, style2, style3, row, top, sheet);
            } else {
                if (condition.getJgid() == 133) {
                    //导出JGBH133
                    exportExcelJgid133(pager, conditionString, style1, style2, style3, row, top, sheet);
                } else if (condition.getJgid() == 134) {
                    //导出JGBH134
                    exportExcelJgid134(pager, conditionString, style1, style2, style3, row, top, sheet);
                }
            }


            workbook.write(out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void exportExcelAll(Pageable<TimeZone> pager, String conditionString,
                                HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3,
                                HSSFRow row, HSSFRow top, HSSFSheet sheet) {
        //设置表头长度
        for (int i = 0; i < 8; i++) {
            HSSFCell cell = top.createCell(i);
            cell.setCellStyle(style3);
        }
        //设置表头长度
        top.getSheet().addMergedRegion(new Region(0, (short) 0, 0, (short) 7));
        //设置表头样式
        HSSFCell celltop = top.createCell(0);
        top.setHeight((short) (200 * 4));
        celltop.setCellStyle(style3);
        //设置表头内容：
        celltop.setCellValue("按小时超限统计\r\n" + conditionString);


        String[] head = {"序号", "统计日期", "K110+150", "K109+800", "总计"};

        int i0 = 0, i1 = 0, i2 = 0, i3 = 0, i4 = 0;

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

        }
        for (int i = 0; i < pager.size(); i++) {
            row = sheet.createRow(i + 2);
//				   序号
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(1 + i);
            cell0.setCellStyle(style2);
            if (String.valueOf(1 + i).length() * 256 >= i0) {
                i0 = String.valueOf(1 + i).length() * 256 + 256 * 8;
            }
//				  统计时间
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellStyle(style2);
            if (pager.get(i).getTimeString() != null) {
                cell1.setCellValue(pager.get(i).getTimeString());
                if (pager.get(i).getTimeString().length() * 256 >= i1) {
                    i1 = pager.get(i).getTimeString().length() * 256 + 256 * 10;
                }
            } else {
                cell1.setCellValue("");
            }
////			   兰亭往绍兴
            HSSFCell cell2 = row.createCell(2);
            cell2.setCellStyle(style2);
            if (pager.get(i).getStationA() != null) {
                cell2.setCellValue(pager.get(i).getStationA());
            } else {
                cell2.setCellValue("");
            }
////			   兰亭往诸暨
            HSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(style2);
            if (pager.get(i).getStationB() != null) {
                cell3.setCellValue(pager.get(i).getStationB());
            } else {
                cell3.setCellValue("");
            }
////			      总计
            HSSFCell cell4 = row.createCell(4);
            cell4.setCellStyle(style2);
            if (pager.get(i).getStationC() != null) {
                cell4.setCellValue(pager.get(i).getStationC());
            } else {
                cell4.setCellValue("");
            }
        }
        sheet.setColumnWidth(0, i0);
        sheet.setColumnWidth(1, i1);
        sheet.setColumnWidth(2, i2);
        sheet.setColumnWidth(3, i3);
        sheet.setColumnWidth(4, i4);
    }

    private void exportExcelJgid133(Pageable<TimeZone> pager, String conditionString,
                                    HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3,
                                    HSSFRow row, HSSFRow top, HSSFSheet sheet) {

        //设置表头长度
        for (int i = 0; i < 5; i++) {
            HSSFCell cell = top.createCell(i);
            cell.setCellStyle(style3);
        }
        //设置表头长度
        top.getSheet().addMergedRegion(new Region(0, (short) 0, 0, (short) 4));
        //设置表头样式
        HSSFCell celltop = top.createCell(0);
        top.setHeight((short) (200 * 4));
        celltop.setCellStyle(style3);
        //设置表头内容：
        celltop.setCellValue("按小时超限统计\r\n" + conditionString);

        String[] head = {"序号", "统计日期", "K110+150", "K109+800", "总计"};

        int i0 = 0, i1 = 0, i2 = 0, i3 = 0, i4 = 0;

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
        }
        for (int i = 0; i < pager.size(); i++) {
            row = sheet.createRow(i + 2);
//			   序号
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(1 + i);
            cell0.setCellStyle(style2);
            if (String.valueOf(1 + i).length() * 256 >= i0) {
                i0 = String.valueOf(1 + i).length() * 256 + 256 * 8;
            }
//			  统计时间
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellStyle(style2);
            if (pager.get(i).getTimeString() != null) {
                cell1.setCellValue(pager.get(i).getTimeString());
                if (pager.get(i).getTimeString().length() * 256 >= i1) {
                    i1 = pager.get(i).getTimeString().length() * 256 + 256 * 10;
                }
            } else {
                cell1.setCellValue("");
            }
////		   椒江一桥北
            HSSFCell cell2 = row.createCell(2);
            cell2.setCellStyle(style2);
            if (pager.get(i).getStationA() != null) {
                cell2.setCellValue(pager.get(i).getStationA());
            } else {
                cell2.setCellValue("");
            }
////		   椒江一桥南
            HSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(style2);
            if (pager.get(i).getStationB() != null) {
                cell3.setCellValue(pager.get(i).getStationB());
            } else {
                cell3.setCellValue("");
            }
////	 	     总计
            HSSFCell cell4 = row.createCell(4);
            cell4.setCellStyle(style2);
            if (pager.get(i).getTotal() != null) {
                cell4.setCellValue(pager.get(i).getTotal());
            } else {
                cell4.setCellValue("");
            }
        }
        sheet.setColumnWidth(0, i0);
        sheet.setColumnWidth(1, i1);
        sheet.setColumnWidth(2, i2);
        sheet.setColumnWidth(3, i3);
        sheet.setColumnWidth(4, i4);

    }

    private void exportExcelJgid134(Pageable<TimeZone> pager, String conditionString,
                                    HSSFCellStyle style1, HSSFCellStyle style2, HSSFCellStyle style3,
                                    HSSFRow row, HSSFRow top, HSSFSheet sheet) {
        //设置表头长度
        for (int i = 0; i < 6; i++) {
            HSSFCell cell = top.createCell(i);
            cell.setCellStyle(style3);
        }
        //设置表头长度
        top.getSheet().addMergedRegion(new Region(0, (short) 0, 0, (short) 5));
        //设置表头样式
        HSSFCell celltop = top.createCell(0);
        top.setHeight((short) (200 * 4));
        celltop.setCellStyle(style3);
        //设置表头内容：
        celltop.setCellValue("按小时超限统计\r\n" + conditionString);


        String[] head = {"序号", "统计日期", "椒江二桥北74省道", "椒江二桥北75省道", "椒江二桥南75省道", "总计"};

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
        for (int i = 0; i < pager.size(); i++) {
            row = sheet.createRow(i + 2);
//			   序号
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(1 + i);
            cell0.setCellStyle(style2);
            if (String.valueOf(1 + i).length() * 256 >= i0) {
                i0 = String.valueOf(1 + i).length() * 256 + 256 * 8;
            }
//			  统计时间
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellStyle(style2);
            if (pager.get(i).getTimeString() != null) {
                cell1.setCellValue(pager.get(i).getTimeString());
                if (pager.get(i).getTimeString().length() * 256 >= i1) {
                    i1 = pager.get(i).getTimeString().length() * 256 + 256 * 10;
                }
            } else {
                cell1.setCellValue("");
            }
////		      椒江二桥北74省道 
            HSSFCell cell2 = row.createCell(2);
            cell2.setCellStyle(style2);
            if (pager.get(i).getStationC() != null) {
                cell2.setCellValue(pager.get(i).getStationC());
            } else {
                cell2.setCellValue("");
            }
////		      椒江二桥北75省道 
            HSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(style2);
            if (pager.get(i).getStationD() != null) {
                cell3.setCellValue(pager.get(i).getStationD());
            } else {
                cell3.setCellValue("");
            }
////		      椒江二桥南75省道 
            HSSFCell cell4 = row.createCell(4);
            cell4.setCellStyle(style2);
            if (pager.get(i).getStationE() != null) {
                cell4.setCellValue(pager.get(i).getStationE());
            } else {
                cell4.setCellValue("");
            }
//			      总计
            HSSFCell cell5 = row.createCell(5);
            cell5.setCellStyle(style2);
            if (pager.get(i).getTotal() != null) {
                cell5.setCellValue(pager.get(i).getTotal());
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
    }


}

