package org.hammer.service;
  
import java.util.List;
 
import org.hammer.domain.SampleDomain;
import org.hammer.exception.RetException;
import org.hammer.vo.Pageable;

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */
public interface SampleInterface {

    public SampleDomain selectByPk( Integer orderId) throws RetException ;    

    public SampleDomain selectByPkR( Integer orderId) throws RetException  ;
    
    public List<SampleDomain> selectByPkPage(Pageable page) throws RetException  ;
    
}
