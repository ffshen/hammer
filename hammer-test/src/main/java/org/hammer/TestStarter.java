package org.hammer;
    
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties; 
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;  
 
 
 

/**
 * @author shenx
 * @date 2017年1月25日
 * @see
 * @version
 */
@SpringBootApplication 
@EnableAspectJAutoProxy
@ConfigurationProperties(locations = "classpath:application.yml",prefix = "server")
@ComponentScan(basePackages={TestStarter.baseBackages})
public class TestStarter implements EmbeddedServletContainerCustomizer,CommandLineRunner{
    
    public static final String baseBackages = "org.hammer" ;

    private static Logger logger = LoggerFactory.getLogger(TestStarter.class);
     
    private String port ;    

    public static void main( String[] args ) {
        try{
            SpringApplication.run(TestStarter.class, args);            
        }
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            throw ex ;
        }
    }
    
    /* (non-Javadoc)
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("- init : app starting");
        
    }

    /* (non-Javadoc)
     * @see org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer#customize(org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer)
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        logger.info("- init : server port : {}",getPort());
        container.setPort(Integer.parseInt(getPort()));        
    }
    
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
