package com.eac.common.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.eac.common.datasource.aspect.DataSourceNames;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <pre>
 * Title :  数据源配置类
 * </pre>
 *
 * @author : xiashilong
 * @since : 2021-11-26
 **/
@Configuration
@EnableTransactionManagement
@MapperScan("com.eac.*.mapper")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceConfig implements TransactionManagementConfigurer, ApplicationContextAware {

    private ApplicationContext applicationContext;

    public static final Set<DataSourceNames> DATA_SOURCE_NAMES = new CopyOnWriteArraySet<>();

    /**
     * 动态数据源配置：
     * 默认数据源为default-db或配置文件中第一个配置的数据源
     * 若未配置数据源，则抛出RuntimeSqlException
     *
     * @return DynamicDataSource
     * @author xiashilong
     * @date 2021/12/1
     */
    @Bean
    public DynamicDataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        addDataSource("DefaultDbDataSource", DataSourceNames.DEFAULT, targetDataSources);
        addDataSource("MysqlDbDataSource", DataSourceNames.MYSQL, targetDataSources);
        addDataSource("CrateDbDataSource", DataSourceNames.CRATEDB, targetDataSources);
        addDataSource("ClickHouseDataSource", DataSourceNames.CLICKHOUSE, targetDataSources);
        DataSourceNames defaultDb;
        if (DATA_SOURCE_NAMES.isEmpty()) {
            throw new RuntimeSqlException("Data source not configured or configured error");
        } else {
            defaultDb = DATA_SOURCE_NAMES.iterator().next();
        }
        DynamicDataSource.setDataSource(defaultDb);
        return new DynamicDataSource((DruidDataSource) targetDataSources.get(DynamicDataSource.getDataSource()), targetDataSources);
    }

    private void addDataSource(String beanName, DataSourceNames dataSourceNames, Map<Object, Object> targetDataSources) {
        if (applicationContext.containsBean(beanName)) {
            DruidDataSource crateDbDataSource = applicationContext.getBean(beanName, DruidDataSource.class);
            if (StringUtils.isEmpty(crateDbDataSource.getUrl())) {
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
                defaultListableBeanFactory.removeBeanDefinition(beanName);
            } else {
                targetDataSources.put(dataSourceNames, crateDbDataSource);
                DATA_SOURCE_NAMES.add(dataSourceNames);
            }
        } else {
            throw new RuntimeSqlException("Unknown data source");
        }
    }

    /**
     * 实例化数据源事务管理器
     */
    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * 指定默认数据源事务管理器，可扩展多数据源
     */
    @Override
    @NonNull
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager();
    }

    /**
     * 创建Mybatis的SqlSessionFactory，共享在Spring应用程序上下文，然后通过依赖注入将SqlSessionFactory传递给DAO
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCallSettersOnNulls(true);
        //开启下划线转驼峰
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:com/eac/**/mapper/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.eac.*.entity");
        return sqlSessionFactoryBean;
    }

    /**
     * 实例化Spring MVC数据校验器
     */
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}


//        import java.io.IOException;
//
//        import javax.sql.DataSource;
//
//        import com.eac.common.datasource.aspect.DataSourceNames;
//        import org.mybatis.spring.SqlSessionFactoryBean;
//        import org.mybatis.spring.annotation.MapperScan;
//        import org.springframework.boot.context.properties.ConfigurationProperties;
//        import org.springframework.boot.jdbc.DataSourceBuilder;
//        import org.springframework.context.annotation.Bean;
//        import org.springframework.context.annotation.Configuration;
//        import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//        import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//        import org.springframework.transaction.PlatformTransactionManager;
//        import org.springframework.transaction.annotation.EnableTransactionManagement;
//        import org.springframework.transaction.annotation.TransactionManagementConfigurer;
//        import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//
//        import com.alibaba.druid.pool.DruidDataSource;
///**
// * 数据源配置类
// *
// */
//@Configuration
//@EnableTransactionManagement
//@MapperScan("com.eac.*.mapper")
//public class DataSourceConfig implements TransactionManagementConfigurer {
//
//    /**
//     * 创建DruidDataSource数据源
//     *
//     * @return
//     */
//    @Bean
////    @ConfigurationProperties(prefix = "spring.datasource")
//    @ConfigurationProperties(prefix = "spring.datasource.druid.default-db")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().type(DruidDataSource.class).build();
//    }
//
//    /**
//     * 实例化数据源事务管理器
//     *
//     * @return
//     */
//    @Bean
//    public PlatformTransactionManager txManager() {
//        return new DataSourceTransactionManager(dataSource());
//    }
//
//    /**
//     * 指定默认数据源事务管理器，可扩展多数据源
//     */
//    @Override
//    public PlatformTransactionManager annotationDrivenTransactionManager() {
//        return txManager();
//    }
//
//    /**
//     * 创建Mybatis的SqlSessionFactory，共享在Spring应用程序上下文，然后通过依赖注入将SqlSessionFactory传递给DAO
//     *
//     * @return
//     * @throws IOException
//     */
//    @Bean
//    public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource());
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//        configuration.setCallSettersOnNulls(true);
//        sqlSessionFactoryBean.setConfiguration(configuration);
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:com/eac/**/mapper/*.xml"));
//        sqlSessionFactoryBean.setTypeAliasesPackage("com.eac.*.entity");
//        return sqlSessionFactoryBean;
//    }
//
//    /**
//     * 实例化Spring MVC数据校验器
//     *
//     * @return
//     */
//    @Bean
//    public Validator validator() {
//        return new LocalValidatorFactoryBean();
//    }
//
//}
