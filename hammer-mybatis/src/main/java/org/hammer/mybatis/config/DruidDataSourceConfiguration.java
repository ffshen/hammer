package org.hammer.mybatis.config;
 
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.hammer.mybatis.context.DBContextHolder;
import org.hammer.mybatis.datasource.DynamicDataSource;
import org.hammer.mybatis.properties.DruidDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps; 

/**
 * @author shenx
 * @date 2017年1月29日
 * @see
 * @version
 */

@Configuration
@Import(DruidDataSourceProperties.class)
public class DruidDataSourceConfiguration {
   
    
    @Autowired
    private DruidDataSourceProperties druidDataSourceProperties ;    
    
    @Bean
    public Map<Object, Object> targetDataSource() throws SQLException{
        DataSource rwDataSource = createDataSource(druidDataSourceProperties.getDriverClassName(), druidDataSourceProperties.getUrl(), druidDataSourceProperties.getUsername(), druidDataSourceProperties.getPassword());
        DataSource rDataSource = createDataSource(druidDataSourceProperties.getDriverClassName(), druidDataSourceProperties.getReadUrl(), druidDataSourceProperties.getReadUsername(), druidDataSourceProperties.getReadPassword());
        Map<Object, Object> targetDataSource = Maps.newHashMap();
        targetDataSource.put(DBContextHolder.DBType.RW.value(), rwDataSource);
        targetDataSource.put(DBContextHolder.DBType.R.value(), rDataSource);
        return targetDataSource ;
    }
    
    @Bean
    public DataSource dynamicDruidDataSource() throws SQLException{
        DynamicDataSource dynamicDataSource = new DynamicDataSource(targetDataSource());
        dynamicDataSource.setTargetDataSources(targetDataSource());
        dynamicDataSource.setDefaultTargetDataSource(targetDataSource().get(DBContextHolder.DBType.RW.value()));
        return dynamicDataSource ; 
    }
    
    private DataSource createDataSource(String driver, String url, String username, String password) throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setTestWhileIdle(true);        
        druidDataSource.setMaxWait(druidDataSourceProperties.getMaxWait());
        druidDataSource.setMinIdle(druidDataSourceProperties.getMinIdle());
        druidDataSource.setMaxActive(druidDataSourceProperties.getMaxActive());
        druidDataSource.setPoolPreparedStatements(druidDataSourceProperties.getPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(druidDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        druidDataSource.setMaxOpenPreparedStatements(druidDataSourceProperties.getMaxOpenPreparedStatements());
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setFilters(druidDataSourceProperties.getFilters());
        return druidDataSource;
    }
}
