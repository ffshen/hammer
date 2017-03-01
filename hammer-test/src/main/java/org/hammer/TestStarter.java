package org.hammer;
    
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer; 
import org.springframework.context.annotation.ComponentScan; 
 
 
 

/**
 * @author shenx
 * @date 2017年1月25日
 * @see
 * @version
 */
@SpringBootApplication 
@EnableAutoConfiguration
@ComponentScan(basePackages={TestStarter.baseBackages})
public class TestStarter implements EmbeddedServletContainerCustomizer,CommandLineRunner{
    
    public static final String baseBackages = "org.hammer" ;

    private static Logger logger = LoggerFactory.getLogger(TestStarter.class);

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
        
    }

    /* (non-Javadoc)
     * @see org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer#customize(org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer)
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(10088);        
    }
     
 
}
