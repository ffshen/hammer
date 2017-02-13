package org.hammer.zk;
 
import org.hammer.TestStarter;
import org.hammer.zookeeper.curator.ICuratorClient;
import org.hammer.zookeeper.curator.impl.CuratorLock;
import org.hammer.zookeeper.curator.impl.CuratorLock.CuratorLockSupplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes = TestStarter.class) // 指定我们SpringBoot工程的Application启动类
@WebIntegrationTest//不加此注解，会报异常，NoSuchBeanDefinitionException，javax.servlet.ServletContext
@ComponentScan("org.hammer")
public class ZkTest {
	

    Logger logger = LoggerFactory.getLogger(this.getClass());  
    
    private static final String UniqueCodePath = "/test/atomic" ;
    
    private static final String LockCodePath = "/test/lock" ;
    
    @Autowired
    private ICuratorClient curatorClient ;
    
    @Autowired
    private CuratorLock lock ;
    
    @Test
    public void getUniqueCode() throws Exception{
    	logger.info("- CuratorClientTest getUniqueCode : {} " , curatorClient.getUniqueCode(UniqueCodePath) );
    }
    
    @Test
    public void lock() throws Exception {
    	lock.lock(LockCodePath,  locksupplier );
    	lock.lock(LockCodePath,  locksupplier1 );
    }

    private CuratorLockSupplier locksupplier  = ()->{
    	logger.info("--- Lock 1 Logic ---");
    	return null ;
    } ;
    
    private CuratorLockSupplier locksupplier1  = ()->{
    	logger.info("--- Lock 2 Logic ---");
    	return null ;
    } ;
}
