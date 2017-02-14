package org.hammer.kafka;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.pool2.ObjectPool; 
import org.hammer.TestStarter; 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage; 

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes = TestStarter.class) // 指定我们SpringBoot工程的Application启动类
@WebIntegrationTest//不加此注解，会报异常，NoSuchBeanDefinitionException，javax.servlet.ServletContext
@ComponentScan("org.hammer")
public class ProducerTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());      
	
	@Autowired
	ObjectPool<Producer<String, String>>  simiPool ;

    @Test
    public void kafkaProducer() throws Exception { 
    	Random rnd = new Random();
		for (long nEvents = 0; nEvents < 10; nEvents++) { 	
			Producer<String, String> producer = null  ;
			try{
				producer =  simiPool.borrowObject() ;			
	            long runtime = new Date().getTime();              
	            String msg = "192.168.3." + rnd.nextInt(10);             
	            String key = runtime + msg;             
	            KeyedMessage<String, String> data = new KeyedMessage<String, String>("example-metric1", key ,msg );            
	            logger.info(" KafkaTest kafkaProducer Test SendMsg : {} ",msg);            
	            producer.send(data);     
	            Thread.sleep(500);
			}
			finally{
				Boolean isProducerNull = Objects.isNull(producer) ;
				if(BooleanUtils.isFalse(isProducerNull) ){
					simiPool.returnObject(producer);
				}
			}
		}
		simiPool.close();
    }
     
}
