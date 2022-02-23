package com.eac.common.datasource.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * Title : crate数据配置
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-26
 **/
@Configuration("DefaultDbDataSource")
@ConfigurationProperties(prefix = "spring.datasource.druid.default-db")
public class DefaultDbDataSource extends DruidDataSource implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
    }
}
