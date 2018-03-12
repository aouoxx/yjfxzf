package com.supconit.kqfx.web.fxzf.search.controllers;

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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supconit.honeycomb.business.authorization.entities.Role;
import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.honeycomb.business.authorization.services.RoleService;
import com.supconit.honeycomb.business.organization.services.PersonService;
import com.supconit.kqfx.web.analysis.entities.JgZcd;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfStatic;
import com.supconit.kqfx.web.fxzf.search.entities.IllegalTrail;
import com.supconit.kqfx.web.fxzf.search.services.FxzfStaticService;
import com.supconit.kqfx.web.fxzf.search.services.IllegalTrailService;
import com.supconit.kqfx.web.fxzf.search.services.ZczTreeService;
import com.supconit.kqfx.web.fxzf.warn.entities.Config;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnHistory;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnHistoryService;
import com.supconit.kqfx.web.list.entities.BlackList;
import com.supconit.kqfx.web.list.services.BlackListService;
import com.supconit.kqfx.web.util.DictionaryUtil;
import com.supconit.kqfx.web.util.OperateType;
import com.supconit.kqfx.web.util.SysConstants;
import com.supconit.kqfx.web.util.UtilTool;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.services.SystemLogService;

/**
 * 静态称重数据查询
 * 
 * @author JNJ
 * @time 2017年8月25日10:25:36
 * 
 */
@SuppressWarnings("deprecation")
@RequestMapping("/fxzf/static")
@Controller
public class FxzfStaticController {
	private static final String MODULE_CODE = "FXZF_STATIC";

	@Autowired
	private FxzfStaticService fxzfStaticService;

	@Autowired
	private DataDictionaryService dataDictionaryService;

	@Autowired
	private SystemLogService systemLogService;

	@Resource
	private HttpServletRequest request;

	@Autowired
	private SafetyManager safetyManager;

	@Autowired
	private RoleService roleService;

	@Autowired
	private JgZcdService jgZcdService;

	@Autowired
	private PersonService personService;

	@Autowired
	private WriteRedisService writeRedisService;

	@Autowired
	private IllegalTrailService illegalTrailService;

	@Autowired
	private BlackListService blackListService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private WarnHistoryService warnHistoryService;

	@Autowired
	private ZczTreeService zczTreeService;

	@Value("${refresh.time}")
	private String refreshTime;

	private transient static final Logger logger = LoggerFactory.getLogger(FxzfStaticController.class);

	/**
	 * 创建FxzfStatic实体类
	 * 
	 * @return
	 */
	@ModelAttribute("fxzfStatic")
	private FxzfStatic getFxzfStatic() {
		FxzfStatic fxzfStatic = new FxzfStatic();
		return fxzfStatic;
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String display(ModelMap map, @ModelAttribute("fxzfStatic") FxzfStatic condition, HttpServletRequest request) {
		this.systemLogService.log(MODULE_CODE, OperateType.query.getCode(), "过车信息列表", request.getRemoteAddr());
		String ip = request.getLocalAddr();
		logger.info("请求服务器的IP地址：" + ip);
		map.put("ip", ip);
		String imageServer = (String) request.getSession().getAttribute("imageServer");
		map.put("imageServerAddr", imageServer);
		map.put("refreshTime", refreshTime);
		return "/fxzf/static/list";
	}

	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public Pagination<FxzfStatic> list(Pagination<FxzfStatic> pager, @FormBean(value = "condition", modelCode = "fxzfStatic") FxzfStatic condition) {
		try {

			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
			// 是否选中只看超限,若选中说明是选择全部违法信息
			if (condition.getOverLoadFlag() != null && (!condition.getOverLoadFlag().equals(""))) {
				condition.setOverStatus(condition.getOverLoadFlag());
			}

			condition = editFxzf(condition);
			fxzfStaticService.findByPager(pager, condition);
//			HashMap<String, String> stationMap = DictionaryUtil.dictionary("DETECTIONSTATION", dataDictionaryService);
//			HashMap<String, String> overLoadMap = DictionaryUtil.dictionary("OVERLOADSTATUS",dataDictionaryService);
			HashMap<String, String> punishMap = DictionaryUtil.dictionary("OVERLOADPUNISH", dataDictionaryService);
			for (FxzfStatic fxzfStatic : pager) {
				// fxzfStatic.setDetectStationFlag(fxzfStatic.getDetectStation());
				// fxzfStatic.setDetectStation(stationMap.get(fxzfStatic.getDetectStation()));
				// fxzfStatic.setOverStatus(overLoadMap.get(String.valueOf(fxzfStatic.getOverLoadStatus())));
				fxzfStatic.setOverPunish(punishMap.get(String.valueOf(fxzfStatic.getOverLoadPunish())));

				if (fxzfStatic.getOverLoad() != null && (!fxzfStatic.getOverLoad().equals(""))) {
					if (fxzfStatic.getAxisCount() <= 2) {
						if (fxzfStatic.getOverLoad() <= 1) {
							fxzfStatic.setOverStatus("轻微");
						} else if (fxzfStatic.getOverLoad() > 1 && fxzfStatic.getOverLoad() <= 6) {
							fxzfStatic.setOverStatus("一般");
						} else if (fxzfStatic.getOverLoad() > 6 && fxzfStatic.getOverLoad() <= 10) {
							fxzfStatic.setOverStatus("较重");
						} else if (fxzfStatic.getOverLoad() > 10 && fxzfStatic.getOverLoad() <= 16) {
							fxzfStatic.setOverStatus("严重");
						} else {
							fxzfStatic.setOverStatus("特别严重");
						}
					} else if (fxzfStatic.getAxisCount() == 3) {
						if (fxzfStatic.getOverLoad() <= 1.5) {
							fxzfStatic.setOverStatus("轻微");
						} else if (fxzfStatic.getOverLoad() > 1.5 && fxzfStatic.getOverLoad() <= 10) {
							fxzfStatic.setOverStatus("一般");
						} else if (fxzfStatic.getOverLoad() > 10 && fxzfStatic.getOverLoad() <= 15) {
							fxzfStatic.setOverStatus("较重");
						} else if (fxzfStatic.getOverLoad() > 15 && fxzfStatic.getOverLoad() <= 25) {
							fxzfStatic.setOverStatus("严重");
						} else {
							fxzfStatic.setOverStatus("特别严重");
						}
					} else if (fxzfStatic.getAxisCount() == 4) {
						if (fxzfStatic.getOverLoad() <= 2) {
							fxzfStatic.setOverStatus("轻微");
						} else if (fxzfStatic.getOverLoad() > 2 && fxzfStatic.getOverLoad() <= 13) {
							fxzfStatic.setOverStatus("一般");
						} else if (fxzfStatic.getOverLoad() > 13 && fxzfStatic.getOverLoad() <= 20) {
							fxzfStatic.setOverStatus("较重");
						} else if (fxzfStatic.getOverLoad() > 20 && fxzfStatic.getOverLoad() <= 33) {
							fxzfStatic.setOverStatus("严重");
						} else {
							fxzfStatic.setOverStatus("特别严重");
						}
					} else if (fxzfStatic.getAxisCount() == 5) {
						if (fxzfStatic.getOverLoad() <= 2.5) {
							fxzfStatic.setOverStatus("轻微");
						} else if (fxzfStatic.getOverLoad() > 2.5 && fxzfStatic.getOverLoad() <= 14) {
							fxzfStatic.setOverStatus("一般");
						} else if (fxzfStatic.getOverLoad() > 14 && fxzfStatic.getOverLoad() <= 25) {
							fxzfStatic.setOverStatus("较重");
						} else if (fxzfStatic.getOverLoad() > 25 && fxzfStatic.getOverLoad() <= 42) {
							fxzfStatic.setOverStatus("严重");
						} else {
							fxzfStatic.setOverStatus("特别严重");
						}
					} else {
						if (fxzfStatic.getOverLoad() <= 3) {
							fxzfStatic.setOverStatus("轻微");
						} else if (fxzfStatic.getOverLoad() > 3 && fxzfStatic.getOverLoad() <= 18) {
							fxzfStatic.setOverStatus("一般");
						} else if (fxzfStatic.getOverLoad() > 18 && fxzfStatic.getOverLoad() <= 28) {
							fxzfStatic.setOverStatus("较重");
						} else if (fxzfStatic.getOverLoad() > 28 && fxzfStatic.getOverLoad() <= 46) {
							fxzfStatic.setOverStatus("严重");
						} else {
							fxzfStatic.setOverStatus("特别严重");
						}
					}
				} else {
					fxzfStatic.setOverStatus("正常");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}

	@ResponseBody
	@RequestMapping(value = "listDetail", method = RequestMethod.POST)
	public Pagination<FxzfStatic> listDatail(Pagination<FxzfStatic> pager, @FormBean(value = "condition", modelCode = "fxzfStatic") FxzfStatic condition) {
		try {
			if (pager.getPageNo() < 1 || pager.getPageSize() < 1 || pager.getPageSize() > Pagination.MAX_PAGE_SIZE)
				return pager;
			// 是否选中只看超限,若选中说明是选择全部违法信息
			if (condition.getOverLoadFlag() != null && (!condition.getOverLoadFlag().equals(""))) {
				condition.setOverStatus(condition.getOverLoadFlag());
			}
			condition = editFxzf(condition);
			fxzfStaticService.findByPagerDetail(pager, condition);
			// HashMap<String, String> stationMap =
			// DictionaryUtil.dictionary("DETECTIONSTATION",
			// dataDictionaryService);
			HashMap<String, String> overLoadMap = DictionaryUtil.dictionary("OVERLOADSTATUS", dataDictionaryService);
			HashMap<String, String> punishMap = DictionaryUtil.dictionary("OVERLOADPUNISH", dataDictionaryService);
			for (FxzfStatic fxzfStatic : pager) {
				fxzfStatic.setOverStatus(overLoadMap.get(String.valueOf(fxzfStatic.getOverLoadStatus())));
				fxzfStatic.setOverPunish(punishMap.get(String.valueOf(fxzfStatic.getOverLoadPunish())));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}

	@ResponseBody
	@RequestMapping(value = "change", method = RequestMethod.POST)
	public AjaxMessage changeLicense(@RequestParam String license, @RequestParam String oldlicense, @RequestParam String id, @RequestParam String color) {
		try {
			logger.info("****修改前的车牌号：" + oldlicense + "****");
			// //更新过车信息表中的数据
			FxzfStatic fxzfStatic = fxzfStaticService.getById(id);
			fxzfStatic.setLicense(license);
			/************************************************************************
			 * \ ************************对新的车牌号流程的控制************************ \
			 * **********************************************************************/
			// 判断是否为白名单,1为白名单 返回true说明存在否则不存在
			logger.info(fxzfStatic.getLicense() + "判断是否为白名单");
			if (!writeRedisService.RedisExistsList(fxzfStatic.getLicense(), fxzfStatic.getLicenseColor(), 1)) {
				logger.info(fxzfStatic.getLicense() + "判断是否为黑名单");
				// 判断是否为白名单,0为黑名单 返回true说明存在否则不存在
				if (!writeRedisService.RedisExistsList(fxzfStatic.getLicense(), fxzfStatic.getLicenseColor(), 0)) {
					logger.info(fxzfStatic.getLicense() + "判断是否超限");
					// 判断是否超限,overLoadStatus >0 表示超限
					if (fxzfStatic.getOverLoadStatus() > 0) {
						logger.info(fxzfStatic.getLicense() + "同步至车辆轨迹表");
						this.illegalTrailService.syncIllegalTrail(fxzfStatic.getLicense(), fxzfStatic.getLicenseColor());
						logger.info(fxzfStatic.getLicense() + "判断是否已经符合黑名单,如果符合则更新黑名单并同步至redis中");
						if (this.illegalTrailService.IsAccordBlackList(fxzfStatic.getLicense(), fxzfStatic.getLicenseColor())) {
							this.blackListService.syncBlackList(fxzfStatic.getLicense(), fxzfStatic.getLicenseColor());
						}
					}
				} else {
					// 如果是黑名单
					logger.info(fxzfStatic.getLicense() + "判断是否超限");
					// 判断是否超限,overLoadStatus >0 表示超限
					if (fxzfStatic.getOverLoadStatus() > 0) {
						logger.info(fxzfStatic.getLicense() + "同步数据至车辆轨迹表");
						this.illegalTrailService.syncIllegalTrail(fxzfStatic.getLicense(), fxzfStatic.getLicenseColor());
						logger.info(fxzfStatic.getLicense() + "更新黑名单");
						this.blackListService.syncBlackList(fxzfStatic.getLicense(), fxzfStatic.getLicenseColor());
					}
				}
				// 判断是否超限,告警
				if (fxzfStatic.getWarnFlag() == 1) {
					// 将告警信息中对应的FXZF_ID的车牌号修改
					WarnHistory warn = new WarnHistory();
					warn.setFxzfId(fxzfStatic.getId());
					warn.setLicense(fxzfStatic.getLicense());
					warnHistoryService.update(warn);
				}
				// 更新过车轨迹表中的信息
				fxzfStaticService.update(fxzfStatic);
			} else {
				// 如果是白名单删除该记录
				fxzfStaticService.deleteById(fxzfStatic.getId());
			}

			/************************************************************************
			 * \ ************************对旧的车牌号流程的控制************************ \
			 * **********************************************************************/
			fxzfStatic.setLicense(oldlicense);
			// 判断是否为空车牌,如果为空车牌不做任何操作
			if (!StringUtil.isEmpty(fxzfStatic.getLicense())) {
				// 判断是否为黑名单
				if (!writeRedisService.RedisExistsList(fxzfStatic.getLicense(), fxzfStatic.getLicenseColor(), 0)) {
					// 不是黑名单,判断是否超限
					if (fxzfStatic.getOverLoadStatus() > 0) {
						// 过车轨迹中该车牌和车牌颜色对应的记录OVERLOAD是否为零
						IllegalTrail illegal = illegalTrailService.findByLicenseAndColor(fxzfStatic.getLicense(), fxzfStatic.getLicenseColor());
						if (illegal.getOverLoadTimes() == 1) {
							// 从过车轨迹中删除该条记录
							illegalTrailService.deleteById(illegal.getId());
						} else {
							// 将过车轨迹的违章次数减1,,同时跟新最后一次违章时间
							illegal.setOverLoadTimes(illegal.getOverLoadTimes() - 1);
							illegalTrailService.update(illegal);
						}
					}
				} else {
					// 是黑名单信息,判断是否为手动添加的黑名单
					BlackList black = new BlackList();
					if (fxzfStatic.getOverLoadStatus() > 0) {
						// 过车轨迹中该车牌和车牌颜色对应的记录OVERLOAD是否为零
						IllegalTrail illegal = illegalTrailService.findByLicenseAndColor(fxzfStatic.getLicense(), fxzfStatic.getLicenseColor());
						if (illegal.getOverLoadTimes() == 1) {
							// 从过车轨迹中删除该条记录
							illegalTrailService.deleteById(illegal.getId());
						} else {
							// 将过车轨迹的违章次数减1,,同时跟新最后一次违章时间
							illegal.setOverLoadTimes(illegal.getOverLoadTimes() - 1);
							illegalTrailService.update(illegal);
						}

						// 同步黑名单信息,是否人工添加(0为自动添加,1为人工添加)
						black.setLicense(fxzfStatic.getLicense());
						black.setPlateColor(fxzfStatic.getLicenseColor());
						black = blackListService.findByPlateAndColor(black);
						if (black.getAddByOperatorFlag() == 1) {
							// 人工添加
							black.setOverloadTimes(black.getOverloadTimes() - 1);
							blackListService.update(black);
						} else {
							// 自动添加
							// 判断黑名单次数是否超过黑名单阈值
							Config config = configService.getByCode(SysConstants.BLACK_LIST_OVERLOADS);
							if ((black.getOverloadTimes() - 1) > config.getValue()) {
								// 当减"1" > 黑名单阈值的时候，更改该名单阈值的大小
								black.setOverloadTimes(black.getOverloadTimes() - 1);
								blackListService.update(black);
							} else {
								// 当减"1" < 黑名单阈值的时候，更改该名单阈值的大小
								blackListService.deleteById(black.getId());
								// 删除redis中的黑名单
								writeRedisService.DeleteListToRedis(black.getId(), black.getLicense(), black.getPlateColor(), 0);
							}
						}
					}
				}
			}
			return AjaxMessage.success(200);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxMessage.success(1200);
		}
	}

	/**
	 * 编辑FXZF
	 * 
	 * @param condition
	 * @return
	 */
	public FxzfStatic editFxzf(FxzfStatic condition) {
		if (condition.getOverStatus() != null && !(condition.getOverStatus().equals("null"))) {
			String status[] = condition.getOverStatus().split(",");
			Integer[] data = new Integer[status.length];
			for (int i = 0; i < status.length; i++) {
				data[i] = Integer.valueOf(status[i]);
			}
			condition.setStatus(data);
		} else {
			condition.setOverStatus(null);
		}
		if (condition.getOverPunish() != null && !(condition.getOverPunish().equals("null"))) {
			String punishs[] = condition.getOverPunish().split(",");
			Integer[] data = new Integer[punishs.length];
			for (int i = 0; i < punishs.length; i++) {
				data[i] = Integer.valueOf(punishs[i]);
			}
			condition.setPunish(data);
		} else {
			condition.setOverPunish(null);
		}
		return condition;
	}

	@RequestMapping(value = "check", method = RequestMethod.GET)
	public String check(ModelMap map, @ModelAttribute("fxzfStatic") FxzfStatic fxzfStaticInfo, String flag) {
		fxzfStaticInfo = fxzfStaticService.getById(fxzfStaticInfo.getId());
		map.put("condition", fxzfStaticInfo);
		map.put("flag", flag);
		return "/fxzf/static/check";
	}

	/**
	 * 审核信息FxzfStatic
	 */
	@ResponseBody
	@RequestMapping(value = "check", method = RequestMethod.POST)
	public AjaxMessage checkFxzfMessage(@FormBean(value = "condition", modelCode = "fxzfStatic") FxzfStatic condition) {
		logger.info("--------------------信息审核--------------------");
		try {
			// 信息审核完成后更改信息
			// HashMap<String, String> reasonMap =
			// DictionaryUtil.dictionary("PUNISHREASON",dataDictionaryService);
			// if(condition.getPunishReason()!=null){
			// condition.setPunishReason(reasonMap.get(condition.getPunishReason()));
			// }
			this.systemLogService.log(MODULE_CODE, OperateType.check.getCode(), "过车信息列表", request.getRemoteAddr());
			fxzfStaticService.checkFxzfMessage(condition);
			if (condition.getPunishReason() != null) {
				// 审核不通过返回不通过原因
				HashMap<String, String> reasonMap = DictionaryUtil.dictionary("PUNISHREASON", dataDictionaryService);
				return AjaxMessage.success(reasonMap.get(condition.getPunishReason()));
			}
			if (condition.getPunishId() != null) {
				// 审核通过并且已经处罚
				return AjaxMessage.success(condition.getPunishId());
			}
			return AjaxMessage.success(condition.getId());
		} catch (Exception e) {
			return AjaxMessage.error("操作失败");
		}
	}

	@RequestMapping(value = "union", method = RequestMethod.GET)
	public String union(ModelMap map, @ModelAttribute("fxzfStatic") FxzfStatic fxzfStaticInfo, String flag) {
		fxzfStaticInfo = fxzfStaticService.getById(fxzfStaticInfo.getId());
		map.put("condition", fxzfStaticInfo);
		map.put("flag", flag);
		return "/fxzf/static/union";
	}

	/**
	 * 关联处罚信息FxzfStatic
	 */
	@ResponseBody
	@RequestMapping(value = "union", method = RequestMethod.POST)
	public AjaxMessage union(@FormBean(value = "condition", modelCode = "fxzfStatic") FxzfStatic condition) {
		logger.info("--------------------关联处罚信息--------------------");
		try {
			// 信息审核完成后更改信息
			this.systemLogService.log(MODULE_CODE, OperateType.union.getCode(), "过车信息列表", request.getRemoteAddr());
			condition.setOverLoadPunish(4);
			fxzfStaticService.checkFxzfMessage(condition);
			return AjaxMessage.success(condition.getPunishId() + ":" + condition.getId());
		} catch (Exception e) {
			return AjaxMessage.error("操作失败");
		}
	}

	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam(required = true) String id, ModelMap model, HttpServletRequest request) {
		FxzfStatic fxzfStatic = this.fxzfStaticService.getById(id);
		if (fxzfStatic.getLength() == -1) {
			fxzfStatic.setLength(null);
		}
		if (fxzfStatic.getWidth() == -1) {
			fxzfStatic.setWidth(null);
		}
		if (fxzfStatic.getHeight() == -1) {
			fxzfStatic.setHeight(null);
		}
		model.put("fxzf", fxzfStatic);
		String imageServer = (String) request.getSession().getAttribute("imageServer");
		model.put("imageServerAddr", imageServer);
		return "/fxzf/static/view";
	}

	@RequestMapping(value = "zfws", method = RequestMethod.GET)
	public String zfwh(@RequestParam(required = true) String id, ModelMap model) {
		// FxzfStatic fxzfStatic = this.fxzfStaticService.getById(id);

		return "/fxzf/static/zfws";
	}

	/**
	 * 获取违法程度
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "overLoadStatus", method = RequestMethod.POST)
	AjaxMessage getOverLoadStatus() {
		try {
			HashMap<String, String> stationMap = DictionaryUtil.dictionary("OVERLOADSTATUS", dataDictionaryService);
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

	/**
	 * 获取治超站数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "detectStation", method = RequestMethod.POST)
	AjaxMessage getDetectionStation() {
		HashMap<String, String> stationMap = DictionaryUtil.dictionary("DETECTIONSTATION", dataDictionaryService);
		List<String> stationList = new ArrayList<String>();
		for (String key : stationMap.keySet()) {
			stationList.add(key + ":" + stationMap.get(key));
		}
		return AjaxMessage.success(stationList);
		// ZczTree zczTree = zczTreeService.getById((long) 0);
		// List<ZczTree> zczTrees = zczTreeService.getAllNodes(zczTree);
		// for(ZczTree node:zczTrees){
		// List<ZczTree> zczTreeNodes = zczTreeService.getAllNodes(node);
		// node.setZczList(zczTreeNodes);
		// }
		// zczTree.setZczList(zczTrees);
		// return AjaxMessage.success(JSON.toJSON(zczTree));
	}

	/**
	 * 根据权限获取查询治超站信息
	 */
	public List<String> getStations() {
		HashMap<String, String> stationMap = DictionaryUtil.dictionary("DETECTIONSTATION", dataDictionaryService);
		List<String> stationList = new ArrayList<String>();

		Boolean isAdmin = false;
		Long jgid = null;
		// 根据人员来获取查询权限
		User user = (User) safetyManager.getAuthenticationInfo().getUser();
		List<Role> rolelist = this.roleService.findAssigned(user.getId());
		if (!CollectionUtils.isEmpty(rolelist)) {
			for (Role role : rolelist) {
				if ("ROLE_ADMIN".equals(role.getCode()))
					isAdmin = true;
			}
		}
		if (!isAdmin) {
			ExtPerson person = personService.getById(user.getPersonId());
			jgid = null != person ? person.getJgbh() : null;
			// 人员部分权限
			// 根据权限设置查询的治超站范围
			List<JgZcd> zcdList = jgZcdService.getByJgid(jgid);
			for (JgZcd jgZcd : zcdList) {
				stationList.add(jgZcd.getDeteStation() + ":" + stationMap.get(jgZcd.getDeteStation()));
			}
		} else {
			// 超级管理员权限
			// 所有的治超站权限
			for (String key : stationMap.keySet()) {
				stationList.add(key + ":" + stationMap.get(key));
			}
		}
		return stationList;
	}

	/**
	 * 获取处罚结果
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "overLoadPunish", method = RequestMethod.POST)
	AjaxMessage getOverLoadPunish() {
		try {
			HashMap<String, String> stationMap = DictionaryUtil.dictionary("OVERLOADPUNISH", dataDictionaryService);
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

	/**
	 * 导出一页Excel
	 * 
	 * @param request
	 * @param response
	 * @param pageNo
	 * @param condition
	 */
	@RequestMapping(value = "exportPage", method = RequestMethod.GET)
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, String pageNo, @FormBean(value = "condition", modelCode = "fxzfStatic") FxzfStatic condition) {
		logger.info("-------------------------导出本页excel列表---------------");
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(), "过车信息列表导出当前页", request.getRemoteAddr());
			condition = editFxzf(condition);
			if (condition.getLicense() != null) {
				condition.setLicense(java.net.URLDecoder.decode(condition.getLicense(), "utf-8"));
			}
			if (condition.getLicenseColor() != null) {
				condition.setLicenseColor(java.net.URLDecoder.decode(condition.getLicenseColor(), "utf-8"));
			}
			Pagination<FxzfStatic> pager = new Pagination<FxzfStatic>();
			pager.setPageNo(Integer.valueOf(pageNo));
			pager.setPageSize(15);
			Pageable<FxzfStatic> page = (Pagination<FxzfStatic>) fxzfStaticService.findByPager(pager, condition);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			String time = formatter.format(date);
			String title = "过车记录查询_" + time + ".xls";
			editExcel(page, response, title);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("-------------------------导出本页excel列表失败---------------");
		}

	}

	@RequestMapping(value = "exportAll", method = RequestMethod.GET)
	public void exportAll(HttpServletRequest request, HttpServletResponse response, String total, @FormBean(value = "condition", modelCode = "fxzfStatic") FxzfStatic condition) {
		logger.info("-------------------------导出全部excel列表---------------");
		try {
			this.systemLogService.log(MODULE_CODE, OperateType.export.getCode(), "过车信息列表导出全部", request.getRemoteAddr());
			if (condition.getLicense() != null) {
				condition.setLicense(java.net.URLDecoder.decode(condition.getLicense(), "utf-8"));
			}
			if (condition.getLicenseColor() != null) {
				condition.setLicenseColor(java.net.URLDecoder.decode(condition.getLicenseColor(), "utf-8"));
			}
			condition = editFxzf(condition);
			Pagination<FxzfStatic> pager = new Pagination<FxzfStatic>();
			pager.setPageNo(1);
			pager.setPageSize(Integer.MAX_VALUE);
			Pageable<FxzfStatic> page = (Pagination<FxzfStatic>) fxzfStaticService.findByPager(pager, condition);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			String time = formatter.format(date);
			String title = "实时过车记录查询_" + time + ".xls";
			editExcel(page, response, title);
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

	void editExcel(Pageable<FxzfStatic> pager, HttpServletResponse response, String title) {
		OutputStream out = null;
		try {
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(title.getBytes("GB2312"), "iso8859-1"));
			response.setContentType("application/msexcel;charset=UTF-8");
			out = response.getOutputStream();
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet(UtilTool.toGBK("过车数据查询"));
			HSSFRow top = sheet.createRow(0);
			HSSFRow row = sheet.createRow(1);
			HSSFCellStyle style1 = workbook.createCellStyle();
			HSSFCellStyle style2 = workbook.createCellStyle();
			HSSFCellStyle style3 = workbook.createCellStyle();

			/**************************************** 字体font *********************/
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

			// 设置表头长度
			for (int i = 0; i < 14; i++) {
				HSSFCell cell = top.createCell(i);
				cell.setCellStyle(style3);
			}
			// 设置表头长度
			top.getSheet().addMergedRegion(new Region(0, (short) 0, 0, (short) 13));
			HSSFCell celltop = top.createCell(0);
			celltop.setCellValue("实时过车数据");
			celltop.setCellStyle(style3);

			// HashMap<String, String> detectionMap =
			// DictionaryUtil.dictionary("DETECTIONSTATION",
			// dataDictionaryService);
			HashMap<String, String> punishMap = DictionaryUtil.dictionary("OVERLOADPUNISH", dataDictionaryService);
			HashMap<String, String> overLoadMap = DictionaryUtil.dictionary("OVERLOADSTATUS", dataDictionaryService);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String[] head = { "序号", "过车时间", "车牌号", "车牌颜色", "轴数", "总重（吨）", "超限量（吨）", "违法程度", "超限率", "车道", "长宽高", "处罚结果" };
			int i0 = 0, i1 = 0, i2 = 0, i3 = 0, i4 = 0, i5 = 0, i6 = 0, i7 = 0, i8 = 0, i9 = 0, i10 = 0, i11 = 0;
			for (int i = 0; i < head.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(head[i]);
				cell.setCellStyle(style1);
				if (i == 0) {
					i0 = head[i].length() * 256 + 256 * 10;
				}
				if (i == 1) {
					i1 = head[i].length() * 256 + 256 * 20;
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
				if (i == 6) {
					i6 = head[i].length() * 256 + 256 * 10;
				}
				if (i == 7) {
					i7 = head[i].length() * 256 + 256 * 10;
				}
				if (i == 8) {
					i8 = head[i].length() * 256 + 256 * 10;
				}
				if (i == 9) {
					i9 = head[i].length() * 256 + 256 * 10;
				}
				if (i == 10) {
					i10 = head[i].length() * 256 + 256 * 10;
				}
				if (i == 11) {
					i11 = head[i].length() * 256 + 256 * 10;
				}
				// if (i == 12) {
				// i12 = head[i].length() * 256 + 256 * 10;
				// }
				// if (i == 13) {
				// i13 = head[i].length() * 256 + 256 * 10;
				// }
			}

			for (int i = 0; i < pager.size(); i++) {
				row = sheet.createRow(i + 2);
				// 序号
				HSSFCell cell0 = row.createCell(0);
				cell0.setCellValue(1 + i);
				cell0.setCellStyle(style2);
				if (String.valueOf(1 + i).length() * 256 >= i0) {
					i0 = String.valueOf(1 + i).length() * 256 + 256 * 8;
				}
				// 治超站位置
				HSSFCell cell1 = row.createCell(1);
				cell1.setCellStyle(style1);
				// if (pager.get(i).getDetectStation() != null) {
				// cell1.setCellValue("绍大线兰亭治超站");
				// if
				// (detectionMap.get(pager.get(i).getDetectStation()).length() *
				// 156 >= i1) {
				// i1 =
				// detectionMap.get(pager.get(i).getDetectStation()).length() *
				// 156 + 156 * 30;
				// }
				// } else {
				// cell1.setCellValue("");
				// }
				//
				// // 治超站方向
				// HSSFCell cell2 = row.createCell(2);
				// cell2.setCellStyle(style2);
				// if (pager.get(i).getDetectStation() != null) {
				// cell2.setCellValue(detectionMap.get(pager.get(i).getDetectStation()));
				// if
				// (detectionMap.get(pager.get(i).getDetectStation()).length() *
				// 256 >= i2) {
				// i2 =
				// detectionMap.get(pager.get(i).getDetectStation()).length() *
				// 256 + 256 * 20;
				// }
				// } else {
				// cell2.setCellValue("");
				// }
				// // 过车时间
				HSSFCell cell3 = row.createCell(1);
				cell3.setCellStyle(style3);
				if (pager.get(i).getCaptureTime() != null) {
					cell3.setCellValue(format.format(pager.get(i).getCaptureTime()));
					if (format.format(pager.get(i).getCaptureTime()).length() * 356 >= i3) {
						i3 = format.format(pager.get(i).getCaptureTime()).length() * 356 + 356 * 10;
					}
				} else {
					cell3.setCellValue("");
				}
				// // 车牌号
				HSSFCell cell4 = row.createCell(2);
				cell4.setCellStyle(style2);
				if (pager.get(i).getLicense() != null) {
					cell4.setCellValue(pager.get(i).getLicense());
					if (pager.get(i).getLicense().length() * 256 >= i4) {
						i4 = pager.get(i).getLicense().length() * 256 + 256 * 10;
					}
				} else {
					cell4.setCellValue("");
				}
				// // 车牌颜色
				HSSFCell cell5 = row.createCell(3);
				cell5.setCellStyle(style2);
				if (pager.get(i).getLicenseColor() != null) {
					cell5.setCellValue(pager.get(i).getLicenseColor());
					if (pager.get(i).getLicenseColor().length() * 256 >= i5) {
						i5 = pager.get(i).getLicenseColor().length() * 256 + 256 * 8;
					}
				} else {
					cell5.setCellValue("");
				}
				// // 轴数
				HSSFCell cell6 = row.createCell(4);
				cell6.setCellStyle(style2);
				if (pager.get(i).getAxisCount() != null) {
					cell6.setCellValue(pager.get(i).getAxisCount());
				} else {
					cell6.setCellValue("");
				}
				// // 总重
				HSSFCell cell7 = row.createCell(5);
				cell7.setCellStyle(style2);
				if (pager.get(i).getWeight() != null) {
					cell7.setCellValue(pager.get(i).getWeight());
				} else {
					cell7.setCellValue("");
				}
				// // 超限量（吨）
				HSSFCell cell8 = row.createCell(6);
				cell8.setCellStyle(style2);
				if (pager.get(i).getOverLoad() != null) {
					cell8.setCellValue(pager.get(i).getOverLoad());
				} else {
					cell8.setCellValue("");
				}
				// // 违法程度
				HSSFCell cell9 = row.createCell(7);
				cell9.setCellStyle(style2);
				if (pager.get(i).getOverLoadPercent() != null) {
					cell9.setCellValue(overLoadMap.get(String.valueOf(pager.get(i).getOverLoadStatus())));
				} else {
					cell9.setCellValue("");
				}
				// // 超限率
				HSSFCell cell10 = row.createCell(8);
				cell10.setCellStyle(style2);
				if (pager.get(i).getOverLoadPercent() != null) {
					cell10.setCellValue(pager.get(i).getOverLoadPercent());
				} else {
					cell10.setCellValue("");
				}
				// // 车道
				HSSFCell cell11 = row.createCell(9);
				cell11.setCellStyle(style2);
				if (pager.get(i).getLane() != null) {
					cell11.setCellValue(pager.get(i).getLane());
				} else {
					cell11.setCellValue("");
				}
				// // 长宽高
				HSSFCell cell12 = row.createCell(10);
				cell12.setCellStyle(style2);
				if (pager.get(i).getOverLoadPunish() != null) {
					String length = null;
					if (pager.get(i).getLength() == null || String.valueOf(pager.get(i).getLength()).equals("null")) {
						length = "";
					} else {
						length = String.valueOf(pager.get(i).getLength());
					}
					String width = null;
					if (pager.get(i).getWidth() == null || String.valueOf(pager.get(i).getWidth()).equals("null")) {
						width = "";
					} else {
						width = String.valueOf(pager.get(i).getWidth());
					}
					String height = null;
					if (pager.get(i).getHeight() == null || String.valueOf(pager.get(i).getHeight()).equals("null")) {
						height = "";
					} else {
						height = String.valueOf(pager.get(i).getHeight());
					}
					cell12.setCellValue(length + "*" + width + "*" + height);
					if (punishMap.get(String.valueOf(pager.get(i).getOverLoadPunish())).length() * 256 >= i10) {
						i10 = punishMap.get(String.valueOf(pager.get(i).getOverLoadPunish())).length() * 256 + 256 * 12;
					}
				} else {
					cell12.setCellValue("");
				}
				// // 处罚结果
				HSSFCell cell13 = row.createCell(11);
				cell13.setCellStyle(style2);
				if (pager.get(i).getOverLoadPunish() != null) {
					cell13.setCellValue(punishMap.get(String.valueOf(pager.get(i).getOverLoadPunish())));
				} else {
					cell13.setCellValue("");
				}
			}
			sheet.setColumnWidth(0, i0);
			sheet.setColumnWidth(1, i1);
			sheet.setColumnWidth(2, i2);
			sheet.setColumnWidth(3, i3);
			sheet.setColumnWidth(4, i4);
			sheet.setColumnWidth(5, i5);
			sheet.setColumnWidth(6, i6);
			sheet.setColumnWidth(7, i7);
			sheet.setColumnWidth(8, i8);
			sheet.setColumnWidth(9, i9);
			sheet.setColumnWidth(10, i10);
			sheet.setColumnWidth(11, i11);
			// sheet.setColumnWidth(12, i12);
			// sheet.setColumnWidth(13, i13);
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("---------------数据导出失败--------");
			e.printStackTrace();
		}
	}
}
