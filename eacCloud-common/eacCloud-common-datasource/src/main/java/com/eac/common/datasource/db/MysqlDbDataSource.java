package com.eac.common.datasource.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * Title : Mysql数据配置
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-30
 **/
@Configuration("MysqlDbDataSource")
@ConfigurationProperties(prefix = "spring.datasource.druid.mysql-db")
public class MysqlDbDataSource extends DruidDataSource implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
    }
}
