package com.supconit.kqfx.web.fxzf.monitor.controllers;

import hc.base.domains.AjaxMessage;
import hc.business.dic.domains.FlatData;
import hc.business.dic.entities.Data;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supconit.kqfx.web.analysis.entities.JgZcd;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.fxzf.avro.redis.ReadRedisService;
import com.supconit.kqfx.web.fxzf.monitor.entities.ZTreeNode;
import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfLane;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfMonitor;
import com.supconit.kqfx.web.fxzf.search.services.FxzfLaneService;
import com.supconit.kqfx.web.fxzf.search.services.FxzfMonitorService;
import com.supconit.kqfx.web.fxzf.search.services.FxzfSearchService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.xtgl.services.JgxxService;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

@RequestMapping("/fxzf/monitor")
@Controller("tzfxzf_monitor_controller")
public class MonitorController {
	
	private transient static final Logger logger = LoggerFactory.getLogger(MonitorController.class);
	
	private static String MENU_CODE = "FXZF_MONITOR";
	
	private static String DIC_DETECT = "DETECTIONSTATION";
	
	private static String DIC_OVERLOAD = "OVERLOADSTATUS";
	
	@Autowired
	private  DataDictionaryService dataDictionaryService;
	
	@Autowired
	private FxzfSearchService	fxzfSearchService;
	
	@Autowired
	private JgxxService	jgxxService;
	
	@Autowired
	private JgZcdService jgZcdService;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private SystemLogService	systemLogService;
	
	@Autowired
	private FxzfMonitorService fxzfMonitorService;
	
	@Autowired
	private FxzfLaneService fxzfLaneService;
	
	@Autowired
	private ReadRedisService readRedisService;
	
	/**
	 * 初始化页面
	 * @return
	 */
	@RequestMapping("/init")
	public String Init(String treeNode,ModelMap model,HttpServletRequest request){
		logger.info("==查看监控页面==");
		List<ZTreeNode> result = this.jgZcdService.getZtreeNodeByAuth();
		FlatData overloadData = (FlatData) this.dataDictionaryService.getByCode(DIC_OVERLOAD);
		model.put("result", JSON.toJSONString(result));
		model.put("overloadData",overloadData);
		model.put("treeNode", !StringUtil.isEmpty(treeNode)?JSONObject.toJSONString(treeNode.split(",")):"");
		//获取人员数据权限
		List<JgZcd> zcdList = jgZcdService.getZcdListByAuth();
		String personRole = "";
		List<String> stationStrings = new ArrayList<String>();
		if(!CollectionUtils.isEmpty(zcdList))
		{
			for (JgZcd jgZcd : zcdList) {
				personRole = personRole+jgZcd.getDeteStation()+",";
				stationStrings.add(jgZcd.getDeteStation());
			}
		}
		String username=(String) request.getSession().getAttribute("username");
		model.put("username", username);
		model.put("personRole", personRole);
		
		/**
		 * 获取权限对应的违法记录
		 */
		FxzfMonitor monitor = new FxzfMonitor();
		monitor.setStationStrings(stationStrings.toArray(new String[stationStrings.size()]));
		List<FxzfMonitor> list= new ArrayList<FxzfMonitor>();
		list = fxzfMonitorService.getByTotalStation(monitor);
		HashMap<String, String> overLoadMap = DictionaryUtil.dictionary("OVERLOADSTATUS",dataDictionaryService);
		for(FxzfMonitor temp : list){
			temp.setOverLoadString(overLoadMap.get(temp.getOverLoadStatus()+""));
		}
		JSONArray jsonArray = JSONArray.fromObject(list);
		model.put("marqueeListTotal", jsonArray);
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"监控系统主页面", request.getRemoteAddr());
		return "fxzf/monitor/init";
	}
			
	@RequestMapping("/list")
	public String list(@RequestParam(required = true)String detestation,ModelMap model,HttpServletRequest request){
		
		FlatData overloadData = (FlatData) this.dataDictionaryService.getByCode(DIC_OVERLOAD);
		Data data = this.dataDictionaryService.getByDataCode(DIC_DETECT, detestation);
		model.put("data", data);
		model.put("overloadData",overloadData);
		String imageServer = (String) request.getSession().getAttribute("imageServer");
		model.put("imageServerAddr", imageServer); 
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"监控系统治超点监控页面", request.getRemoteAddr());
		/**
		 * 记录对应治超站最近的五条超限记录
		 */
		List<FxzfMonitor> list= new ArrayList<FxzfMonitor>();
		list=fxzfMonitorService.getByStation(detestation);
		HashMap<String, String> overLoadMap = DictionaryUtil.dictionary("OVERLOADSTATUS",dataDictionaryService);
		for(FxzfMonitor monitor : list){
			monitor.setOverLoadString(overLoadMap.get(monitor.getOverLoadStatus()+""));
		}
		JSONArray jsonArray = JSONArray.fromObject(list);
		model.put("marqueeList", jsonArray);
		
		/**
		 * 获取每个车道对应的最近的超限记录
		 */
		List<FxzfLane> laneList= new ArrayList<FxzfLane>();
		laneList=fxzfLaneService.getByStation(detestation);
		for(FxzfLane lane : laneList){
			lane.setOverLoadString(overLoadMap.get(lane.getOverLoadStatus()+""));
		}
		JSONArray jsonArrayLane = JSONArray.fromObject(laneList);
		model.put("laneList", jsonArrayLane);
		/**
		 * 获取情报板当前信息
		 */
//		redis_BoardData reisBoardData = this.readRedisService.ReadBoardData(detestation);
//		String qbbContent = (null!=reisBoardData?String.valueOf(reisBoardData.getSQbb()):"");
//		model.put("qbbContent", qbbContent);
		
		/**
		 * 获取对应的station
		 */
		model.put("stationDetectionFlag", detestation);
		//绍兴到兰亭(三车道)
		if("1".equals(detestation)){
			return "fxzf/monitor/track_two";
			
		//绍兴到诸暨两车道)
		}else if("2".equals(detestation)){
			return "fxzf/monitor/track_three";
		//二桥(四车道)
		}else{
			return "fxzf/monitor/track_four";
		}
	}
	
	@ResponseBody
	@RequestMapping("getfxzfcount")
	public AjaxMessage getFxzfCount(@FormBean(value = "fxzf", modelCode = "prepareFxzf") Fxzf fxzf){
		try {
			Map<String,Object> result = new HashMap<String, Object>();
			
			//当汇总主界面数据时获取对应数据权限
			if(StringUtil.isEmpty(fxzf.getDetectStation())){
				List<JgZcd> jgZcds =this.jgZcdService.getZcdListByAuth();
				if(!CollectionUtils.isEmpty(jgZcds)){
					String[] detects = new String[jgZcds.size()];
					for (int i = 0; i < jgZcds.size(); i++) {
						detects[i] = jgZcds.get(i).getDeteStation();
					}
					fxzf.setDetects(detects);
				}
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
			Date beginDate = cal.getTime();
			cal.add(Calendar.DATE, 1);
			Date endDate = cal.getTime();
			
			cal.add(Calendar.DATE, -1);
			Date yestday = cal.getTime();
			fxzf.setBackTime(yestday);
			//截止昨日治超点总过车数量
			Integer detectCount = this.fxzfSearchService.getCountByCondition(fxzf);
			//治超点违法过车数量
			fxzf.setBackTime(null);
			fxzf.setOverLoadStatus(0);
			Integer illegalCount = this.fxzfSearchService.getCountByCondition(fxzf);
			//治超点当天违法过车数量
			fxzf.setCaptureTime(beginDate);
			fxzf.setBackTime(endDate);
			Integer dayIllegalCount = this.fxzfSearchService.getCountByCondition(fxzf);
			//当天违法过车数量程度信息
			FlatData data = (FlatData) this.dataDictionaryService.getByCode(DIC_OVERLOAD);
			Integer[] overloadCount = new Integer[data.size()-1];
			if(!CollectionUtils.isEmpty(data)){
				for (int i = 1; i < data.size(); i++) {
					fxzf.setOverLoadStatus(Integer.valueOf(data.get(i).getCode()));
					overloadCount[i-1] = this.fxzfSearchService.getOverLoadCount(fxzf);
				}
			}
			
			result.put("detectCount", detectCount);
			result.put("illegalCount", illegalCount);
			result.put("dayIllegalCount", dayIllegalCount);
			result.put("overloadCount", overloadCount);
			this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
					"监控系统过车数据查询", request.getRemoteAddr());
			return AjaxMessage.success().setData(result);
		} catch (Exception e) {
			e.printStackTrace();
			this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
					"监控系统过车数据查询", request.getRemoteAddr());
			return AjaxMessage.error(e.toString());
		}
	}
} 
