package com.eac.common.datasource.aspect;

import com.eac.common.datasource.config.DynamicDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <pre>
 * Title : 多数据源aop
 * 根据配置文件中配置的数据源及DataSource注解，动态切换数据源
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-25
 **/
@Aspect
@Component
public class DataSourceAspect implements Ordered {
    @Pointcut("@annotation(com.eac.common.datasource.aspect.DataSource)")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DataSource ds = method.getAnnotation(DataSource.class);
        DynamicDataSource.setDataSource(ds.value());
        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
