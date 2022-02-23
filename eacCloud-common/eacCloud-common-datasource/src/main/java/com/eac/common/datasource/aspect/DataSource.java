package com.eac.common.datasource.aspect;

import java.lang.annotation.*;

/**
 * <pre>
 * Title : 数据源配置注解
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-25
 **/
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    DataSourceNames value() default DataSourceNames.DEFAULT;
}
