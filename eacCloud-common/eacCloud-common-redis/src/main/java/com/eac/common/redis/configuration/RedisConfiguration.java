package com.eac.common.redis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * Redis的配置类
 *
 */
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfiguration {

	@Value("${redis.host}")
	private String host;// 主机IP
	@Value("${redis.port}")
	private Integer port;// 端口
	@Value("${redis.password}")
	private String password;// 密码
	@Value("${redis.expiredTime}")
	private Long expiredTime;// 过期时间

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Long expiredTime) {
		this.expiredTime = expiredTime;
	}

	@Bean
	public JedisPool getPool() {
		return new JedisPool(this.getHost(), this.getPort());
	}

}
