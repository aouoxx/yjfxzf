package com.supconit.kqfx.web.fxzf.search.services.Impl;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicOrmService;

import java.util.Date;
import java.util.List;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardData;
import com.supconit.kqfx.web.fxzf.avro.redis.ReadRedisService;
import com.supconit.kqfx.web.fxzf.monitor.Dwr.FxzfPush;
import com.supconit.kqfx.web.fxzf.search.daos.FxzfMonitorDao;
import com.supconit.kqfx.web.fxzf.search.daos.FxzfSearchDao;
import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfLane;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfMonitor;
import com.supconit.kqfx.web.fxzf.search.services.FxzfLaneService;
import com.supconit.kqfx.web.fxzf.search.services.FxzfSearchService;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnHistory;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnHistoryService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnInfoService;


@Service("taizhou_offsite_enforcement_searchFxzf_service")
public class FxzfSearchServiceImpl extends AbstractBasicOrmService<Fxzf, String>
   implements FxzfSearchService {

	private static final Logger logger = LoggerFactory
			.getLogger(FxzfSearchServiceImpl.class);
	@Autowired
	private FxzfSearchDao dataSearchDao;
	
	@Autowired
	private ConfigService	configService; 
	
	@Autowired
	private ReadRedisService readRedisService;
	
	@Autowired
	private WarnInfoService	warnInfoService;
	
	@Autowired
	private WarnHistoryService	warnHistoryService;
	
	@Autowired 
	private FxzfMonitorDao fxzfMonitorDao;
	
	@Autowired
	private FxzfLaneService fxzfLaneService;
	
	@Override
	public Pageable<Fxzf> findByPager(Pageable<Fxzf> pager, Fxzf condition) {
		return dataSearchDao.findByPager(pager, condition);
	}

	@Override
	public Fxzf getById(String arg0) {
		return dataSearchDao.getById(arg0);
	}

	@Override
	public void insert(Fxzf entity) {
		/**
		 * 如果车头车牌图片和车尾车牌图片都不存在则图片不存在
		 */
		logger.info("================判断车牌是否非空,设置标志为=====================");
		if(StringUtil.isEmpty(entity.getHeadLicensePicdir())&&StringUtil.isEmpty(entity.getTailLicensePicdir())){
			entity.setPicFlag(1);//图片不存在
		}else{
			entity.setPicFlag(0);//图片存在
		}
		/**
		 * 如果违章车辆没有车牌号，则不信息推送
		 */
		//车牌号不为空
		if(!StringUtil.isEmpty(entity.getLicense())){
			/**
			 * 获取情报板播放内容
			 */
			logger.info("================车牌号非空,进行信息推送=====================");
			if(entity.getOverLoadStatus()>0){
				if(!StringUtil.isEmpty(entity.getDetectStation())&&StringUtil.isEmpty(entity.getXxnr())){
					redis_BoardData reisBoardData = this.readRedisService.ReadBoardData(entity.getDetectStation());
					entity.setXxnr(null!=reisBoardData?String.valueOf(reisBoardData.getSQbb()):"");
				}
				/**
				 * 判断告警信息表中是否有该告警信息中是否含有该车牌
				 */
				WarnHistory warnHistory = warnHistoryService.getWarnHistoryQbb(entity);
				if(warnHistory!=null){
					entity.setXxnr(warnHistory.getWarnInfo());
				}
				/**
				 * 车辆超限时进行信息推送
				 */
				FxzfPush.send(entity.getDetectStation(),JSONObject.toJSONString(entity));
			}else{
				//没有超限时进行信息推送
				FxzfPush.send(entity.getDetectStation(),JSONObject.toJSONString(entity));
			}
		}
		logger.info("===========================信息写入数据库============================ ");
		this.dataSearchDao.insert(entity);
	}
	
	private FxzfMonitor transformToMintor(Fxzf fxzf){
		FxzfMonitor monitor = new FxzfMonitor();
		monitor.setAxisCount(fxzf.getAxisCount());
		monitor.setCaptureTime(fxzf.getCaptureTime());
		monitor.setCarDirection(fxzf.getCarDirection());
		monitor.setCarModel(fxzf.getCarModel());
		
		monitor.setHeadCarPicdir(fxzf.getHeadCarPicdir());
		monitor.setHeadLicensePicdir(fxzf.getHeadLicensePicdir());
		
		monitor.setDetectStation(fxzf.getDetectStation());
		monitor.setId(fxzf.getId());
		monitor.setLane(fxzf.getLane());
		monitor.setLicense(fxzf.getLicense());
		monitor.setLicenseColor(fxzf.getLicenseColor());
		
		monitor.setTailCarPicdir(fxzf.getTailCarPicdir());
		monitor.setTailLicensePicdir(fxzf.getTailLicensePicdir());
		
		monitor.setOverLoadPercent(fxzf.getOverLoadPercent());
		monitor.setOverLoadStatus(fxzf.getOverLoadStatus());
		monitor.setOverLoadPunish(fxzf.getOverLoadPunish());
		monitor.setXxnr(fxzf.getXxnr());
		monitor.setSpeed(fxzf.getSpeed());
		monitor.setWeight(fxzf.getWeight());
		
		return monitor;
	}
	
	private FxzfLane transformToLane(Fxzf fxzf){
		FxzfLane lane = new FxzfLane();
		lane.setAxisCount(fxzf.getAxisCount());
		lane.setCaptureTime(fxzf.getCaptureTime());
		lane.setCarDirection(fxzf.getCarDirection());
		lane.setCarModel(fxzf.getCarModel());
		
		lane.setHeadCarPicdir(fxzf.getHeadCarPicdir());
		lane.setHeadLicensePicdir(fxzf.getHeadLicensePicdir());
		
		lane.setDetectStation(fxzf.getDetectStation());
		lane.setId(fxzf.getId());
		lane.setLane(fxzf.getLane());
		lane.setLicense(fxzf.getLicense());
		lane.setLicenseColor(fxzf.getLicenseColor());
		
		lane.setTailCarPicdir(fxzf.getTailCarPicdir());
		lane.setTailLicensePicdir(fxzf.getTailLicensePicdir());
		
		lane.setOverLoadPercent(fxzf.getOverLoadPercent());
		lane.setOverLoadStatus(fxzf.getOverLoadStatus());
		lane.setOverLoadPunish(fxzf.getOverLoadPunish());
		lane.setXxnr(fxzf.getXxnr());
		lane.setSpeed(fxzf.getSpeed());
		lane.setWeight(fxzf.getWeight());
		
		return lane;
	}

	@Override
	public void update(Fxzf entity) {
		this.dataSearchDao.update(entity);
	}

	@Override
	public void delete(Fxzf entity) {
		this.dataSearchDao.delete(entity);
	}

	@Override
	public void batchInsert(List<Fxzf> fxzfList) {
		
		this.dataSearchDao.batchInsert(fxzfList);
	}

	@Override
	public Boolean warnTrigger(Fxzf fxzf) {
//		if(null!=fxzf&&null!=fxzf.getOverLoadPercent())
//		{
//			
//		}
//		Long overload_percent = null;
//		Long weight = null;
//		// 一桥
//		if ("1,2".contains(fxzf.getDetectStation())) {
//			overload_percent = this.configService
//					.getValueByCode(SysConstants.OVERLOAD_PERCENT_01);
//			weight = this.configService
//					.getValueByCode(SysConstants.WEIGHT_01);
//			// 二桥
//		} else {
//			overload_percent = this.configService
//					.getValueByCode(SysConstants.OVERLOAD_PERCENT_02);
//			weight = this.configService
//					.getValueByCode(SysConstants.WEIGHT_02);
//		}
//		
		return true;
	}

	@Override
	public void checkFxzfMessage(Fxzf condition) {
		dataSearchDao.checkFxzfMessage(condition);
		
	}

	@Override
	public int getOverLoadStatusCount(Fxzf fxzf) {
		// TODO Auto-generated method stub
		return dataSearchDao.getOverLoadStatusCount(fxzf);
	}

	@Override
	public int getIllegalLevelCount(Fxzf fxzf) {
		// TODO Auto-generated method stub
		return dataSearchDao.getIllegalLevelCount(fxzf);
	}

	@Override
	public Pageable<Fxzf> findByPagerDetail(Pageable<Fxzf> pager, Fxzf condition) {
		// TODO Auto-generated method stub
		return dataSearchDao.findByPagerDetail(pager, condition);
	}

	@Override
	public Integer getCountByCondition(Fxzf fxzf) {
		
		return dataSearchDao.getCountByCondition(fxzf);
	}

	@Override
	public Integer getOverLoadCount(Fxzf fxzf) {
		
		return dataSearchDao.getOverLoadCount(fxzf);
	}

	@Override
	public void deleteById(String id) {
		dataSearchDao.deleteById(id);
		
	}

	@Override
	public List<Fxzf> getFxzfToTransport() {
		return dataSearchDao.getFxzfToTransport();
	}

	@Override
	public void upDateFxzftransport(Fxzf fxzf) {
		dataSearchDao.upDateFxzftransport(fxzf);
		
	}

}
