package com.eac.common.datasource.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Title : clickhouse数据配置
 **/
@Configuration("ClickHouseDataSource")
@ConfigurationProperties(prefix = "spring.datasource.druid.clickhouse-db")
public class ClickHouseDataSource extends DruidDataSource implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
    }
}
