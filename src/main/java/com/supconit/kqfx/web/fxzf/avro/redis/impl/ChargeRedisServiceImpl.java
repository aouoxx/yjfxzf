package com.supconit.kqfx.web.fxzf.avro.redis.impl;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.supconit.kqfx.web.fxzf.avro.redis.ChargeRedisService;
import com.supconit.kqfx.web.util.JedisSource;

@Service("chargeRedisServiceImpl")
public class ChargeRedisServiceImpl implements ChargeRedisService {

	private static final Logger logger = LoggerFactory
			.getLogger(ChargeRedisServiceImpl.class);
	
	
	@Autowired
	private  JedisSource jedisSource;


	@Override
	public boolean chargeKey(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisSource.getJedis();
			logger.info("getJedis的Jedis为:"+jedis);
			//判断时候存在该key
			return jedis.exists(key);
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		}finally {
			//连接池对象销毁
			jedisSource.returnSource(jedis);
		}
	}
	
	/**
	 * 列表内容被读取
	 * @param key 为true
	 * @return 否则为false
	 */
	public boolean chargeDeviceKey(String key){
		Jedis jedis = null;
		try {
			jedis = jedisSource.getJedis();
			logger.info("getJedis的Jedis为:"+jedis);
			//判断时候存在该key
			String value = jedis.lpop(key);
			if(value==null||value.equals("")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		}finally {
			//连接池对象销毁
			jedisSource.returnSource(jedis);
		}
	}

	/**
	 * 读取同步时间
	 */
	@Override
	public String readSynKey(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisSource.getJedis();
			logger.info("getJedis的Jedis为:"+jedis);
			//判断时候存在该key
			String time = jedis.get(key);
			return time;
		} catch (Exception e) {
			logger.error(e.toString());
			return "";
		}finally {
			//连接池对象销毁
			jedisSource.returnSource(jedis);
		}
	}

}
