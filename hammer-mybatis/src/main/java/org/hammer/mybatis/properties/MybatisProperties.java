package org.hammer.mybatis.properties;
 

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shenx
 * @date 2017年1月25日
 * @see
 * @version
 */
@Configuration
@ConfigurationProperties(
        locations = "classpath:mybatis.yml",
        prefix = MybatisProperties.MYBATIS_PREFIX)
public class MybatisProperties  { 
    
    public static final String MYBATIS_PREFIX = "mybatis";
    
    /**
     * Config file path.
     */
    private String configLocation;
    
    /**
     * Location of mybatis mapper files.
     */
    private String mapperLocation;
    
    /**
     * Package to scan domain objects.
     */
    private String typeAliasesPackage;
    
    private String basePackage ;
    
    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getConfigLocation() {
      return this.configLocation;
    }
 
    public void setConfigLocation(String configLocation) {
      this.configLocation = configLocation;
    }

    public String getMapperLocation() {
        return mapperLocation;
    }

    public void setMapperLocation(String mapperLocation) {
        this.mapperLocation = mapperLocation;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }
     
    
}
