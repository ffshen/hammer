package org.hammer.service.impl;
  
import org.hammer.domain.SampleDomain;
import org.hammer.exception.RetException;
import org.hammer.service.AppHessianClientAPI;
import org.hammer.service.SampleInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
@Service
public class AppHessianClientAPIImpl implements AppHessianClientAPI {
    
    @Autowired
    private SampleInterface sample ;

    /* (non-Javadoc)
     * @see org.hammer.service.HessianClientAPI#selectByPk(java.lang.Integer)
     */
    @Override
    public SampleDomain selectByPk(Integer orderId) throws RetException {
        return sample.selectByPk(orderId);
    }

}
