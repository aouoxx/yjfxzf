package com.supconit.kqfx.web.fxzf.avro.redis.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;

import jodd.util.StringUtil;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;

import com.supconit.kqfx.web.fxzf.avro.qbbItemInfo;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardData;
import com.supconit.kqfx.web.fxzf.avro.redis_BoardInfo;
import com.supconit.kqfx.web.fxzf.avro.redis_ConfigInfo;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.list.entities.BlackList;
import com.supconit.kqfx.web.list.entities.WhiteList;
import com.supconit.kqfx.web.list.services.BlackListService;
import com.supconit.kqfx.web.list.services.WhiteListService;
import com.supconit.kqfx.web.util.JedisSource;

@Service("avro_writeredis_service")
public class WriteRedisServiceImpl implements WriteRedisService {

    private static final Logger logger = LoggerFactory.getLogger(WriteRedisServiceImpl.class);

    private static final String BoardInfo_Prefix = "QBB_0";

    private static final String BoardData_Prefix = "QBB_INFO_0";

    @Autowired
    private JedisSource jedisSource;

    @Autowired
    private BlackListService blackListService;

    @Autowired
    private WhiteListService whiteListService;

    @Override
    public String PatternListToRedis(String License, String LicenseColor, Integer flag) {
        if (!StringUtil.isEmpty(License)) {
            if (StringUtil.isEmpty(LicenseColor)) {
                LicenseColor = "";
            }
            if (flag == 1) {
                return "W_" + License + "_" + LicenseColor;
            } else {
                return "B_" + License + "_" + LicenseColor;
            }
        } else {
            return null;
        }
    }


    @Override
    @Transactional
    public void RedisInitList(Integer flag) {
        Jedis jedis = null;
        try {
            jedis = jedisSource.getJedis();
            logger.info("getJedis的Jedis为:" + jedis);
            //判断是否存在黑白名单的key值,如存在则删除
            Set<String> keys = jedis.keys(flag == 1 ? "W_*" : "B_*");
            if (!keys.isEmpty()) {
                for (String string : keys) {
                    jedis.del(string);
                }
            }
            //获取数据库中所有黑白名单的列表,格式成redis中的key值,同步至redis中
            if (1 == flag) {
                List<WhiteList> list = this.whiteListService.getAll();
                String keyvalue = null;

                for (WhiteList whiteList : list) {
                    keyvalue = this.PatternListToRedis(whiteList.getLicense(), whiteList.getPlateColor(), 1);
                    if (null != keyvalue) jedis.set(keyvalue, whiteList.getId());
                }
            } else {
                List<BlackList> list = this.blackListService.getAll();
                String keyvalue = null;

                for (BlackList blackList : list) {
                    keyvalue = this.PatternListToRedis(blackList.getLicense(), blackList.getPlateColor(), 0);
                    if (null != keyvalue) jedis.set(keyvalue, blackList.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            //连接池对象销毁
            jedisSource.returnSource(jedis);
        }
    }

    @Override
    public Boolean RedisExistsList(String License, String LicenseColor, Integer flag) {
        Jedis jedis = null;
        try {
            jedis = jedisSource.getJedis();
            logger.info("getJedis的Jedis为:" + jedis);
            //通过flag判断黑白名单前缀
            String pattern = (flag == 1) ? "W_" : "B_";
            if (StringUtil.isEmpty(License)) {
                return false;
            } else {
                //当车牌颜色为空是模糊匹配
                if (StringUtil.isEmpty(LicenseColor)) {
                    Set<String> keys = jedis.keys(pattern + License + "*");
                    return keys.isEmpty() ? false : true;
                    //当车牌、车牌颜色都非空时判断redis是否存在此key
                } else {
                    return jedis.exists(pattern + License + "_" + LicenseColor);
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        } finally {
            //连接池对象销毁
            jedisSource.returnSource(jedis);
        }
    }

    @Override
    @Transactional
    public void AddListToRedis(String id, String License, String LicenseColor, Integer flag) {
        Jedis jedis = null;
        try {
            jedis = jedisSource.getJedis();
            logger.info("getJedis的Jedis为:" + jedis);
            String pattern = (flag == 1) ? "W_" : "B_";
            if (!StringUtil.isEmpty(id) && !StringUtil.isEmpty(License))
                jedis.set(pattern + License + "_" + LicenseColor, id);
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            //连接池对象销毁
            jedisSource.returnSource(jedis);
        }
    }


    @Override
    public void DeleteListToRedis(String id, String License, String LicenseColor, Integer flag) {
        Jedis jedis = null;
        try {
            jedis = jedisSource.getJedis();
            logger.info("getJedis的Jedis为:" + jedis);
            String pattern = (flag == 1) ? "W_" : "B_";
            if (!StringUtil.isEmpty(id) && !StringUtil.isEmpty(License) && !StringUtil.isEmpty(LicenseColor))
                jedis.del(pattern + License + "_" + LicenseColor);
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            //连接池对象销毁
            jedisSource.returnSource(jedis);
        }
    }

    @Override
    public void WriteBoardInfo(redis_BoardInfo boardInfo, String detectStation) {
        Jedis jedis = null;
        logger.info("==================================");
        try {
            jedis = jedisSource.getJedis();
            logger.info("getJedis的Jedis为:" + jedis);
            if (null != boardInfo && !StringUtil.isEmpty(detectStation)) {
                String dection = null;
                if(detectStation.equals("QBB_WY_01")){
                    dection = "1";
                }else if(detectStation.equals("QBB_WY_02")){
                    dection = "1";
                }else if(detectStation.equals("QBB_DT_01")){
                    dection = "2";
                }else if(detectStation.equals("QBB_DT_02")){
                    dection = "2";
                }
                DatumWriter<redis_BoardInfo> datumWriter = new SpecificDatumWriter<redis_BoardInfo>(redis_BoardInfo.class);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
                datumWriter.write(boardInfo, encoder);
                encoder.flush();
                out.close();
                logger.info("==================================" +BoardInfo_Prefix + dection + out.toString("UTF-8"));
                jedis.rpush(BoardInfo_Prefix + dection, out.toString("UTF-8"));
            }
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            //连接池对象销毁
            jedisSource.returnSource(jedis);
        }
    }


    @Override
    @Transactional
    public void WriteKeyToRedis(String key, redis_ConfigInfo configInfo) {
        Jedis jedis = null;
        try {
            jedis = jedisSource.getJedis();
            logger.info("getJedis的Jedis为:" + jedis);
            if (!StringUtil.isEmpty(key) && configInfo != null) {
                DatumWriter<redis_ConfigInfo> datumWriter = new SpecificDatumWriter<redis_ConfigInfo>(redis_ConfigInfo.class);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
                datumWriter.write(configInfo, encoder);
                encoder.flush();
                out.close();
                jedis.set("SYSTEM_PARAMETER", out.toString("UTF-8"));
            }

        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            //连接池对象销毁
            jedisSource.returnSource(jedis);
        }
    }


    /**
     * 将值写入到实时播放信息中
     */
    @Override
    public void WriteBoardData(redis_BoardData boardData, String detectStation) {
        Jedis jedis = null;
        try {
            jedis = jedisSource.getJedis();
            logger.info("getJedis的Jedis为:" + jedis);
            if (null != boardData && !StringUtil.isEmpty(detectStation)) {
                String dection = null;
                if(detectStation.equals("QBB_WY_01")){
                    dection = "1";
                }else if(detectStation.equals("QBB_WY_02")){
                    dection = "1";
                }else if(detectStation.equals("QBB_DT_01")){
                    dection = "2";
                }else if(detectStation.equals("QBB_DT_02")){
                    dection = "2";
                }
                DatumWriter<redis_BoardData> datumWriter = new SpecificDatumWriter<redis_BoardData>(redis_BoardData.class);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
                datumWriter.write(boardData, encoder);
                encoder.flush();
                out.close();
                jedis.set(BoardData_Prefix + dection, out.toString("UTF-8"));
            }
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            //连接池对象销毁
            jedisSource.returnSource(jedis);
        }
    }


    @Override
    public void writeSynTime(String key, String time) {
        Jedis jedis = null;
        try {
            jedis = jedisSource.getJedis();
            logger.info("getJedis的Jedis为:" + jedis);
            jedis.set(key, time);
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            jedisSource.returnSource(jedis);
        }
    }

    @Override
    public void writeDeviceIdByStation(String key, String deviceId) {
        Jedis jedis = null;
        try {
            jedis = jedisSource.getJedis();
            logger.info("getJedis的Jedis为:" + jedis);
            jedis.lpush(key, deviceId);
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            jedisSource.returnSource(jedis);
        }
    }

}
