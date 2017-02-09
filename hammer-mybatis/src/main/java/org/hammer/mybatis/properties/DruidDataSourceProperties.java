package org.hammer.mybatis.properties;
 
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shenx
 * @date 2017年1月29日
 * @see
 * @version
 */

@Configuration
@ConfigurationProperties(
        locations = "classpath:datasource.yml",
        prefix = DruidDataSourceProperties.DATASOURCE_PREFIX)
public class DruidDataSourceProperties {
     
    public  static final String DATASOURCE_PREFIX = "spring.dbconfig" ;      

    private Boolean testWhileIdele;
    
    private Boolean poolPreparedStatements ;

    private Integer maxPoolPreparedStatementPerConnectionSize;

    private Integer maxWait;

    private Integer minIdle;
    
    private Integer maxActive;

    private Integer maxOpenPreparedStatements;
 
    private String driverClassName;
    
    private String url;
    
    private String username;
    
    private String password;
    
    private String readUrl;
    
    private String readUsername;
    
    private String readPassword;
    
    private String filters ;
    
    
    
    

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public Boolean getPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public Boolean getTestWhileIdele() {
        return testWhileIdele;
    }

    public void setTestWhileIdele(Boolean testWhileIdele) {
        this.testWhileIdele = testWhileIdele;
    }

    public Integer getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public Integer getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Integer getMaxOpenPreparedStatements() {
        return maxOpenPreparedStatements;
    }

    public void setMaxOpenPreparedStatements(Integer maxOpenPreparedStatements) {
        this.maxOpenPreparedStatements = maxOpenPreparedStatements;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReadUrl() {
        return readUrl;
    }

    public void setReadUrl(String readUrl) {
        this.readUrl = readUrl;
    }

    public String getReadUsername() {
        return readUsername;
    }

    public void setReadUsername(String readUsername) {
        this.readUsername = readUsername;
    }

    public String getReadPassword() {
        return readPassword;
    }

    public void setReadPassword(String readPassword) {
        this.readPassword = readPassword;
    }
    
    
}
