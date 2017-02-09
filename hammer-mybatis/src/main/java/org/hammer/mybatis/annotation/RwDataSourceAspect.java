package org.hammer.mybatis.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hammer.mybatis.context.DBContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
@Aspect
@Component 
@Order(-1)
public class RwDataSourceAspect    {

    private static final Logger logger = LoggerFactory.getLogger(RwDataSourceAspect.class);
    
    @Around("@annotation(dataSourceType)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, DataSourceType dataSourceType) throws Throwable {
        try {
            logger.info("set database connection to {}" ,dataSourceType.value().value());
            DBContextHolder.setDBType(dataSourceType.value().value());
            Object result = proceedingJoinPoint.proceed();
            return result;
        } finally {
            DBContextHolder.clearDBType();
            logger.info("restore database connection");
        }
    }
 
}
