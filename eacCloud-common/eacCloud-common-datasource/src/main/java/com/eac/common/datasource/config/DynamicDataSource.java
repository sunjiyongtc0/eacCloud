package com.eac.common.datasource.config;

import com.eac.common.datasource.aspect.DataSourceNames;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * <pre>
 * Title : 动态数据源配置类
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-25
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DataSourceNames> contextHolder = new ThreadLocal<>();

    /**
     * 配置DataSource, defaultTargetDataSource为主数据库
     */
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(DataSourceNames dataSource) {
        contextHolder.set(dataSource);
    }

    public static DataSourceNames getDataSource() {
        return contextHolder.get() != null ? contextHolder.get() : DataSourceNames.DEFAULT;
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }

}
