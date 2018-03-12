package com.supconit.kqfx.web.fxzf.search.services;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicOrmService;

import java.util.List;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.fxzf.avro.redis.ReadRedisService;
import com.supconit.kqfx.web.fxzf.search.daos.FxzfMonitorDao;
import com.supconit.kqfx.web.fxzf.search.daos.FxzfStaticDao;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfLane;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfMonitor;
import com.supconit.kqfx.web.fxzf.search.entities.FxzfStatic;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnHistoryService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnInfoService;

@Service
public class FxzfStaticServiceImpl extends AbstractBasicOrmService<FxzfStatic, String> implements FxzfStaticService {

	private static final Logger logger = LoggerFactory.getLogger(FxzfStaticServiceImpl.class);

	@Autowired
	private FxzfStaticDao dataSearchDao;

	@Autowired
	private ConfigService configService;

	@Autowired
	private ReadRedisService readRedisService;

	@Autowired
	private WarnInfoService warnInfoService;

	@Autowired
	private WarnHistoryService warnHistoryService;

	@Autowired
	private FxzfMonitorDao fxzfMonitorDao;

	@Autowired
	private FxzfLaneService fxzfLaneService;

	@Override
	public Pageable<FxzfStatic> findByPager(Pageable<FxzfStatic> pager, FxzfStatic condition) {
		return dataSearchDao.findByPager(pager, condition);
	}

	@Override
	public FxzfStatic getById(String arg0) {
		return dataSearchDao.getById(arg0);
	}

	@Override
	public void insert(FxzfStatic entity) {
		/**
		 * 如果车头车牌图片和车尾车牌图片都不存在则图片不存在
		 */
		logger.info("================判断车牌是否非空,设置标志为=====================");
		if (StringUtil.isEmpty(entity.getHeadLicensePicdir()) && StringUtil.isEmpty(entity.getTailLicensePicdir())) {
			entity.setPicFlag(1);// 图片不存在
		} else {
			entity.setPicFlag(0);// 图片存在
		}
		/**
		 * 如果违章车辆没有车牌号，则不信息推送
		 */
		// 车牌号不为空
		// if (!StringUtil.isEmpty(entity.getLicense())) {
		// /**
		// * 获取情报板播放内容
		// */
		// logger.info("================车牌号非空,进行信息推送=====================");
		// if (entity.getOverLoadStatus() > 0) {
		// /**
		// * 判断告警信息表中是否有该告警信息中是否含有该车牌
		// */
		// WarnHistory warnHistory =
		// warnHistoryService.getWarnHistoryQbb(entity);
		// if (warnHistory != null) {
		// entity.setXxnr(warnHistory.getWarnInfo());
		// }
		// /**
		// * 车辆超限时进行信息推送
		// */
		// FxzfPush.send(entity.getDetectStation(),
		// JSONObject.toJSONString(entity));
		// } else {
		// // 没有超限时进行信息推送
		// FxzfPush.send(entity.getDetectStation(),
		// JSONObject.toJSONString(entity));
		// }
		// }
		logger.info("===========================信息写入数据库============================ ");
		this.dataSearchDao.insert(entity);
	}

	private FxzfMonitor transformToMintor(FxzfStatic fxzfStatic) {
		FxzfMonitor monitor = new FxzfMonitor();
		monitor.setAxisCount(fxzfStatic.getAxisCount());
		monitor.setCaptureTime(fxzfStatic.getCaptureTime());
		monitor.setCarDirection(fxzfStatic.getCarDirection());
		monitor.setCarModel(fxzfStatic.getCarModel());

		monitor.setHeadCarPicdir(fxzfStatic.getHeadCarPicdir());
		monitor.setHeadLicensePicdir(fxzfStatic.getHeadLicensePicdir());

		monitor.setId(fxzfStatic.getId());
		monitor.setLane(fxzfStatic.getLane());
		monitor.setLicense(fxzfStatic.getLicense());
		monitor.setLicenseColor(fxzfStatic.getLicenseColor());

		monitor.setTailCarPicdir(fxzfStatic.getTailCarPicdir());
		monitor.setTailLicensePicdir(fxzfStatic.getTailLicensePicdir());

		monitor.setOverLoadPercent(fxzfStatic.getOverLoadPercent());
		monitor.setOverLoadStatus(fxzfStatic.getOverLoadStatus());
		monitor.setOverLoadPunish(fxzfStatic.getOverLoadPunish());
		monitor.setXxnr(fxzfStatic.getXxnr());
		monitor.setSpeed(fxzfStatic.getSpeed());
		monitor.setWeight(fxzfStatic.getWeight());

		return monitor;
	}

	private FxzfLane transformToLane(FxzfStatic fxzfStatic) {
		FxzfLane lane = new FxzfLane();
		lane.setAxisCount(fxzfStatic.getAxisCount());
		lane.setCaptureTime(fxzfStatic.getCaptureTime());
		lane.setCarDirection(fxzfStatic.getCarDirection());
		lane.setCarModel(fxzfStatic.getCarModel());

		lane.setHeadCarPicdir(fxzfStatic.getHeadCarPicdir());
		lane.setHeadLicensePicdir(fxzfStatic.getHeadLicensePicdir());

		lane.setId(fxzfStatic.getId());
		lane.setLane(fxzfStatic.getLane());
		lane.setLicense(fxzfStatic.getLicense());
		lane.setLicenseColor(fxzfStatic.getLicenseColor());

		lane.setTailCarPicdir(fxzfStatic.getTailCarPicdir());
		lane.setTailLicensePicdir(fxzfStatic.getTailLicensePicdir());

		lane.setOverLoadPercent(fxzfStatic.getOverLoadPercent());
		lane.setOverLoadStatus(fxzfStatic.getOverLoadStatus());
		lane.setOverLoadPunish(fxzfStatic.getOverLoadPunish());
		lane.setXxnr(fxzfStatic.getXxnr());
		lane.setSpeed(fxzfStatic.getSpeed());
		lane.setWeight(fxzfStatic.getWeight());

		return lane;
	}

	@Override
	public void update(FxzfStatic entity) {
		this.dataSearchDao.update(entity);
	}

	@Override
	public void delete(FxzfStatic entity) {
		this.dataSearchDao.delete(entity);
	}

	@Override
	public void batchInsert(List<FxzfStatic> fxzfList) {

		this.dataSearchDao.batchInsert(fxzfList);
	}

	@Override
	public Boolean warnTrigger(FxzfStatic fxzfStatic) {
		// if(null!=fxzfStatic&&null!=fxzfStatic.getOverLoadPercent())
		// {
		//
		// }
		// Long overload_percent = null;
		// Long weight = null;
		// // 一桥
		// if ("1,2".contains(fxzfStatic.getDetectStation())) {
		// overload_percent = this.configService
		// .getValueByCode(SysConstants.OVERLOAD_PERCENT_01);
		// weight = this.configService
		// .getValueByCode(SysConstants.WEIGHT_01);
		// // 二桥
		// } else {
		// overload_percent = this.configService
		// .getValueByCode(SysConstants.OVERLOAD_PERCENT_02);
		// weight = this.configService
		// .getValueByCode(SysConstants.WEIGHT_02);
		// }
		//
		return true;
	}

	@Override
	public void checkFxzfMessage(FxzfStatic condition) {
		dataSearchDao.checkFxzfMessage(condition);

	}

	@Override
	public int getOverLoadStatusCount(FxzfStatic fxzfStatic) {
		// TODO Auto-generated method stub
		return dataSearchDao.getOverLoadStatusCount(fxzfStatic);
	}

	@Override
	public int getIllegalLevelCount(FxzfStatic fxzfStatic) {
		// TODO Auto-generated method stub
		return dataSearchDao.getIllegalLevelCount(fxzfStatic);
	}

	@Override
	public Pageable<FxzfStatic> findByPagerDetail(Pageable<FxzfStatic> pager, FxzfStatic condition) {
		// TODO Auto-generated method stub
		return dataSearchDao.findByPagerDetail(pager, condition);
	}

	@Override
	public Integer getCountByCondition(FxzfStatic fxzfStatic) {

		return dataSearchDao.getCountByCondition(fxzfStatic);
	}

	@Override
	public Integer getOverLoadCount(FxzfStatic fxzfStatic) {

		return dataSearchDao.getOverLoadCount(fxzfStatic);
	}

	@Override
	public void deleteById(String id) {
		dataSearchDao.deleteById(id);

	}

	@Override
	public List<FxzfStatic> getFxzfToTransport() {
		return dataSearchDao.getFxzfToTransport();
	}

	@Override
	public void upDateFxzftransport(FxzfStatic fxzfStatic) {
		dataSearchDao.upDateFxzftransport(fxzfStatic);

	}

}
