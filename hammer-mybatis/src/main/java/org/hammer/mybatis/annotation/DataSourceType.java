package org.hammer.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hammer.mybatis.context.DBContextHolder;
 

/**
 * 
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceType {
    
    DBContextHolder.DBType value() default DBContextHolder.DBType.RW;
    
}
