package com.supconit.kqfx.web.fxzf.avro.redis.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;
import jodd.util.StringUtil;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;

import com.supconit.kqfx.web.device.entities.Redis_DeviceData;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardData;
import com.supconit.kqfx.web.fxzf.avro.redis_FxzfInfo;
import com.supconit.kqfx.web.fxzf.avro.redis.ReadRedisService;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;
import com.supconit.kqfx.web.fxzf.search.services.FxzfSearchService;
import com.supconit.kqfx.web.fxzf.search.services.IllegalTrailService;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnInfo;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnHistoryService;
import com.supconit.kqfx.web.fxzf.warn.services.WarnInfoService;
import com.supconit.kqfx.web.list.services.BlackListService;
import com.supconit.kqfx.web.util.EncodeTransforUtil;
import com.supconit.kqfx.web.util.JedisSource;
import com.supconit.kqfx.web.util.SysConstants;
/**
 * 从redis中读取内容显示
 * @author gs
 */
@Service("avro_readredis_service")
public class ReadRedisServiceImpl implements ReadRedisService {

	private static final Logger logger = LoggerFactory.getLogger(ReadRedisServiceImpl.class);

	private static final String BoardData_Prefix = "QBB_INFO_0";

	private static final String FxzfInfo = "FXZF_DATA_DY";

	private static final BlockingQueue<Fxzf> fxzfQueue = new ArrayBlockingQueue<Fxzf>(
			5000);

	private ExecutorService executorService = Executors.newCachedThreadPool();

	private static final Integer threadSzie = 10;

	@Autowired
	private  JedisSource jedisSource;

	@Autowired
	private WriteRedisService writeRedisService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private IllegalTrailService illegalTrailService;

	@Autowired
	private BlackListService blackListService;

	@Autowired
	private WarnHistoryService warnHistoryService;

	@Autowired
	private FxzfSearchService fxzfSearchService;

	@Autowired
	private WarnInfoService	warnInfoService;

	@Value("${fxzfdata.llen}")
	private long fxzfdata_llen;

	@Override
	public redis_BoardData ReadBoardData(String detectStation) {
		Jedis jedis = null;
		redis_BoardData boardData = null;
		try {
			jedis = jedisSource.getJedis();
			logger.info("getJedis的Jedis为:"+jedis);
			String key = BoardData_Prefix + detectStation;
			if (jedis.exists(key)) {
				String result = jedis.get(key);
				DatumReader<redis_BoardData> reader = new SpecificDatumReader<redis_BoardData>(
						redis_BoardData.class);
				Decoder decoder = DecoderFactory.get().binaryDecoder(
						result.getBytes("UTF-8"), null);
				boardData = reader.read(null, decoder);
			}
		} catch (IOException e) {
			logger.error(e.toString());
		} finally {
			// 连接池对象销毁
			jedisSource.returnSource(jedis);
		}
		return boardData;
	}

	@Override
	@Transactional
	public List<redis_FxzfInfo> ReadFxzfInfo() {
		Jedis jedis = null;
		List<redis_FxzfInfo> result = new ArrayList<redis_FxzfInfo>();
		try {
			jedis = jedisSource.getJedis();
			logger.info("getJedis的Jedis为:"+jedis);
			List<String> fxzfData = jedis.blpop(0, FxzfInfo);

			redis_FxzfInfo fxzfInfo = null;
			for (String string : fxzfData) {
				if (!FxzfInfo.equals(string)) {
					DatumReader<redis_FxzfInfo> reader = new SpecificDatumReader<redis_FxzfInfo>(redis_FxzfInfo.class);
					Decoder decoder = DecoderFactory.get().binaryDecoder(string.getBytes("UTF-8"), null);
					fxzfInfo = reader.read(null, decoder);
					logger.info("====原始车头车牌："+fxzfInfo.getSHeadLicense()+"");
					logger.info("====原始车头车牌颜色："+fxzfInfo.getSHeadLicenseColor()+"");
					logger.info("====原始车头车牌地址："+fxzfInfo.getSHeadLicensePicDir()+"");
					logger.info("====原始车头车辆地址："+fxzfInfo.getSHeadCarPicDir()+"");
					result.add(fxzfInfo);
					// 将数据获取放入队列中
					fxzfQueue.put(Fxzf.translateFxzf(fxzfInfo));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 连接池对象销毁
			jedisSource.returnSource(jedis);
		}
		return result;
	}

	
	
	/**
	 * 获取设备实时状态信息
	 */
	@Override
	public List<Redis_DeviceData> ReadDeviceData(String key) {
		Jedis jedis = null;
		List<Redis_DeviceData> result = new ArrayList<Redis_DeviceData>();
		try {
			jedis = jedisSource.getJedis();
			logger.info("getJedis的Jedis为:"+jedis);
			List<String> deviceDatas = jedis.blpop(0, key);
			Redis_DeviceData redisDeviceData = null;
			for(String deviceData:deviceDatas){
				if(!deviceData.equals(key)){
					DatumReader<Redis_DeviceData> reader = new SpecificDatumReader<Redis_DeviceData>(
							Redis_DeviceData.class);
					Decoder decoder = DecoderFactory.get().binaryDecoder(
							deviceData.getBytes("UTF-8"), null);
					redisDeviceData = reader.read(null, decoder);
					result.add(redisDeviceData);
				}
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String RedisValueList(String License, String LicenseColor,
			Integer flag) {
		Jedis jedis = null;
		String result = null;
		try {
			jedis = jedisSource.getJedis();
			logger.info("getJedis的Jedis为:"+jedis);
			String key = this.writeRedisService.PatternListToRedis(License,
					LicenseColor, flag);
			result = jedis.get(key);
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			// 连接池对象销毁
			jedisSource.returnSource(jedis);
		}
		return result;
	}

	@Override
	@Transactional
	public void ReceiveRealTimeData() {
		for (int i = 0; i < threadSzie; i++) {
			executorService.execute(new handler(fxzfQueue));
		}
		while (true) {
			ReadFxzfInfo();
		}
	}

	class handler implements Runnable {
		private BlockingQueue<Fxzf> fxzfQueue;

		public handler(BlockingQueue<Fxzf> fxzfQueue) {
			this.fxzfQueue = fxzfQueue;
		}

		@Override
		public void run() {
			while (true) {
				try {
					handler(fxzfQueue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Transactional
	public void handler(Fxzf fxzf) {
		try {
			if (null == fxzf) return;
			logger.info("=======================对接入数据进行处理=================================");
			/** 获取车重和长宽高的超限率和超限*/
			Double overload_percent = null;
			Double overload_weight = null;
			overload_percent = this.configService.getValueByCode(SysConstants.OVERLOAD_PERCENT);
			overload_weight = this.configService.getValueByCode(SysConstants.OVERLOAD_WEIGHT);
			fxzf.setLicense(fxzf.getHeadLicense().equals("-1")?null:fxzf.getHeadLicense());
			fxzf.setLicenseColor(fxzf.getHeadLicenseColor().equals("-1")?null:fxzf.getHeadLicenseColor());
			logger.info("===============车牌号:"+fxzf.getLicense());
			logger.info("===============车牌颜色："+fxzf.getLicenseColor());
			
			if (!StringUtil.isEmpty(fxzf.getLicense())) {
				//***************判断是否为白名单****************\\
				if (!writeRedisService.RedisExistsList(fxzf.getLicense(),fxzf.getLicenseColor(), 1)) {
					//***************判断是否为黑名单****************\\
					if (!writeRedisService.RedisExistsList(fxzf.getLicense(),fxzf.getLicenseColor(), 0)) {
						//***************判断是否超限****************\\
						if (fxzf.getOverLoadStatus() > 0) {
							//***************同步至车辆轨迹表****************\\
							this.illegalTrailService.syncIllegalTrail(fxzf.getLicense(), fxzf.getLicenseColor());
							//***************判断是否已经符合黑名单,如果符合则更新黑名单并同步至redis中****************\\
							if (this.illegalTrailService.IsAccordBlackList(fxzf.getLicense(), fxzf.getLicenseColor())) {
								this.blackListService.syncBlackList(fxzf.getLicense(), fxzf.getLicenseColor());
							}
						}
					}else {
						if (fxzf.getOverLoadStatus() > 0) {
							logger.info(fxzf.getLicense() +"同步数据至车辆轨迹表");
							this.illegalTrailService.syncIllegalTrail(fxzf.getLicense(), fxzf.getLicenseColor());
							logger.info(fxzf.getLicense() +"更新黑名单");
							this.blackListService.syncBlackList(fxzf.getLicense(),fxzf.getLicenseColor());
						}
					}
					

					
					logger.info(fxzf.getLicense() +"判断是否超过告警阀值");
					if ((null != overload_percent && fxzf.getOverLoadPercent() > overload_percent.doubleValue())
					  ||(null != overload_weight && fxzf.getWeight() > overload_weight.doubleValue())) {
						
						/***设置告警信息*/
						fxzf.setWarnFlag(1);
						/***判断是否为24小时内已告警信息,如果是则入库，否则发布告警至情报板、web端，入库"*/
						if (!this.warnHistoryService.IsPublishWarnMessage(fxzf.getLicense(), fxzf.getLicenseColor())) {
							/************不是24小时已告警信息***********/
							logger.info(fxzf.getLicense() +"发布告警至情报板、web端");
							//超限发布到情报板
							if ((null != overload_percent && fxzf.getOverLoadPercent() > overload_percent.doubleValue())
									  ||(null != overload_weight && fxzf.getWeight() > overload_weight.doubleValue())){
								fxzf.setWarnType("CHAOZAI");
								this.warnHistoryService.publishToQbb(fxzf);
								this.warnHistoryService.publishToWeb(fxzf);
							}else{
							//超限发布到情报板	
								fxzf.setWarnType("CHAOXIAN");
								this.warnHistoryService.publishToQbb(fxzf);
								this.warnHistoryService.publishToWeb(fxzf);
							}
							
							//设置推送情报板信息
							if(!StringUtil.isEmpty(fxzf.getDetectStation())){
								WarnInfo warnInfo = new WarnInfo();
								warnInfo.setDetectStation(fxzf.getDetectStation());
								warnInfo.setWarnType(fxzf.getWarnType());
							    warnInfo = this.warnInfoService.getByTempletTypeAndStation(warnInfo);
								fxzf.setXxnr(EncodeTransforUtil.getXxnrResult(this.warnHistoryService.getWarnInfo(fxzf, warnInfo),null));
							}
						}
					} else {
						//没有超过告警阀值，但是是黑名单进行web端告警
						if (writeRedisService.RedisExistsList(fxzf.getLicense(),fxzf.getLicenseColor(), 0)) {
							logger.info(fxzf.getLicense() +"web端告警");
							this.warnHistoryService.publishToWeb(fxzf);
						}
					}
				 //非空车牌,非白名单信息
				 fxzf.setTransport(0);
				}
				//非空车牌0标志未通过webservice进行传送
			}
			 //全部车牌插入
			 this.fxzfSearchService.insert(fxzf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
