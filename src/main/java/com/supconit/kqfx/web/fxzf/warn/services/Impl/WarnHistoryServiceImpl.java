package com.supconit.kqfx.web.fxzf.warn.services.Impl;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.business.dic.services.DataDictionaryService;
import hc.orm.AbstractBasicOrmService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.supconit.kqfx.web.analysis.entities.JgZcd;
import com.supconit.kqfx.web.analysis.services.JgZcdService;
import com.supconit.kqfx.web.fxzf.avro.qbbItemInfo;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardData;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardInfo;
import com.supconit.kqfx.web.fxzf.avro.redis.ChargeRedisService;
import com.supconit.kqfx.web.fxzf.avro.redis.ReadRedisService;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.msg.services.MsgService;
import com.supconit.kqfx.web.fxzf.qbb.entities.QbbBfxx;
import com.supconit.kqfx.web.fxzf.qbb.services.QbbBfxxService;
import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.fxzf.warn.daos.WarnHistoryDao;
import com.supconit.kqfx.web.fxzf.warn.entities.Config;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnHistory;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnInfo;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnHistoryService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnInfoService;
import com.supconit.kqfx.web.util.DateUtil;
import com.supconit.kqfx.web.util.EncodeTransforUtil;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.SysConstants;

@Service("taizhou_offsite_enforcement_warnHistory_service")
public class WarnHistoryServiceImpl extends AbstractBasicOrmService<WarnHistory, String> implements WarnHistoryService {

    //治超站数据字典项code
    private static final String DETECT_CODE = "DETECTIONSTATION";
    //治超站数据字典项code
    private static final String OVERLOADSTATUS_CODE = "OVERLOADSTATUS";

    @Autowired
    private WarnHistoryDao warnHistoryDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private WarnInfoService warnInfoService;

    @Autowired
    private QbbBfxxService qbbBfxxService;

    @Autowired
    private WriteRedisService writeRedisService;

    @Autowired
    private ReadRedisService readRedisService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private JgZcdService jgZcdService;

    @Autowired
    private ChargeRedisService chargeRedisService;

    @Value("${qbb.circle.time}")
    private long circleTime;

    @Override
    public Pageable<WarnHistory> findByPager(Pageable<WarnHistory> pager, WarnHistory condition) {
        return warnHistoryDao.findByPager(pager, condition);
    }

    @Override
    public WarnHistory getById(String id) {
        return this.warnHistoryDao.getById(id);
    }

    @Override
    public void insert(WarnHistory entity) {
        this.warnHistoryDao.insert(entity);
    }

    @Override
    public void update(WarnHistory entity) {
        this.warnHistoryDao.update(entity);
    }

    @Override
    public void delete(WarnHistory entity) {
        this.warnHistoryDao.delete(entity);
    }

    @Override
    public Boolean IsPublishWarnMessage(String license, String plateColor) {
        double againTime = this.configService.getValueByCode(SysConstants.WARN_AGAIN_TIME);
        if (0 != againTime) {
            List<WarnHistory> result = this.warnHistoryDao.findAgainTimeData(license, plateColor, againTime);
            return !CollectionUtils.isEmpty(result) ? true : false;
        } else {
            return false;
        }
    }

    /**
     * 发布告警信息到情报板
     */
    @Override
    @Transactional
    public AjaxMessage publishToQbb(Fxzf fxzf) {
        try {
            //数据库入库预警信息
            if (!StringUtil.isEmpty(fxzf.getDetectStation())) {
                WarnInfo warnInfo = new WarnInfo();
                warnInfo.setDetectStation(fxzf.getDetectStation());
                warnInfo.setWarnType(fxzf.getWarnType());
                warnInfo = this.warnInfoService.getByTempletTypeAndStation(warnInfo);

                //获取系统默认情报板停留时间
                Config config = this.configService.getByCode(SysConstants.QBB_STAY_TIME);
                if (null != warnInfo) {
                    WarnHistory warnHistory = new WarnHistory();
                    warnHistory.setFxzfId(fxzf.getId());
                    warnHistory.setDeleted(0);
                    warnHistory.setLicense(fxzf.getLicense());
                    warnHistory.setPlateColor(fxzf.getLicenseColor());
                    //设置为情报板报警
                    warnHistory.setWarnType(1);
                    warnHistory.setWarnTime(fxzf.getCaptureTime());
                    warnHistory.setWarnInfo(EncodeTransforUtil.getXxnrResult(this.getWarnInfo(fxzf, warnInfo), null));//根据告警模板匹配信息并截取信息
                    warnHistory.setId(IDGenerator.idGenerator());
                    this.warnHistoryDao.insert(warnHistory);

                    QbbBfxx qbbBfxx = new QbbBfxx();
                    qbbBfxx.setId(IDGenerator.idGenerator());
                    qbbBfxx.setXxnr(this.getWarnInfo(fxzf, warnInfo));//根据告警模板匹配信息
                    qbbBfxx.setCzfs(warnInfo.getQbbmbxx().getCzfs());
                    qbbBfxx.setFont(warnInfo.getQbbmbxx().getFont());
                    qbbBfxx.setColor(warnInfo.getQbbmbxx().getColor());
                    //默认循环时间缺省值
                    qbbBfxx.setCircleTime(circleTime);
                    //默认停留时间缺省值
                    qbbBfxx.setRemainTime(config.getValue().longValue());
                    qbbBfxx.setPublishTime(new Date());
                    qbbBfxx.setLocation(fxzf.getDetectStation());
                    //自动发布
                    qbbBfxx.setType("0");
                    qbbBfxx.setDeleted(0);
                    /** 获取情报板实时内容,目前无法进行读取情报板 */
                    redis_BoardData boardData = this.readRedisService.ReadBoardData(fxzf.getDetectStation());
                    /** 插入情报板播放列表中 */
                    this.qbbBfxxService.insert(qbbBfxx);
                    redis_BoardInfo boardInfo = new redis_BoardInfo();
                    List<qbbItemInfo> itemList = new ArrayList<qbbItemInfo>();
                    itemList.add(QbbBfxx.translateToQbbItem(qbbBfxx));
                    boardInfo.setSQbbItems(itemList);
                    this.writeRedisService.WriteBoardInfo(boardInfo, fxzf.getDetectStation());
                    // 将实时播放内容保存到 BoardData_Prefix = "QBB_INFO_01/2";中便于读取
                    redis_BoardData boardData2 = new redis_BoardData();
                    boardData2.setSQbb(qbbBfxx.getXxnr());
                    this.writeRedisService.WriteBoardData(boardData2, fxzf.getDetectStation());
                    // 回归之前获取情报板内容
                    this.qbbBfxxService.restoreLastQbb(config.getValue().longValue(), null != boardData ? String.valueOf(boardData.getSQbb()) : "", fxzf.getDetectStation(), 0);
                }
            }
            return AjaxMessage.success();
        } catch (Exception e) {
            return AjaxMessage.error(e.toString());
        }
        //写入情报板信息至redis中
    }

    @Override
    @Transactional
    public AjaxMessage publishToWeb(Fxzf fxzf) {
        //数据库入库预警信息
        try {
            if (!StringUtil.isEmpty(fxzf.getDetectStation()) && !StringUtil.isEmpty(fxzf.getLicense())) {
                WarnInfo warnInfo = new WarnInfo();
                warnInfo.setDetectStation(fxzf.getDetectStation());
                warnInfo.setWarnType(fxzf.getWarnType());
                warnInfo = this.warnInfoService.getByTempletTypeAndStation(warnInfo);
                Config config = this.configService.getByCode(SysConstants.WEB_WARN_TEMPLET);
                if (null != warnInfo) {
                    //根据配置项设置网页告警模板内容(设置warnInfo使getWarnInfo取值TempletInfo)
                    warnInfo.setQbbTempletId(null);
                    warnInfo.setQbbmbxx(null);
                    warnInfo.setTempletInfo(null != config ? config.getName() : "");
                    WarnHistory warnHistory = new WarnHistory();
                    Date date = new Date();
                    warnHistory.setFxzfId(fxzf.getId());
                    warnHistory.setDeleted(0);
                    warnHistory.setLicense(fxzf.getLicense());
                    warnHistory.setPlateColor(fxzf.getLicenseColor());
                    //设置为WEB报警
                    warnHistory.setWarnType(2);
                    warnHistory.setWarnTime(date);
                    warnHistory.setWarnInfo(this.getWarnInfo(fxzf, warnInfo));//模糊匹配WEB告警信息
                    warnHistory.setId(IDGenerator.idGenerator());
                    this.warnHistoryDao.insert(warnHistory);
                    JgZcd jgZcd = this.jgZcdService.getByZczId(fxzf.getDetectStation());
                    //判断过车信息中的车辆是否为黑名单
                    String key = "B_" + fxzf.getLicense() + "_" + fxzf.getLicenseColor();
                    boolean flagKey = chargeRedisService.chargeKey(key);
                    if (flagKey) {
                        this.msgService.insertWarnInfo(warnHistory.getId(), "0", warnHistory.getWarnTime(), "黑名单告警：" + warnHistory.getWarnInfo(), String.valueOf(jgZcd.getJgid()));
                    } else {
                        this.msgService.insertWarnInfo(warnHistory.getId(), "0", warnHistory.getWarnTime(), warnHistory.getWarnInfo(), String.valueOf(jgZcd.getJgid()));
                    }
                }
            }
            return AjaxMessage.success("WEB自动告警成功");
        } catch (Exception e) {
            return AjaxMessage.error(e.toString());
        }
    }


    /**
     * 通过正则将表达式中字段匹配成对象属性
     *
     * @param fxzf
     * @param warnInfo
     * @return
     */
    @Override
    public String getWarnInfo(Fxzf fxzf, WarnInfo warnInfo) {
        String result = "";
        if (null != fxzf && null != warnInfo) {
            String para = "";
            if (StringUtil.isEmpty(warnInfo.getQbbTempletId()) || null == warnInfo.getQbbmbxx()) {
                para = warnInfo.getTempletInfo().replace("+", "");
            } else {
                para = warnInfo.getQbbmbxx().getXxnr().replace("+", "");
            }
            if (!StringUtil.isEmpty(para)) {
                String regEx = "@([\\s\\S]*?)@";
                Pattern pat = Pattern.compile(regEx);
                Matcher mat = pat.matcher(para);

                while (mat.find()) {
                    System.out.println(mat.group());
                    Map<String, Object> map = this.getMap(fxzf);
                    para = para.replaceAll(mat.group(), String.valueOf(map.get(mat.group())));
                }
                result = para;
            }
        }
        return result;
    }

    /**
     * 将fxzf转换成map
     *
     * @param fxzf
     * @return
     */
    public Map<String, Object> getMap(Fxzf fxzf) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("@车牌号@", null != fxzf.getLicense() ? fxzf.getLicense() : "");
        map.put("@治超站@", null != fxzf.getDetectStation() ? this.dataDictionaryService.getByDataCode(DETECT_CODE, fxzf.getDetectStation()).getName() : "");
        map.put("@过车时间@", null != fxzf.getCaptureTime() ? DateUtil.formatDate(fxzf.getCaptureTime(), DateUtil.DATE_FORMAT_TIME_T) : "");
        map.put("@总重@", null != fxzf.getWeight() ? fxzf.getWeight() : "");
        map.put("@轴数@", null != fxzf.getAxisCount() ? fxzf.getAxisCount() : "");
        map.put("@超限率@", null != fxzf.getOverLoadPercent() ? fxzf.getOverLoadPercent() : "");
        map.put("@违法等级@", null != fxzf.getOverLoadStatus() ? this.dataDictionaryService.getByDataCode(OVERLOADSTATUS_CODE, String.valueOf(fxzf.getOverLoadStatus())).getName() : "");
        return map;
    }

    @Override
    public List<WarnHistory> AnalysisDayWarnByCondition(WarnHistory condition) {
        return this.warnHistoryDao.AnalysisDayWarnByCondition(condition);
    }

    @Override
    public Pageable<WarnHistory> findByPagerExport(Pageable<WarnHistory> pager, WarnHistory condition) {
        return this.warnHistoryDao.findByPagerExport(pager, condition);
    }

    @Override
    public List<WarnHistory> getWarnByFxzfId(String fxzfid) {
        return this.warnHistoryDao.getWarnByFxzfId(fxzfid);
    }

    @Override
    public WarnHistory getWarnHistoryQbb(Fxzf warn) {
        return this.warnHistoryDao.getWarnHistoryQbb(warn);
    }
}
