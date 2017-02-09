package org.hammer.mybatis.config;

import java.util.Objects;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hammer.mybatis.plugin.PageInterceptor;
import org.hammer.mybatis.properties.MybatisProperties;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author shenx
 * @date 2017年1月28日
 * @see
 * @version
 */

@Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@EnableTransactionManagement()
@Import(MybatisProperties.class) 
@MapperScan(basePackages = {MybatisAutoConfiguration.BasePackages}, sqlSessionFactoryRef = MybatisAutoConfiguration.SqlSessionFactoryBeanName )
public class MybatisAutoConfiguration implements TransactionManagementConfigurer { 
    
    private static final Logger log = LoggerFactory.getLogger(MybatisAutoConfiguration.class); 
    
    public static final String SqlSessionFactoryBeanName = "sqlSessionFactory" ;
    
    public static final String BasePackages = "org.hammer.mapper" ;

    @Autowired
    DataSource dataSource;
    
    @Autowired
    private MybatisProperties properties;
    
    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    
    @Bean(name = SqlSessionFactoryBeanName)
    public SqlSessionFactory sqlSessionFactory() throws Exception {
      if(Objects.isNull(dataSource)){
          log.error("-init SqlSessionFactory error,dataSource is null !") ;
          throw new RuntimeException("-init SqlSessionFactory error,dataSource is null !");
      }
      SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
      factory.setDataSource(dataSource); 
      //配置文件所在的包路径
      if (StringUtils.hasText(this.properties.getConfigLocation())) {
        factory.setConfigLocation(this.resolver.getResource(this.properties.getConfigLocation()));
      }
      //当使用@MapperScan时候，以下配置可以不进行配置
      //解决java -jar启动时配置的Domain别名失效问题
      //但一般配置的时候,result可以不写别名，而写全路径
//      factory.setVfs(SpringBootVFS.class);
      //Domain，Pojo所在的包路径
      if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
          factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
      }
      //Mapper做在的包的路径
      if (!ObjectUtils.isEmpty(this.properties.getMapperLocation())) {        
        factory.setMapperLocations(resolver.getResources(this.properties.getMapperLocation()));
      } 
      buildInterceptor(factory) ;
      return factory.getObject();
    }
    
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /* (non-Javadoc)
     * @see org.springframework.transaction.annotation.TransactionManagementConfigurer#annotationDrivenTransactionManager()
     */
    @Override
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
    
    private void buildInterceptor(SqlSessionFactoryBean factory){
        PageInterceptor page = new PageInterceptor() ;
        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");
        properties.setProperty("pageSqlId", ".*Page.*");
        page.setProperties(properties);
        //
        factory.setPlugins(new Interceptor[]{page});
    }
}
