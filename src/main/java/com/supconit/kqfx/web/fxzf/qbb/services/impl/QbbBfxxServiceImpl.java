package com.supconit.kqfx.web.fxzf.qbb.services.impl;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.business.dic.domains.FlatData;
import hc.business.dic.entities.Data;
import hc.business.dic.services.DataDictionaryService;
import hc.orm.AbstractBasicOrmService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.supconit.kqfx.web.fxzf.avro.qbbItemInfo;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardData;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardInfo;
import com.supconit.kqfx.web.fxzf.avro.redis.ReadRedisService;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.qbb.daos.QbbBfxxDao;
import com.supconit.kqfx.web.fxzf.qbb.entities.QbbBfxx;
import com.supconit.kqfx.web.fxzf.qbb.services.QbbBfxxService;
import com.supconit.kqfx.web.fxzf.qbb.services.RestoreQbb;
import com.supconit.kqfx.web.fxzf.warn.entities.Config;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnHistoryService;
import com.supconit.kqfx.web.util.DateUtil;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.SysConstants;
import com.supconit.kqfx.web.util.ThreadUtil;

@Service("fxzf_qbb_qbbbfxx_service")
public class QbbBfxxServiceImpl extends AbstractBasicOrmService<QbbBfxx, String> implements QbbBfxxService {

    private static final String DIC_CODE_CZFS = "QBB_WAY";
    private static final String DIC_CODE_ZT = "QBB_FONT";
    private static final String DIC_CODE_YS = "QBB_COLOR";
    private static final String DIC_DETECT = "QBB_STATIONNAME";

    @Autowired
    private QbbBfxxDao qbbBfxxDao;

    @Autowired
    private WriteRedisService writeRedisService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private ReadRedisService readRedisService;

    @Autowired
    private WarnHistoryService warnHistoryService;

    @Autowired
    private ConfigService configService;

    @Value("${qbb.circle.time}")
    private long circleTime;

    @Override
    public QbbBfxx getById(String id) {
        return this.getById(id);
    }

    @Override
    public void save(QbbBfxx entity) {
        if (StringUtil.isEmpty(entity.getId())) {
            this.qbbBfxxDao.insert(entity);
        } else {
            this.qbbBfxxDao.update(entity);
        }
    }

    @Override
    public void insert(QbbBfxx entity) {
        this.qbbBfxxDao.insert(entity);
    }

    @Override
    public void update(QbbBfxx entity) {
        this.qbbBfxxDao.update(entity);
    }

    @Override
    public void delete(QbbBfxx entity) {
        this.qbbBfxxDao.delete(entity);
    }

    @Override
    public Pageable<QbbBfxx> find(Pagination<QbbBfxx> pager, QbbBfxx condition) {
        return this.qbbBfxxDao.find(pager, condition);
    }

    /**
     * 回复默认情报板内容
     *
     * @param location
     * @return
     */
    public QbbBfxx restore(String location) {
        FlatData wayData = (FlatData) dataDictionaryService.getByCode(DIC_CODE_CZFS);
        FlatData colorData = (FlatData) dataDictionaryService.getByCode(DIC_CODE_YS);
        FlatData ztData = (FlatData) dataDictionaryService.getByCode(DIC_CODE_ZT);
        QbbBfxx qbbBfxx = new QbbBfxx();
        qbbBfxx.setId(IDGenerator.idGenerator());
        qbbBfxx.setXxnr(this.qbbXxnr(location));//配置文件中默认情报板信息
        qbbBfxx.setCzfs(!CollectionUtils.isEmpty(wayData) ? wayData.get(0).getCode() : "");
        qbbBfxx.setColor(!CollectionUtils.isEmpty(colorData) ? colorData.get(0).getCode() : "");
        qbbBfxx.setFont(!CollectionUtils.isEmpty(ztData) ? ztData.get(0).getCode() : "");
        //默认轮询时间
        qbbBfxx.setCircleTime(circleTime);
        //默认停留时间缺省值
        qbbBfxx.setPublishTime(new Date());
        qbbBfxx.setLocation(location);
        //自动发布
        qbbBfxx.setType("2");
        qbbBfxx.setDeleted(0);
        return qbbBfxx;
    }

    @Override
    @Transactional
    public void publishToQbb(List<QbbBfxx> bfxxs, String location, Long remainTime) {
        redis_BoardInfo boardInfo = new redis_BoardInfo();
        List<qbbItemInfo> itemList = new ArrayList<qbbItemInfo>();
        redis_BoardData boardData = new redis_BoardData();
        if (!CollectionUtils.isEmpty(bfxxs)) {
            /**
             * 将播放信息插入到播放列表中
             */
            for (QbbBfxx qbbBfxx : bfxxs) {
                itemList.add(QbbBfxx.translateToQbbItem(qbbBfxx));
                this.qbbBfxxDao.insert(qbbBfxx);
            }
            /**
             * 更新最后一次发布情报板内容,并保存在T_CONFIG表中
             */
            QbbBfxx lastBfxx = bfxxs.get(bfxxs.size() - 1);
            Config config = configService.getByCode(getConfigCode(lastBfxx.getLocation()));
            config.setName(lastBfxx.getXxnr());
            configService.update(config);
            //将最后一条播放信息插入到QBB_INFO_01/2中表示实时播放信息
            boardData.setSQbb(lastBfxx.getXxnr());
            boardData.setSQbbName(lastBfxx.getLocation());
        }
        /**
         * 将播放列表写入到redis中
         */
        boardInfo.setSQbbName(location);
        boardInfo.setSQbbItems(itemList);
        this.writeRedisService.WriteBoardInfo(boardInfo, location);

        /**
         * 将实时播放信息写入到redis的变量中QBB_INFO_01或QBB_INFO_02
         */
        this.writeRedisService.WriteBoardData(boardData, location);

        /**
         * 恢复默认情报板内容
         */
        this.restoreLastQbb(remainTime, this.qbbXxnr(location), location, 2);
    }

    @Override
    public List<QbbBfxx> getByCondition(QbbBfxx condition) {
        return this.qbbBfxxDao.getByCondition(condition);
    }


    /**
     * 根据治超点获取默认情报板信息
     *
     * @param detectStation
     * @return
     */
    public String qbbXxnr(String detectStation) {
        Integer locationNum = 0;
        if (detectStation.equals("QBB_WY_01")){
            locationNum = 1;
        }else if(detectStation.equals("QBB_WY_02")){
            locationNum = 2;
        }else if(detectStation.equals("QBB_DT_01")){
            locationNum = 3;
        }else if(detectStation.equals("QBB_DT_02")){
            locationNum = 4;
        }
        String[] qbbXxnr = new String[5];
        Config config1 = this.configService.getByCode(SysConstants.LT_SX_QBB_XXNR);
        Config config2 = this.configService.getByCode(SysConstants.LT_SX_QBB_XXNR2);
        Config config11 = this.configService.getByCode(SysConstants.LT_ZJ_QBB_XXNR);
        Config config22 = this.configService.getByCode(SysConstants.LT_ZJ_QBB_XXNR2);
        qbbXxnr[0] = null != config1 ? config1.getName() : "";
        qbbXxnr[1] = null != config2 ? config2.getName() : "";
        qbbXxnr[2] = null != config11 ? config11.getName() : "";
        qbbXxnr[3] = null != config22 ? config22.getName() : "";
        return qbbXxnr[locationNum - 1];
    }

    /**
     * 根据治超点获取最近一次情报板发布信息
     *
     * @param detectStation
     * @return
     */
    public String lastQbbXxnr(String detectStation) {
        Integer locationNum = 0;
        if (detectStation.equals("QBB_WY_01")){
            locationNum = 1;
        }else if(detectStation.equals("QBB_WY_02")){
            locationNum = 2;
        }else if(detectStation.equals("QBB_DT_01")){
            locationNum = 3;
        }else if(detectStation.equals("QBB_DT_02")){
            locationNum = 4;
        }
        String[] qbbXxnr = new String[5];
        Config config1 = this.configService.getByCode(SysConstants.LT_SX_QBB_XXNR_LAST);
        Config config2 = this.configService.getByCode(SysConstants.LT_SX_QBB_XXNR_LAST2);
        Config config11 = this.configService.getByCode(SysConstants.LT_ZJ_QBB_XXNR_LAST);
        Config config22 = this.configService.getByCode(SysConstants.LT_ZJ_QBB_XXNR_LAST2);
        qbbXxnr[0] = null != config1 ? config1.getName() : "";
        qbbXxnr[1] = null != config2 ? config2.getName() : "";
        qbbXxnr[2] = null != config11 ? config11.getName() : "";
        qbbXxnr[3] = null != config22 ? config22.getName() : "";
        return qbbXxnr[locationNum - 1];
    }

    /**
     * 回复上次情报板发布的内容，系统启动时恢复情报板信息
     *
     * @param remainTime 保留时间
     * @param redis_xxnr 信息内容
     * @param location   治超站标志
     * @param type       自动还是手动发布信息
     */
    @Override
    public void restoreLastQbb(Long remainTime, String redis_xxnr, String location, Integer type) {

        if (!StringUtil.isEmpty(location)) {
            //情报板发布前实时内容redis_xxnr
            if (StringUtil.isEmpty(redis_xxnr)) {
                redis_BoardData boardData = this.readRedisService.ReadBoardData(location);
                redis_xxnr = null != boardData ? String.valueOf(boardData.getSQbb()) : "";
            }
            QbbBfxx qbbbfxx0 = new QbbBfxx();
            qbbbfxx0.setLocation(location);
            qbbbfxx0.setXxnr(redis_xxnr);
            /**
             *判断情报板内容是否为告警信息
             */
            List<QbbBfxx> warnHistories = this.IsWarnTypeHistrory(qbbbfxx0);
            /**
             * 线程中断,
             * 这里是中断所有治超站的恢复线程
             */
            Thread[] list = ThreadUtil.findAllThreads();
            for (Thread thread : list) {
                if (("restoreQbb" + location).equals(thread.getName())) {
                    if (thread.isAlive()) {
                        thread.interrupt();
                    }
                }
            }

            /**
             * 获取情报板停留时间
             */
            Config config = this.configService.getByCode(SysConstants.QBB_STAY_TIME);
            /**
             * 获取默认情报板内容
             */
            List<QbbBfxx> bfxxList = new ArrayList<QbbBfxx>();
            QbbBfxx qbbbfxx = this.restore(location);
            bfxxList.add(qbbbfxx);
            /**
             * 判断情报板实时内容是否为系统发布告警情报板内容
             * 如是则恢复之前情报板手动发布的最后一条内容,否则恢复情报板默认内容
             */
            if (!CollectionUtils.isEmpty(warnHistories) || type == 0) {
                //设置之前情报板内容
                qbbbfxx0.setXxnr(this.lastQbbXxnr(location));
                //匹配之前发布的情报板内容并计算停留时间
                List<QbbBfxx> bfxxList2 = this.IsPublishTypeHistrory(qbbbfxx0, remainTime);
                Thread[] list2 = ThreadUtil.findAllThreads();
                /**
                 * 杀死线程
                 */
                for (Thread thread : list2) {
                    if ("restoreQbb".equals(thread.getName())) {
                        if (thread.isAlive()) {
                            thread.interrupt();
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(bfxxList2)) {
                    //恢复之前情报板继续播放
                    new Thread(new RestoreQbb(null != remainTime ? remainTime : config.getValue().longValue(), bfxxList2), "restoreQbb" + location).start();
                    new Thread(new RestoreQbb(null != remainTime ? remainTime + bfxxList2.get(0).getRemainTime() : config.getValue().longValue() + bfxxList2.get(0).getRemainTime(), bfxxList), "restoreQbb" + location).start();
                    return;
                } else {
                    //如果情报板已完成播放则恢复默认情报板
                    new Thread(new RestoreQbb(null != remainTime ? remainTime : config.getValue().longValue(), bfxxList), "restoreQbb" + location).start();
                    return;
                }
            }
            //判断是否需要停留,启动线程发布恢复情报板
            new Thread(new RestoreQbb(null != remainTime ? remainTime : config.getValue().longValue(), bfxxList), "restoreQbb" + location).start();
            return;
        }
    }

    /**
     * @param qbbbfxx
     * @param remainTime
     * @return
     */
    private List<QbbBfxx> IsPublishTypeHistrory(QbbBfxx qbbbfxx, Long remainTime) {
        List<QbbBfxx> qbbbfxxs = this.qbbBfxxDao.IsPublishTypeHistrory(qbbbfxx);
        if (!CollectionUtils.isEmpty(qbbbfxxs)) {
            Config config = this.configService.getByCode(SysConstants.QBB_STAY_TIME);
            for (QbbBfxx qbbBfxx2 : qbbbfxxs) {
                qbbBfxx2.setId(IDGenerator.idGenerator());
                Date publishTime = DateUtil.addMins(new Date(), null != remainTime ? remainTime.intValue() : config.getValue().intValue());
                int newRemainTime = DateUtil.betweenMins(qbbBfxx2.getPublishTime(), qbbBfxx2.getRemainTime().intValue(), publishTime);
                if (newRemainTime > 0) {
                    qbbBfxx2.setPublishTime(publishTime);
                    qbbBfxx2.setRemainTime((long) newRemainTime);
                } else {
                    return null;
                }
            }
            return qbbbfxxs;
        }
        return null;
    }

    @Override
    public List<QbbBfxx> IsWarnTypeHistrory(QbbBfxx qbbbfxx) {
        return this.qbbBfxxDao.IsWarnTypeHistrory(qbbbfxx);
    }

    @Override
    public void initLastQbb() {
        /**
         * 系统启动前,获取各个治超站上次发布的情报板
         */
        FlatData detectData = (FlatData) this.dataDictionaryService.getByCode(DIC_DETECT);
        for (Data data : detectData) {
            this.restoreLastQbb(0L, null, data.getCode(), 0);
        }
    }


    public String getConfigCode(String location) {
        Integer locationNum = 0;
        if (location.equals("QBB_WY_01")){
            locationNum = 1;
        }else if(location.equals("QBB_WY_02")){
            locationNum = 2;
        }else if(location.equals("QBB_DT_01")){
            locationNum = 3;
        }else if(location.equals("QBB_DT_02")){
            locationNum = 4;
        }
        String[] code = new String[5];
        code[0] = SysConstants.LT_SX_QBB_XXNR_LAST;
        code[1] = SysConstants.LT_SX_QBB_XXNR_LAST2;
        code[2] = SysConstants.LT_ZJ_QBB_XXNR_LAST;
        code[3] = SysConstants.LT_ZJ_QBB_XXNR_LAST2;
        if (!StringUtil.isEmpty(location)) {
            return code[locationNum - 1];
        } else {
            return null;
        }
    }

    @Override
    public QbbBfxx getByBfxxId(String id) {
        return qbbBfxxDao.getByBfxxId(id);
    }
}

