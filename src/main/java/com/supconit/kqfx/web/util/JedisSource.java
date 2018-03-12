package com.supconit.kqfx.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import jodd.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class JedisSource {
	private transient static final Logger logger = LoggerFactory
			.getLogger(JedisSource.class);
	private static Map<Integer, Map<String, String>> redisMap;
	private Map<Integer, JedisPool> map;
	private static JedisPool jedisPool;

	public JedisSource() {
		try {
			Properties properties = new Properties();
			InputStream is = JedisSource.class
					.getResourceAsStream("/config-dev.properties");
			properties.load(is);
			if (properties.isEmpty())
				throw new IOException("找不到config-dev.properties");
			// 将配置的redis服务器参数转换成map形式
			redisMap = new HashMap<Integer, Map<String, String>>();
			for (Entry<Object, Object> entry : properties.entrySet()) {
				String key = (String) entry.getKey();
				String val = (String) entry.getValue();
				if (StringUtils.isEmpty(key))
					continue;
				if (key.substring(0, 5).equals("redis")) {
					int serverIndex = Integer.valueOf(key.substring(6, 7));
					if (redisMap.get(serverIndex) == null) {
						Map<String, String> oneRedisMap = new HashMap<String, String>();
						oneRedisMap.put(key.substring(9).trim(), val);
						redisMap.put(serverIndex, oneRedisMap);
					} else {
						Map<String, String> oneRedisMap = redisMap
								.get(serverIndex);
						oneRedisMap.put(key.substring(9).trim(), val);
					}
				}
			}
			map = new HashMap<Integer, JedisPool>();
			for (Entry<Integer, Map<String, String>> entry : redisMap
					.entrySet()) {
				Integer key = entry.getKey();
				Map<String, String> val = entry.getValue();
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(Integer.valueOf(val.get("maxActive")));
				config.setMaxIdle(Integer.valueOf(val.get("maxIdle")));
				config.setMaxWaitMillis(Long.valueOf(val.get("maxWait")));
				config.setTestOnBorrow(true);
				JedisPool jedisPool = null;
				if(StringUtil.isEmpty(val.get("password"))){
					jedisPool = new JedisPool(config, val.get("host"),
							Integer.valueOf(val.get("port")));
				}else{
					jedisPool = new JedisPool(config, val.get("host"),
							Integer.valueOf(val.get("port")),Integer.valueOf(val.get("maxWait")),val.get("password"));
				}
				map.put(key, jedisPool);
				logger.info("初始化redis连接池成:" + val.get("host"));
			}
			jedisPool = map.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}
	
	public Jedis getJedis(){
		logger.info("getJedis的jeisPool为:"+jedisPool);
		String password = PropertiesUtil.getByCode("redis[0].password");
		Jedis jedis = jedisPool.getResource();
		if(!StringUtil.isEmpty(password)){jedis.auth(password);}
		return jedis;
	}
	
	public void  returnSource(Jedis jedis){
		logger.info("returnSource的jeisPool为:"+jedisPool+"\t       NumActive():"+jedisPool.getNumActive());
		if(null!=jedis){
			jedisPool.returnResource(jedis);
		}
	}
}
