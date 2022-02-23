package com.eac.common.redis.util;

import com.eac.common.redis.configuration.RedisConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * <pre>
 * Title :  Redis的工具类
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@Component
public class RedisUtils {

	@Autowired
	private RedisConfiguration redisConfiguration;
	@Autowired
	private JedisPool pool;

	/**
	 * 持久化数据
	 */
	public void psetex(String key, String value) {
					Jedis jedis = null;
					try {
						jedis = pool.getResource();
						jedis.auth(redisConfiguration.getPassword());
						jedis.psetex(key, redisConfiguration.getExpiredTime() * 1000L, value);
						// 如果key是对URL授权的角色编码，则不过期
						if (key.contains("EAC:URL:ROLECODE")) {
							jedis.persist(key);
						}
					} finally {
						if (jedis != null) {
							jedis.close();
			}
		}
	}

	/**
	 * 根据键删除数据
	 *
	 * @param key
	 */
	public void del(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(redisConfiguration.getPassword());
			jedis.del(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 根据键获取值
	 *
	 * @param key 键
	 * @return
	 */
	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(redisConfiguration.getPassword());
			return jedis.get(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

}
