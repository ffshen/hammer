package org.hammer.service.impl;
 
import java.util.List;

import org.hammer.context.AppContext;
import org.hammer.domain.SampleDomain;
import org.hammer.exception.RetException;
import org.hammer.mapper.SampleMapper;
import org.hammer.mybatis.annotation.DataSourceType;
import org.hammer.mybatis.context.DBContextHolder;
import org.hammer.service.SampleInterface;
import org.hammer.vo.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */
@Service
public class SampleInterfaceImpl implements SampleInterface {
    

    private static final Logger log = LoggerFactory.getLogger(SampleInterfaceImpl.class);
    
    @Autowired
    private SampleMapper sampleMapper ;

    /* (non-Javadoc)
     * @see org.hammer.service.SampleInterface#selectByPk(java.lang.Integer)
     */
    @Override
    public SampleDomain selectByPk(Integer orderId)  throws RetException {
        if(log.isInfoEnabled()){
            log.info("testHessian hessiancontext traceid : {}", AppContext.getTraceId() );
            log.info("selectByPk param : {}",orderId);
        }
        
//        RestClient.getRestTemplate().postForObject("", SampleDomain.class, String.class) ;
        
        return sampleMapper.selectByPk(orderId) ;
    }

    /* (non-Javadoc)
     * @see org.hammer.service.SampleInterface#selectByPkR(java.lang.Integer)
     */
    @Override
    @DataSourceType(DBContextHolder.DBType.R)
    public SampleDomain selectByPkR(Integer orderId)  throws RetException {  
        return selectByPk(orderId) ;
    }

    /* (non-Javadoc)
     * @see org.hammer.service.SampleInterface#selectByPkPage(org.hammer.core.data.vo.Pageable)
     */
    @Override
    public List<SampleDomain> selectByPkPage(Pageable page) throws RetException {
        return sampleMapper.selectByPkPage(page);
    }

}
