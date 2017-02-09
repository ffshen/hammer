package org.hammer.data;

import org.hammer.AppStarter;
import org.hammer.service.SampleInterface;
import org.hammer.utils.JsonUtil;
import org.hammer.vo.Pageable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes = AppStarter.class) // 指定我们SpringBoot工程的Application启动类
@WebIntegrationTest
public class IbatisTest {

    private static final Logger log = LoggerFactory.getLogger(IbatisTest.class); 
    
    private static final Integer orderId = 76 ;
    
    @Autowired
    private SampleInterface i ;
    
    @Test
    @Rollback
    public void testQueryByAnnotationsTest() throws Exception {
        log.info("testQueryByAnnotationsTest ：{}", JsonUtil.toJson(i.selectByPk(orderId)) );
    }
    
    @Test
    public void testQueryRWTest() throws Exception {
        log.info("testQueryRWTest ：{}", JsonUtil.toJson(i.selectByPk(orderId)) );
        log.info("testQueryRWTest ：{}", JsonUtil.toJson(i.selectByPkR(orderId)) );
    }
    
    @Test
    public void testPage() throws Exception {
        Pageable page = new Pageable() ;
        page.setPageSize(2);
        log.info("testPage pageSize 2：{}", JsonUtil.toJson(i.selectByPkPage(page) ) );
        page.setPageSize(10);
        log.info("testPage pageSize 10：{}", JsonUtil.toJson(i.selectByPkPage(page) ) );
        
    }
}
