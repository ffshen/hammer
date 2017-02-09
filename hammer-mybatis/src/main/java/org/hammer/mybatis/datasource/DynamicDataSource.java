package org.hammer.mybatis.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.collections.MapUtils;
import org.hammer.mybatis.context.DBContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.SQLException; 
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy; 

/**
 * 
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger log = LoggerFactory.getLogger(DynamicDataSource.class);   
    
    public DynamicDataSource() {
    }
    
    public DynamicDataSource(Map<Object, Object> targetDataSources) {
        setTargetDataSources(targetDataSources) ;
    }

    Map<Object, Object> targetDs = null;

    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.getDBType();
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        targetDs = targetDataSources ;
        super.setTargetDataSources(targetDataSources);
    }
 
    @PostConstruct
    public void init() {
        super.afterPropertiesSet();
        log.info("-- DynamicDataSource Init   !");
        if(MapUtils.isNotEmpty(targetDs)) {
            log.info("-- DynamicDataSource Init targetDs isNotEmpty  !");
            targetDs.entrySet().forEach(entry -> {
                if (entry.getValue() instanceof DruidDataSource) {
                    try {
                        ((DruidDataSource)entry.getValue()).init();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if(entry.getValue() instanceof InitializingBean) {
                    try {
                        ((InitializingBean)entry.getValue()).afterPropertiesSet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } );
        }
    }
 
    @PreDestroy
    public void close() {
        log.info("-- DynamicDataSource close   !");
        if(MapUtils.isNotEmpty(targetDs)) {
            targetDs.entrySet().forEach(entry -> {
                if (entry.getValue() instanceof DruidDataSource) {
                    try {
                        ((DruidDataSource)entry.getValue()).close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(entry.getValue() instanceof DisposableBean) {
                    try {
                        ((DisposableBean)entry.getValue()).destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } );
        }
    }
}
