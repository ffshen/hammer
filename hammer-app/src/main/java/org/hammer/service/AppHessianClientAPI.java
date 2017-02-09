package org.hammer.service;

import org.hammer.domain.SampleDomain;
import org.hammer.exception.RetException;
import org.hammer.hessian.client.HessianClientAPI;

public interface AppHessianClientAPI extends HessianClientAPI {
    
    public SampleDomain selectByPk( Integer orderId) throws RetException ;
    
}
