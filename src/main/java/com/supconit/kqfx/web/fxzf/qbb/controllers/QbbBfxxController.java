package com.supconit.kqfx.web.fxzf.qbb.controllers;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pagination;
import hc.business.dic.domains.FlatData;
import hc.business.dic.entities.Data;
import hc.business.dic.services.DataDictionaryService;
import hc.mvc.annotations.FormBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;
import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.kqfx.web.fxzf.avro.redis_BoardData;
import com.supconit.kqfx.web.fxzf.avro.redis.ReadRedisService;
import com.supconit.kqfx.web.fxzf.qbb.entities.QbbBfxx;
import com.supconit.kqfx.web.fxzf.qbb.services.QbbBfxxService;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.SysConstants;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

@RequestMapping("/fxzf/qbb/qbbbfxx")
@Controller("fxzf_qbb_qbbbfxx_controller")
public class QbbBfxxController {

	private transient static final Logger logger = LoggerFactory
			.getLogger(QbbBfxxController.class);
	private static final String MENU_CODE = "FXZF_QBB_MANAGE";
	private static final String DIC_CODE_CZFS = "QBB_WAY";
	private static final String DIC_CODE_ZT = "QBB_FONT";
	private static final String DIC_CODE_YS = "QBB_COLOR";
	private static final String DIC_DETECT = "STATIONNAME";

	@Autowired
	private QbbBfxxService qbbBfxxService;

	@Autowired
	private DataDictionaryService dataDictionaryService;

	@Autowired
	private ReadRedisService readRedisService;
	
	@Autowired
	private ConfigService	configService;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private SystemLogService	systemLogService;
	
	@Value("${qbb.circle.time}")
	private long circletime;

	/**
	 * 准备实体对象。
	 * 
	 * @return
	 */
	@ModelAttribute("prepareQbbBfxx")
	public QbbBfxx prepareQbbBfxx() {
		return new QbbBfxx();
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"情报板发布列表", request.getRemoteAddr());
		return "fxzf/qbb/qbbbfxx/list";
	}

	/**
	 * AJAX获取列表数据。
	 * 
	 * @param pager
	 *            分页信息
	 * @param condition
	 *            查询条件
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public Pagination<QbbBfxx> list(
			Pagination<QbbBfxx> pager,
			@FormBean(value = "condition", modelCode = "prepareQbbBfxx") QbbBfxx condition) {
		/* Validate Pager Parameters */
		if (pager.getPageNo() < 1 || pager.getPageSize() < 1
				|| pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
			return pager;
		FlatData flatData =  (FlatData) this.dataDictionaryService.getByCode(DIC_DETECT);
		redis_BoardData redisBoardData = null;
		QbbBfxx bfxx = null;
		String qbbContent = "";
		/**
		 * 从redis中读取当前正在发布的信息
		 */
		for (Data data : flatData) {
			redisBoardData = this.readRedisService.ReadBoardData(data.getCode());
			if(null!=redisBoardData){
				bfxx = new QbbBfxx(); 
				bfxx.setLocation(data.getCode());
				qbbContent=String.valueOf(redisBoardData.getSQbb());
				bfxx.setXxnr(qbbContent);
				/**
				 * 根据治超站点和情报板发布内容获取,该治超站发布情报板列表
				 */
				List<QbbBfxx> bfxxlist = this.qbbBfxxService.getByCondition(bfxx);
				if(CollectionUtils.isEmpty(bfxxlist))
				{
					bfxx.setFlag("Y");
					bfxx.setCount(1);
					pager.add(bfxx); 
				}else{
					for(QbbBfxx qbb: bfxxlist){
						qbb.setCount(bfxxlist.size());
 						if(qbb.getXxnr().replace("_","").equals(qbbContent)){
							qbb.setFlag("Y");
						}
						else{
							qbb. setFlag("N");
						}
					}
					pager.addAll(bfxxlist);
				}
			}
		}
		
		if (!pager.isEmpty()) {
			for (QbbBfxx qbbBfxx : pager) {
				if (!StringUtils.isEmpty(qbbBfxx.getColor())) {
					Data color = dataDictionaryService.getByDataCode(
							DIC_CODE_YS, qbbBfxx.getColor());
					qbbBfxx.setColor(null != color ? color.getName() : "");
				}
				if (!StringUtils.isEmpty(qbbBfxx.getLocation())) {
					Data location = dataDictionaryService.getByDataCode(DIC_DETECT, qbbBfxx.getLocation());
					qbbBfxx.setLocationStr(null != location ? location
							.getName() : "");
				}
				qbbBfxx.setType("0".equals(qbbBfxx.getType()) ? "自动发布" :"1".equals(qbbBfxx.getType()) ? "手动发布" : "默认信息");
				qbbBfxx.setRemainTime(null != qbbBfxx.getRemainTime() ? qbbBfxx
						.getRemainTime() : null);
				qbbBfxx.setCircleTime(qbbBfxx.getCircleTime()/10);
			}
		}
		
		pager.setPageNo(1);
		pager.setPageSize(pager.size());
		pager.setTotal(pager.size());
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"情报板发布列表查询", request.getRemoteAddr());
		return pager;
	}

	@RequestMapping(value = "publish", method = RequestMethod.GET)
	public String publish(String location, ModelMap model) {
		model.put("location", location);
		this.systemLogService.log(MENU_CODE, OperateType.query.getCode(),
				"情报板发布页面", request.getRemoteAddr());
		return "fxzf/qbb/qbbbfxx/publish";
	}
	
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(String id, ModelMap model) {
		QbbBfxx condition = qbbBfxxService.getByBfxxId(id.trim().trim());
		condition.setXxnr(condition.getXxnr().replace("_", ""));
		model.put("location", condition.getLocation());
		model.put("xxnr",condition.getXxnr());
		/**
		 * 根据治超站点和情报板发布内容获取,该治超站发布情报板列表
		 */
		List<QbbBfxx> bfxxlist = this.qbbBfxxService.getByCondition(condition);
		model.put("list", JSONArray.fromObject(bfxxlist));
		this.systemLogService.log(MENU_CODE, OperateType.update.getCode(),
				"情报板修改页面", request.getRemoteAddr());
		return "fxzf/qbb/qbbbfxx/edit";
	}

	/**
	 * 发布情报板内容
	 * 
	 * @param location
	 *            情报板位置信息
	 * @param remainTime
	 *            停留时间
	 * @param circleTime
	 *            轮询时间
	 * @param cnt
	 *            内容
	 * @param way
	 *            显示方式
	 * @param font
	 *            字体
	 * @param color
	 *            颜色
	 * @param order
	 *            列表顺序
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "publish", method = RequestMethod.POST)
	public AjaxMessage publish(@RequestParam(required = true) String location,
			@RequestParam(required = true) String remainTime,
			@RequestParam(required = true) String circleTime, String[] cnt,
			String[] way, String[] font, String[] color, String[] order) {
		try {
			List<QbbBfxx>  bfxxs= new ArrayList<QbbBfxx>();
			Date date = new Date();
			Double stayTime1 = this.configService.getValueByCode(SysConstants.QBB_STAY_TIME);
			/** 转换为long 型  */
			Long stayTime = Math.round(stayTime1);
			
			if(cnt.length>1)
			{
				for (int i = 1; i < cnt.length; i++) {
					QbbBfxx bfxx = new QbbBfxx();
					bfxx.setLocation(location);
					//默认停留stayTime/60分钟
					bfxx.setRemainTime((long) (!StringUtil.isEmpty(remainTime)?Long.valueOf(remainTime):stayTime/60));
					//默认循环时间为60s
					bfxx.setCircleTime(!StringUtil.isEmpty(circleTime)?Long.valueOf(circleTime)*10:circletime);
					bfxx.setXxnr(cnt[i]);
					bfxx.setCzfs(way[i]);
					bfxx.setFont(font[i]);
					bfxx.setColor(color[i]);
					bfxx.setListOrder(!StringUtil.isEmpty(order[i])?Integer.valueOf(order[i]):i+1);
					bfxx.setDeleted(0);
					bfxx.setPublishTime(date);
					//手动发布
					bfxx.setType("1");
					bfxx.setId(IDGenerator.idGenerator());
					if(!bfxx.getXxnr().equals("")&&bfxx.getXxnr()!=null){
						bfxxs.add(bfxx);
					}
					
				}
				this.qbbBfxxService.publishToQbb(bfxxs,location,bfxxs.get(0).getRemainTime());
			}
			this.systemLogService.log(MENU_CODE, OperateType.insert.getCode(),
					"情报板发布", request.getRemoteAddr());
			return AjaxMessage.success();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.systemLogService.log(MENU_CODE, OperateType.insert.getCode(),
					"情报板发布", request.getRemoteAddr());
			return AjaxMessage.error(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(String id, ModelMap model) {
		
		logger.info("***情报板查看ID："+id);
		QbbBfxx condition = qbbBfxxService.getByBfxxId(id.trim().trim());
		condition.setXxnr(condition.getXxnr().replace("_", ""));
		model.put("id", condition.getId());
		model.put("location", condition.getLocation());
		model.put("xxnr",condition.getXxnr());
		
		
		HashMap<String, String> colorMap = DictionaryUtil.dictionary(DIC_CODE_YS,dataDictionaryService);
		HashMap<String, String> fontMap = DictionaryUtil.dictionary(DIC_CODE_ZT,dataDictionaryService);
		HashMap<String, String> wayMap = DictionaryUtil.dictionary(DIC_CODE_CZFS,dataDictionaryService);
		HashMap<String, String> locationMap = DictionaryUtil.dictionary(DIC_DETECT,dataDictionaryService);
		/**
		 * 根据治超站点和情报板发布内容获取,该治超站发布情报板列表
		 */
		List<QbbBfxx> bfxxlist = this.qbbBfxxService.getByCondition(condition);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(QbbBfxx qbb: bfxxlist){
			qbb.setType("0".equals(qbb.getType()) ? "自动发布" :"1".equals(qbb.getType()) ? "手动发布" : "默认信息");
			qbb.setColor(colorMap.get(qbb.getColor()));
			qbb.setFont(fontMap.get(qbb.getFont()));
			qbb.setCzfs(wayMap.get(qbb.getCzfs()));
			qbb.setLocationStr(locationMap.get(qbb.getLocation()));
			qbb.setPublishTimeString(format.format(qbb.getPublishTime()).toString());
		}
		model.put("list", JSONArray.fromObject(bfxxlist));
		this.systemLogService.log(MENU_CODE, OperateType.view.getCode(),
				"情报板查看页面", request.getRemoteAddr());
		return "fxzf/qbb/qbbbfxx/view";
	}
	
}
